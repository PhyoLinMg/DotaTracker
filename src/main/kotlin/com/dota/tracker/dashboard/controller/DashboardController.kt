package com.dota.tracker.dashboard.controller

import com.dota.tracker.job.JobsService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DashboardController(
    private val systemMetricsService: com.dota.tracker.dashboard.service.SystemMetricsService,
    private val jobsService: JobsService

) {
    @GetMapping("/")
    fun dashboard(model: Model): String {
        model.addAttribute("cpuMetrics", systemMetricsService.getCpuUsage())
        model.addAttribute("memoryMetrics", systemMetricsService.getMemoryUsage())

        return "dashboard"
    }

    @GetMapping("/job")
    fun jobs(): String{
        return "job"
    }

}