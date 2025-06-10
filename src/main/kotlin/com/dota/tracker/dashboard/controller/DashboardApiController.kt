package com.dota.tracker.dashboard.controller

import com.dota.tracker.dashboard.service.SystemMetricsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DashboardApiController(
    private val systemMetricsService: SystemMetricsService,
) {

    @GetMapping("/metrics/cpu")
    fun getCpuMetrics() = systemMetricsService.getCpuUsage()

    @GetMapping("/metrics/memory")
    fun getMemoryMetrics() = systemMetricsService.getMemoryUsage()
}