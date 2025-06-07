package com.dota.tracker.pudge.service

import com.dota.tracker.exception.ProcessingException
import com.dota.tracker.job.JobStatus
import com.dota.tracker.job.JobStatusRepository
import com.dota.tracker.opendota.MatchRepository
import com.dota.tracker.opendota.model.DotaMatch
import com.dota.tracker.pudge.model.HookStats
import com.dota.tracker.pudge.model.HookStatsResult
import com.dota.tracker.pudge.repository.HookStatsRepository
import com.dota.tracker.pudge.repository.PudgeParserRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.retry.Retry
import java.time.Duration
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class HookStatsService(
    private val hookStatRepository: HookStatsRepository,
    private val jobStatusRepository: JobStatusRepository,
    private val matchRepository: MatchRepository,
    private val pudgeParserRepository: PudgeParserRepository,
) {

    fun submitMatchProcessing(matchId:String): String{
        val jobId = UUID.randomUUID().toString()

        // Save initial job status
        jobStatusRepository.save(JobStatus(jobId, "PROCESSING", null))

        // Process asynchronously
        processMatchAsync(matchId, jobId)

        return jobId
    }
    @Async("taskExecutor")
    fun processMatchAsync(matchId: String, jobId: String) {
        // Step 1: Get match data
        updateJobStatus(jobId, "FETCHING_MATCH_DATA")


        matchRepository.getMatchDetail(matchId)
            .flatMap { matchData: DotaMatch ->
                if (matchData.od_data.has_parsed) {
                    // Match is already parsed, proceed directly
                    Mono.just(matchData)
                } else {
                    // Step 2: Request parsing and poll until complete
                    updateJobStatus(jobId, "REQUESTING_PARSE")
                    matchRepository.requestToParse(matchId).then(
                        pollForParsedMatch(matchId, jobId)
                    )
                }
            }
            .flatMap { parsedMatchData ->
                // Step 3: Calculate hook stats
                updateJobStatus(jobId, "CALCULATING_STATS")
                val hookStats = pudgeParserRepository.parseReplayFromUrl(parsedMatchData)

                // Step 4: Save to database
                updateJobStatus(jobId, "SAVING_RESULTS")
                Mono.fromCallable<HookStats> { hookStatRepository.save(hookStats) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .doOnSuccess { savedStats ->
                // Step 5: Complete job
                completeJob(jobId, savedStats)
            }
            .doOnError { error ->
                failJob(jobId, error.message ?: "Unknown error")
            }
            .subscribe()
    }
    private fun pollForParsedMatch(matchId: String, jobId: String): Mono<DotaMatch> {
        return Mono.defer {
            matchRepository.getMatchDetail(matchId)
        }
            .flatMap { matchData ->
                if (matchData.od_data.has_parsed) {
                    Mono.just(matchData)
                } else {
                    Mono.error(RuntimeException("Match not yet parsed"))
                }
            }
            .retryWhen(
                Retry.backoff(20, Duration.ofSeconds(30)) // 20 attempts, 30 seconds apart
                    .doBeforeRetry { retrySignal ->
                        val attempt = retrySignal.totalRetries() + 1
                        updateJobStatus(jobId, "WAITING_FOR_PARSE", "Attempt $attempt/20")
                    }
                    .onRetryExhaustedThrow { _, retryExhaustedSignal ->
                        ProcessingException("Match parsing timed out after 20 attempts (10 minutes)")
                    }
            )
    }


    private fun updateJobStatus(jobId: String, status: String, details: String? = null) {
        jobStatusRepository.updateStatus(jobId, status, details)
    }

    private fun completeJob(jobId: String, result: HookStats) {
        jobStatusRepository.updateStatus(jobId, "COMPLETED", null, result.accountId.toLong())
    }

    private fun failJob(jobId: String, error: String) {
        jobStatusRepository.updateStatus(jobId, "FAILED", error)
    }

    fun getJobStatus(jobId: String): JobStatus? {
        return jobStatusRepository.findById(jobId).get()
    }

    fun getResult(jobId: String): HookStatsResult? {
        val job = jobStatusRepository.findById(jobId).get()
        return if (job.status == "COMPLETED" && job.resultId != null) {
            val hookStat = hookStatRepository.findById(job.resultId!!).getOrNull()
            hookStat?.let { HookStatsResult.from(it) }
        } else null
    }


}