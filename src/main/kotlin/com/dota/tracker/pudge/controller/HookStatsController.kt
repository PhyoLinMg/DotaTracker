package com.dota.tracker.pudge.controller

import com.dota.tracker.job.ProcessingResponse

import com.dota.tracker.pudge.service.HookStatsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hooks")
class HookStatsController(
    val hookStatsService: HookStatsService
) {

    /**
     * return that the request is done? - that is a question mark
     * First call, opendota /match API. If the match is not parsed,
     *  we call /request api to parse the match. and call /match API again.
     *  we will get the replay url and the total hook cast.
     *  from parser, will get the total number of pudge hits.
     *  and add it into the HookStat table.
     */
    @PostMapping("/process/{matchId}")
    suspend fun processHooks(@PathVariable matchId: String): ResponseEntity<ProcessingResponse> {
        val jobId = hookStatsService.submitMatchProcessing(matchId)
        return ResponseEntity.ok(ProcessingResponse(jobId, "SUBMITTED"))
    }



}
