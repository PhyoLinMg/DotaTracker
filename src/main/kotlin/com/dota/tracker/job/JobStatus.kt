package com.dota.tracker.job

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "job_status")
data class JobStatus(
    @Id val jobId: String,
    var status: String, // PROCESSING, COMPLETED, FAILED
    var details: String? = null,
    var resultId: Long? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
){
    constructor(): this("","",null,null, Instant.now(), Instant.now())
}
