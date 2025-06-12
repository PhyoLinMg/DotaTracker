package com.dota.tracker.job

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "job_status")
data class JobStatus(
    @Id val jobId: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    var status: JobStatusEnum, // PROCESSING, COMPLETED, FAILED
    var details: String? = null,
    var resultId: Long? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
){
    constructor(): this("", JobStatusEnum.SUBMITTED,null,null, Instant.now(), Instant.now())
}

enum class JobStatusEnum {
    SUBMITTED,
    WAITING_FOR_PARSE,
    FETCHING_MATCH_DATA,
    REQUESTING_PARSE,
    SAVING_RESULT,
    CALCULATING,
    COMPLETED,
    FAILED
}

data class JobStatusResult(
    val jobId: String,
    val status: String,
    val details: String? = null,
    val resultId: Long? = null,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun from(jobStatus: JobStatus) = JobStatusResult(
            jobStatus.jobId,
            jobStatus.status.name,
            jobStatus.details,
            jobStatus.resultId,
            jobStatus.createdAt,
            jobStatus.updatedAt
        )
    }
}
