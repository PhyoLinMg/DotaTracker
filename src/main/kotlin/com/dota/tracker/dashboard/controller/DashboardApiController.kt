package com.dota.tracker.dashboard.controller

import com.dota.tracker.dashboard.service.SystemMetricsService
import com.dota.tracker.pudge.service.LeaderBoardService
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DashboardApiController(
    private val systemMetricsService: SystemMetricsService,
    private val cacheManager: CacheManager
) {

    @GetMapping("/metrics/cpu")
    fun getCpuMetrics() = systemMetricsService.getCpuUsage()

    @GetMapping("/metrics/memory")
    fun getMemoryMetrics() = systemMetricsService.getMemoryUsage()


    @GetMapping("/cache/stats")
    fun getCacheStats(): Map<String, Any> {
        val stats = mutableMapOf<String, Any>()

        cacheManager.cacheNames.forEach { cacheName ->
            val cache = cacheManager.getCache(cacheName)
            when (cache) {
                is CaffeineCache -> {
                    val caffeineStats = cache.nativeCache.stats()
                    val estimatedSize = cache.nativeCache.estimatedSize()

                    stats[cacheName] = mapOf(
                        "hitCount" to caffeineStats.hitCount(),
                        "missCount" to caffeineStats.missCount(),
                        "hitRate" to String.format("%.2f%%", caffeineStats.hitRate() * 100),
                        "evictionCount" to caffeineStats.evictionCount(),
                        "loadCount" to caffeineStats.loadCount(),
                        "averageLoadTime" to "${caffeineStats.averageLoadPenalty() / 1_000_000}ms",
                        "estimatedSize" to estimatedSize,
                        "requestCount" to caffeineStats.requestCount()
                    )
                }
                else -> {
                    stats[cacheName] = mapOf(
                        "error" to "Not a Caffeine cache or cache not found",
                        "cacheType" to cache?.javaClass?.simpleName
                    )
                }
            }
        }

        return stats
    }

    @GetMapping("/debug")
    fun debugCache(): Map<String, Any> {
        return mapOf(
            "cacheManagerType" to cacheManager.javaClass.simpleName,
            "availableCaches" to cacheManager.cacheNames,
            "leaderboardCacheExists" to (cacheManager.getCache("hookStatsLeaderboard") != null),
            "cacheDetails" to cacheManager.cacheNames.associateWith { cacheName ->
                val cache = cacheManager.getCache(cacheName)
                mapOf(
                    "type" to cache?.javaClass?.simpleName,
                    "nativeCacheType" to cache?.nativeCache?.javaClass?.simpleName
                )
            }
        )
    }
}