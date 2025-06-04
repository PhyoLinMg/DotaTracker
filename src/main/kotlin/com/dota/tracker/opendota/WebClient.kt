package com.dota.tracker.opendota

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class OpenDotaWebClient {
  @Bean
  fun buildClient(): WebClient = WebClient.builder().baseUrl("https://api.opendota.com/api/")
      .codecs { configurer ->
          configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
      }.build()
}