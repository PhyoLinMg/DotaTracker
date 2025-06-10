package com.dota.tracker.dashboard.data

import java.time.LocalDateTime

data class CpuMetrics(
    val processCpuUsage: Double,
    val systemCpuUsage: Double,
    val availableProcessors: Int,
    val timestamp: LocalDateTime
)