package com.dota.tracker.opendota.model

data class ObsLeftLog(
    val attackername: String,
    val ehandle: Int,
    val entityleft: Boolean,
    val key: String,
    val player_slot: Int,
    val slot: Int,
    val time: Int,
    val type: String,
    val x: Double,
    val y: Double,
    val z: Double
)