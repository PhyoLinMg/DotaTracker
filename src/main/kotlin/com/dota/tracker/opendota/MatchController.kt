package com.dota.tracker.opendota

import com.dota.tracker.opendota.model.DotaMatch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/matches")
class MatchController(
    private val matchService: MatchService
) {

    @GetMapping("/{matchId}")
    fun getMatch(@PathVariable matchId: String): Mono<DotaMatch> = matchService.getMatchDetail(matchId)
}