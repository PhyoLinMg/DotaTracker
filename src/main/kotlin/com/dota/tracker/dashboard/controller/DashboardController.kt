package com.dota.tracker.dashboard.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DashboardController(
    private val systemMetricsService: com.dota.tracker.dashboard.service.SystemMetricsService,

) {
    @GetMapping("/")
    fun dashboard(model: Model): String {
        model.addAttribute("cpuMetrics", systemMetricsService.getCpuUsage())
        model.addAttribute("memoryMetrics", systemMetricsService.getMemoryUsage())

        return "dashboard"
    }
}