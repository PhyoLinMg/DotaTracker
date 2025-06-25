package com.dota.tracker.pudge.repository

import com.dota.tracker.pudge.model.PlayerHookSummary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PlayerHookSummaryRepository : JpaRepository<PlayerHookSummary, Int> {

    // Primary leaderboard query - O(g log g) complexity
    @Query("""
        SELECT p FROM PlayerHookSummary p 
        WHERE p.totalCasts >= :minCasts 
        ORDER BY p.successRate DESC, p.totalHits DESC
    """)
    fun getLeaderboard(minCasts: Long, pageable: Pageable): Page<PlayerHookSummary>

    // Top N players - most common use case
    @Query("""
        SELECT p FROM PlayerHookSummary p 
        WHERE p.totalCasts >= :minCasts 
        ORDER BY p.successRate DESC, p.totalHits DESC
    """)
    fun getTopPlayers(minCasts: Long, pageable: Pageable): List<PlayerHookSummary>

    // Fast existence check
    fun existsByAccountId(accountId: Int): Boolean

    // Player search with LIKE - for search functionality
    @Query("""
        SELECT p FROM PlayerHookSummary p 
        WHERE LOWER(p.playerName) LIKE LOWER(CONCAT('%', :name, '%'))
        ORDER BY p.successRate DESC
    """)
    fun findPlayersByName(name: String, pageable: Pageable): List<PlayerHookSummary>

    // Get player ranking
    @Query("""
        SELECT COUNT(p) + 1 FROM PlayerHookSummary p 
        WHERE (p.successRate > :successRate) 
        OR (p.successRate = :successRate AND p.totalHits > :totalHits)
    """)
    fun getPlayerRanking(successRate: Double, totalHits: Long): Long


}
