package com.dota.tracker.pudge.model

import jakarta.persistence.*

@Entity
@Table(name = "hook_stats")
data class HookStats(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val playerName: String,
    val hookHits: Int,
    val hookCasts: Int
) {
    val accuracy: Double
        get() = if (hookCasts > 0) hookHits.toDouble() / hookCasts * 100 else 0.0
}
