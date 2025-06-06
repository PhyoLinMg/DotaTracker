package com.dota.tracker.opendota


import com.dota.tracker.opendota.model.DotaMatch
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

import reactor.core.publisher.Mono

@Repository
class MatchRepository(private val client: WebClient) {
    fun getMatchDetail(matchId: String): Mono<DotaMatch>{
        return client.get().uri("matches/$matchId").retrieve().bodyToMono(DotaMatch::class.java)
    }

    fun requestToParse(matchId: String):Mono<Unit> {
        return client.post().uri("request/$matchId").retrieve().bodyToMono(Unit::class.java)
    }

}