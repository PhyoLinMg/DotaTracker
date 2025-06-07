package com.dota.tracker.pudge.utils

object Utils {
    fun isWithinTimeRange(
        timestamp1: Float,
        timestamp2: Float,
        thresholdSeconds: Float = 2.0f
    ): Boolean {
        return kotlin.math.abs(timestamp1 - timestamp2) <= thresholdSeconds
    }

    fun isHookDamageLowerThan100(damage:Int): Boolean = damage< 100


}