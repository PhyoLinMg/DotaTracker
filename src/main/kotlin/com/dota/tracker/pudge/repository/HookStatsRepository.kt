package com.dota.tracker.pudge.repository

import com.dota.tracker.pudge.model.HookStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HookStatsRepository : JpaRepository<HookStats, Long> {
    @Query("SELECT h FROM HookStats h ORDER BY (CASE WHEN h.hookCasts > 0 THEN (h.hookHits * 1.0 / h.hookCasts) ELSE 0 END) DESC LIMIT 10")
    fun findTop10ByHighestAccuracy(): List<HookStats>
}
