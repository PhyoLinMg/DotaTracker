package com.dota.tracker.job

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class JobsService(
    private val jobStatusRepository: JobStatusRepository
) {

    fun getAllJobs(pageable: Pageable): Page<JobStatusResult> {
        return jobStatusRepository.findAll(pageable).map { jobStatus ->
            JobStatusResult.from(jobStatus)
        }
    }
}