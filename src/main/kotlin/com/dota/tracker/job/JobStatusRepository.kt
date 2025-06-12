package com.dota.tracker.job

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface JobStatusRepository : JpaRepository<JobStatus, String> {
    @Modifying
    @Transactional
    @Query("UPDATE JobStatus j SET j.status = :status, j.details = :details, j.resultId = :resultId, j.updatedAt = :now WHERE j.jobId = :jobId")
    fun updateStatus(jobId: String, status: JobStatusEnum, details: String?, resultId: Long? = null, now: Instant = Instant.now())
}