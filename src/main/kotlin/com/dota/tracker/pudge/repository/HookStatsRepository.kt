package com.dota.tracker.pudge.repository

import com.dota.tracker.pudge.model.HookStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface HookStatsRepository : JpaRepository<HookStats, Long>{
    // Get aggregated data for a specific player - O(k) where k = player's matches
    @Query("""
        SELECT COUNT(h), SUM(h.hookHits), SUM(h.hookCasts)
        FROM HookStats h 
        WHERE h.accountId = :accountId
    """)
    fun getPlayerAggregates(accountId: Int): Array<Long>

    // Bulk calculation for summary refresh - O(n)
    @Query("""
        SELECT h.accountId, h.playerName, 
               COUNT(h), SUM(h.hookHits), SUM(h.hookCasts)
        FROM HookStats h 
        GROUP BY h.accountId, h.playerName
        HAVING SUM(h.hookCasts) > 0
    """)
    fun getAllPlayerAggregates(): List<Array<Any>>

    // Get recent matches for incremental updates
    @Query("""
        SELECT h FROM HookStats h 
        WHERE h.matchId > :lastProcessedMatchId 
        ORDER BY h.matchId ASC
    """)
    fun getNewMatches(lastProcessedMatchId: Long): List<HookStats>
}


