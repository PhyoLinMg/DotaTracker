package com.dota.tracker.job

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class JobsService(
    private val jobStatusRepository: JobStatusRepository
) {

    fun getAllJobs(pageable: Pageable): Page<JobStatusResult> {
        return jobStatusRepository.findAll(pageable).map { jobStatus ->
            JobStatusResult.from(jobStatus)
        }
    }

    fun retryFailed(jobId: String): ProcessingResponse {
        val jobStatus = jobStatusRepository.findById(jobId)
            .orElseThrow { IllegalArgumentException("Job with ID $jobId not found") }

        if (jobStatus.status != JobStatusEnum.FAILED) {
            throw IllegalStateException("Job with ID $jobId is not in FAILED state")
        }

        // Logic to retry the job
        // This could involve re-queuing the job or invoking the processing logic again
    

        // For demonstration, we will just update the status to RETRYING
        jobStatus.status = JobStatusEnum.RETRYING
        jobStatusRepository.save(jobStatus)

        return ProcessingResponse(jobId, JobStatusEnum.RETRYING.name)
    }
}