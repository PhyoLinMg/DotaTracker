package com.dota.tracker.opendota.model

data class DotaMatch(
    val duration: Int,
    val loss: Int,
    val match_id: Long,
    val patch: Int,
    val players: List<Player>,
    val pre_game_duration: Int,
    val radiant_win: Boolean,
    val region: Int,
    val replay_salt: Int,
    val replay_url: String,
    val `throw`: Int,
    val version: Int,
    val od_data: OdData
)