package com.dota.tracker.pudge.model

import jakarta.persistence.*

@Entity
@Table(name = "hook_stats")
data class HookStats(
    @Id val accountId: Int,
    val matchId:Long,
    val playerName: String,
    val hookHits: Int,
    val hookCasts: Int
) {
    val accuracy: Double
        get() = if (hookCasts > 0) hookHits.toDouble() / hookCasts * 100 else 0.0

    constructor(): this(0,0,"",0,0)
}

data class HookStatsResult(
    val matchId: Long,
    val totalHooksCast: Int,
    val totalHooksHit: Int,
    val accuracy: Double,
){
    companion object{
        fun from(hookStats: HookStats) = HookStatsResult(
            hookStats.matchId,
            hookStats.hookCasts,
            hookStats.hookHits,
            hookStats.accuracy
        )
    }
}
