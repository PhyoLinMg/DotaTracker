package com.dota.tracker.pudge.service

import com.dota.tracker.pudge.model.LeaderBoardResponse
import com.dota.tracker.pudge.model.PlayerHookSummary
import com.dota.tracker.pudge.repository.HookStatsRepository
import com.dota.tracker.pudge.repository.PlayerHookSummaryRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class LeaderBoardService(
    private val playerSummaryRepository: PlayerHookSummaryRepository,
    private val hookStatsRepository: HookStatsRepository,
    private val cacheManager: CacheManager
) {

    companion object {
        private const val MIN_CASTS_FOR_LEADERBOARD = 50L
        private const val LEADERBOARD_CACHE = "hookStatsLeaderboard"
    }

    // Primary leaderboard method - O(g log g) complexity
    @Cacheable(value = [LEADERBOARD_CACHE], key = "'top_' + #limit + '_' + #minCasts")
    fun getLeaderboard(limit: Int = 10, minCasts: Long = MIN_CASTS_FOR_LEADERBOARD): List<LeaderBoardResponse> {
        val pageable = PageRequest.of(0, limit)
        return playerSummaryRepository.getTopPlayers(minCasts, pageable)
            .map { summary -> summary.toLeaderboardResponse() }
    }

    // Paginated leaderboard for large datasets
    @Cacheable(value = [LEADERBOARD_CACHE], key = "'page_' + #page + '_' + #size + '_' + #minCasts")
    fun getLeaderboardPage(
        page: Int = 0,
        size: Int = 50,
        minCasts: Long = MIN_CASTS_FOR_LEADERBOARD
    ): Page<LeaderBoardResponse> {
        val pageable = PageRequest.of(page, size)
        return playerSummaryRepository.getLeaderboard(minCasts, pageable)
            .map { it.toLeaderboardResponse() }
    }

    // Player search functionality
    fun searchPlayers(name: String, limit: Int = 20): List<LeaderBoardResponse> {
        val pageable = PageRequest.of(0, limit)
        return playerSummaryRepository.findPlayersByName(name, pageable)
            .map { it.toLeaderboardResponse() }
    }

    // Get specific player with ranking
    fun getPlayerWithRanking(accountId: Int): PlayerWithRankingResponse? {
        val player = playerSummaryRepository.findById(accountId).orElse(null) ?: return null
        val ranking = playerSummaryRepository.getPlayerRanking(player.successRate, player.totalHits)

        return PlayerWithRankingResponse(
            player = player.toLeaderboardResponse(),
            ranking = ranking
        )
    }

    // Batch refresh for data consistency - O(n + g log g)
    @Async("taskExecutor")
    @Scheduled(fixedRate = 900000) // Every 15 minutes
    @CacheEvict(value = [LEADERBOARD_CACHE], allEntries = true)
    fun refreshAllPlayerSummaries() {
        val aggregates = hookStatsRepository.getAllPlayerAggregates()

        val summaries = aggregates.map { row ->
            val accountId = (row[0] as Number).toInt()
            val playerName = row[1] as String
            val matchCount = (row[2] as Number).toInt()
            val totalHits = (row[3] as Number).toLong()
            val totalCasts = (row[4] as Number).toLong()
            val successRate = if (totalCasts > 0) (totalHits * 100.0 / totalCasts) else 0.0

            PlayerHookSummary(
                accountId = accountId,
                playerName = playerName,
                totalHits = totalHits,
                totalCasts = totalCasts,
                successRate = successRate,
                lastUpdated = LocalDateTime.now(),
                matchCount = matchCount
            )
        }

        playerSummaryRepository.saveAll(summaries)
        println("Refreshed ${summaries.size} player summaries")
    }
}

// Extension function for clean mapping
private fun PlayerHookSummary.toLeaderboardResponse() = LeaderBoardResponse(
    id = accountId,
    player = playerName,
    totalHooks = totalCasts.toInt(),
    successRate = successRate.toInt(),
    matchCount = matchCount,
    lastUpdated = lastUpdated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
)

data class PlayerWithRankingResponse(
    val player: LeaderBoardResponse,
    val ranking: Long
)