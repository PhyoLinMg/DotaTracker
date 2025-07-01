package com.dota.tracker.pudge.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "player_hook_summary",
    indexes = [
        Index(name = "idx_success_rate", columnList = "successRate", unique = false),
        Index(name = "idx_account_id", columnList = "accountId", unique = true),
        Index(name = "idx_last_updated", columnList = "lastUpdated")
    ]
)
data class PlayerHookSummary(
    @Id
    val accountId: Int,

    @Column(nullable = false, length = 100)
    val playerName: String,

    @Column(nullable = false)
    val totalHits: Long,

    @Column(nullable = false)
    val totalCasts: Long,

    @Column(nullable = false, precision = 5)
    val successRate: Double,

    @Column(nullable = false)
    val lastUpdated: LocalDateTime,

    @Column(nullable = false)
    val matchCount: Int = 0
) {
    constructor() : this(0, "", 0, 0, 0.0, LocalDateTime.now(), 0)
}
