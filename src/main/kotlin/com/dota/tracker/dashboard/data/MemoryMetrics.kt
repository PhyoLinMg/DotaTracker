package com.dota.tracker.dashboard.data

import java.time.LocalDateTime

data class MemoryMetrics(
    val maxMemory: Long,
    val totalMemory: Long,
    val usedMemory: Long,
    val freeMemory: Long,
    val usagePercentage: Double,
    val timestamp: LocalDateTime
)
