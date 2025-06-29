package com.dota.tracker.job

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/jobs")
class JobController(
    private val service: JobsService
) {

    @GetMapping
    fun getJobs(@RequestParam(required = false) page: Int = 0): ResponseEntity<Page<JobStatusResult>>{
        val pageable= PageRequest.of(page, 10, Sort.by("createdAt").descending())
        val jobs= service.getAllJobs(pageable)
        return ResponseEntity( jobs, HttpStatus.OK)
    }

    @GetMapping("/retry")
    fun retryJob(@RequestParam jobId: String): ResponseEntity<String> {
        return try {
            service.retryFailed(jobId)
            ResponseEntity.ok("Job $jobId retried successfully")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retry job $jobId: ${e.message}")
        }
    }
}