package com.dota.tracker.opendota.model

data class PermanentBuff(
    val grant_time: Int,
    val permanent_buff: Int,
    val stack_count: Int
)