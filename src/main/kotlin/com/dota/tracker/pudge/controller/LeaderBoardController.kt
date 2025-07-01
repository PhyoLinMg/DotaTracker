package com.dota.tracker.pudge.controller

import com.dota.tracker.pudge.model.LeaderBoardResponse
import com.dota.tracker.pudge.service.LeaderBoardService
import com.dota.tracker.pudge.service.PlayerWithRankingResponse
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/leaderboard")
class LeaderboardController(
    private val leaderboardService: LeaderBoardService
) {

    // Get top players - Most common endpoint
    @GetMapping("/top")
    fun getTopPlayers(
        @RequestParam(defaultValue = "10") limit: Int,
        @RequestParam(defaultValue = "10") minCasts: Long
    ): ResponseEntity<List<LeaderBoardResponse>> {
        if (limit > 1000) {
            return ResponseEntity.badRequest().build()
        }

        val leaderboard = leaderboardService.getLeaderboard(limit, minCasts)
        return ResponseEntity.ok(leaderboard)
    }

    // Paginated leaderboard for frontend pagination
    @GetMapping
    fun getLeaderboard(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "10") minCasts: Long
    ): ResponseEntity<Page<LeaderBoardResponse>> {
        if (size > 100) {
            return ResponseEntity.badRequest().build()
        }

        val leaderboard = leaderboardService.getLeaderboardPage(page, size, minCasts)
        return ResponseEntity.ok(leaderboard)
    }

    // Search players by name
    @GetMapping("/search")
    fun searchPlayers(
        @RequestParam name: String,
        @RequestParam(defaultValue = "20") limit: Int
    ): ResponseEntity<List<LeaderBoardResponse>> {
        if (name.length < 2) {
            return ResponseEntity.badRequest().build()
        }

        val players = leaderboardService.searchPlayers(name, limit)
        return ResponseEntity.ok(players)
    }

    // Get specific player with ranking
    @GetMapping("/player/{accountId}")
    fun getPlayer(@PathVariable accountId: Int): ResponseEntity<PlayerWithRankingResponse> {
        val playerWithRanking = leaderboardService.getPlayerWithRanking(accountId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(playerWithRanking)
    }

    // Health check endpoint
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(mapOf(
            "status" to "healthy",
            "timestamp" to LocalDateTime.now(),
            "optimization" to "materialized_view_v2"
        ))
    }
}
