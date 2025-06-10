package com.dota.tracker.pudge.parser

import com.dota.tracker.opendota.model.DotaMatch
import com.dota.tracker.pudge.Pudge
import com.dota.tracker.pudge.model.HookStats
import com.dota.tracker.pudge.utils.Utils
import skadistats.clarity.model.CombatLogEntry
import skadistats.clarity.processor.gameevents.OnCombatLogEntry
import skadistats.clarity.wire.dota.common.proto.DOTAUserMessages
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PudgePlayParser(
    val dotaMatch: DotaMatch
) {
    private var totalHooksHit= 0

    private var tempTime= 0f
    private var hookCast= false

    @OnCombatLogEntry
    fun onCombatLogEntry(combatLogEntry: CombatLogEntry){
        when(combatLogEntry.type){
            DOTAUserMessages.DOTA_COMBATLOG_TYPES.DOTA_COMBATLOG_ABILITY->{
                if(combatLogEntry.attackerName== Pudge.heroName && combatLogEntry.inflictorName == Pudge.meatHook){
                    tempTime= combatLogEntry.timestamp
                    hookCast= true
                }
            }
            DOTAUserMessages.DOTA_COMBATLOG_TYPES.DOTA_COMBATLOG_DAMAGE->{
                checkHookTimeout(combatLogEntry.timestamp)
                val timestamp= combatLogEntry.timestamp
                if(hookCast && combatLogEntry.attackerName== Pudge.heroName &&
                    combatLogEntry.inflictorName == Pudge.meatHook &&
                    combatLogEntry.isTargetHero &&
                    !combatLogEntry.isTargetIllusion && Utils.isWithinTimeRange(
                        timestamp, tempTime, thresholdSeconds = 1f
                    )
                ){
                    totalHooksHit++
                    hookCast= false
                }
            }

            else -> {}
        }
    }
    private fun checkHookTimeout(currentTimestamp: Float) {
        if (hookCast && !Utils.isWithinTimeRange(currentTimestamp, tempTime)) {
            hookCast = false // Reset if hook didn't hit within timeframe
        }
    }



    fun getReplayData(): HookStats {
        val player= dotaMatch.players.find { player -> player.isPudge }
        return HookStats(
            accountId = player?.account_id ?: 0,
            matchId = dotaMatch.match_id,
            playerName = player?.personaname ?: "",
            hookHits = totalHooksHit,
            hookCasts=player?.ability_uses?.pudge_meat_hook?: 0
        )
    }
}