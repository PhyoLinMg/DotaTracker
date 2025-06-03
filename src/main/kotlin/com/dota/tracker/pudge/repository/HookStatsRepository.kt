package com.dota.tracker.pudge.repository

import com.dota.tracker.pudge.model.HookStats
import org.springframework.data.jpa.repository.JpaRepository

interface HookStatsRepository : JpaRepository<HookStats, Long>
