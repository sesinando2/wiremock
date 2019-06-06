package net.dlcruz.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient

@Configuration
class TestConfiguration {

    @Bean
    fun webTestClient() = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
}