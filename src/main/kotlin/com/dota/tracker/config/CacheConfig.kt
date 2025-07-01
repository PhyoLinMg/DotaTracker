package com.dota.tracker.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(10_000) // Max 10,000 entries
                .expireAfterWrite(Duration.ofMinutes(30)) // Expire after 30 minutes
                .expireAfterAccess(Duration.ofMinutes(10)) // Expire if not accessed for 10 minutes
                .recordStats() // Enable statistics for monitoring
        )
        return cacheManager
    }
}