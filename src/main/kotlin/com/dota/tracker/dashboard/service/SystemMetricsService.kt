package com.dota.tracker.dashboard.service

import com.dota.tracker.dashboard.data.CpuMetrics
import com.dota.tracker.dashboard.data.MemoryMetrics

import org.springframework.stereotype.Service
import java.lang.management.ManagementFactory
import java.lang.management.OperatingSystemMXBean
import java.time.LocalDateTime

@Service
class SystemMetricsService {

    private val osBean: OperatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean()

    fun getCpuUsage(): CpuMetrics {
        val processCpuLoad = if (osBean is com.sun.management.OperatingSystemMXBean) {
            osBean.processCpuLoad * 100
        } else {
            0.0
        }

        val systemCpuLoad = if (osBean is com.sun.management.OperatingSystemMXBean) {
            osBean.cpuLoad * 100
        } else {
            0.0
        }

        return CpuMetrics(
            processCpuUsage = processCpuLoad,
            systemCpuUsage = systemCpuLoad,
            availableProcessors = osBean.availableProcessors,
            timestamp = LocalDateTime.now()
        )
    }

    fun getMemoryUsage(): MemoryMetrics {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory

        return MemoryMetrics(
            maxMemory = maxMemory,
            totalMemory = totalMemory,
            usedMemory = usedMemory,
            freeMemory = freeMemory,
            usagePercentage = (usedMemory.toDouble() / maxMemory * 100),
            timestamp = LocalDateTime.now()
        )
    }
}