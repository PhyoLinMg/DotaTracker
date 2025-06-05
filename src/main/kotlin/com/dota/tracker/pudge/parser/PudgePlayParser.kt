package com.dota.tracker.pudge.parser

import skadistats.clarity.model.CombatLogEntry
import skadistats.clarity.processor.gameevents.OnCombatLogEntry

class PudgePlayParser {
    private val replayData = mutableMapOf<String, Any>()


    @OnCombatLogEntry
    fun onCombatLogEntry(combatLogEntry: CombatLogEntry){

    }


    fun getReplayData(): Map<String, Any> {
        return replayData.toMap()
    }
}