package com.dota.tracker.opendota.model

data class MaxHeroHit(
    val inflictor: String?= null,
    val key: String,
    val max: Boolean,
    val player_slot: Int,
    val slot: Int,
    val time: Int,
    val type: String,
    val unit: String,
    val value: Int
)