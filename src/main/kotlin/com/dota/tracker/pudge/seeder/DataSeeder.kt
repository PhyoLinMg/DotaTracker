package com.dota.tracker.pudge.seeder

import com.dota.tracker.pudge.model.HookStats
import com.dota.tracker.pudge.repository.HookStatsRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val hookStatsRepository: HookStatsRepository
): CommandLineRunner {
    override fun run(vararg args: String?) {
        if(hookStatsRepository.count()==0L){
            seedHookStatsData()
        }
    }

    private fun seedHookStatsData(){
        val players = listOf(
            Fourth(1001,"HookMaster", 3, 20),
            Fourth(1002,"PrecisionPlayer", 8, 20),
            Fourth(1003,"SniperShot", 14, 24),
            Fourth(1004,"AccuracyAce", 18, 40),
            Fourth(1002,"PrecisionPlayer", 12, 29),
            Fourth(1003,"QuickDraw", 8, 19),
            Fourth(1002,"PrecisionPlayer", 14, 37),
            Fourth(1002,"PrecisionPlayer", 4, 8)
        )

        val hookStats = players.mapIndexed { index, ( accountId,name, hits, casts) ->
            HookStats(
                matchId = (index + 1).toLong(),
                accountId = accountId,
                playerName = name,
                hookHits = hits,
                hookCasts = casts
            )
        }

        hookStatsRepository.saveAll(hookStats)
        println("Seeded ${hookStats.size} players with hook statistics")
    }

}

data class Fourth(
    val accountId: Int,
    val name:String,
    val hits:Int,
    val casts:Int,
)