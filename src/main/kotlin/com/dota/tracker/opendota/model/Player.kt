package com.dota.tracker.opendota.model

data class Player(
    val ability_uses: AbilityUses?,
    val account_id: Int?,
    val actions_per_min: Int?,
    val aghanims_scepter: Int?,
    val aghanims_shard: Int?,
    val courier_kills: Int?,
    val creeps_stacked: Int?,
    val duration: Int?,
    val game_mode: Int?,
    val lobby_type: Int?,
    val lose: Int?,
    val personaname: String?,
    val max_hero_hit: MaxHeroHit?,
){
    val hero = max_hero_hit?.unit?.convertHeroName()
    val isPudge = hero == "pudge"
}

fun String.convertHeroName(): String{
    return if (this.startsWith("npc_dota_hero_")) {
        this.removePrefix("npc_dota_hero_")
            .replace("_", " ")
    } else {
        this
    }
}