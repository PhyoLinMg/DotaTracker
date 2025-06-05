package com.dota.tracker.pudge.controller

import com.dota.tracker.opendota.MatchService
import com.dota.tracker.pudge.service.PudgeParserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hooks")
class HookStatsController(
    val pudgeService: PudgeParserService,
    val matchService: MatchService
) {

    /**
     * return that the request is done? - that is a question mark
     * First call, opendota match API. If the match is not parsed,
     *  we call request api to parse the match. and call match API again.
     *  we will get the replay url and the total hook cast.
     *  from parser, will get the total number of pudge hits.
     *  and add it into the database.
     */
    @GetMapping("/add/{matchId}")
    fun addHook(@PathVariable matchId: String) {

    }

}
