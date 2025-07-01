package com.dota.tracker.pudge.model

data class LeaderBoardResponse(
    val id: Int,
    val player: String,
    val totalHooks: Int,
    val successRate: Int,
    val matchCount: Int,
    val lastUpdated: String
)