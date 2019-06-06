package net.dlcruz.config

import net.dlcruz.fixture.FixtureHelperFactory
import net.dlcruz.fixture.SampleHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class TestConfiguration {

    private val targetUrl: String
    get() = System.getProperty("targetUrl") ?: "http://localhost:8080"

    @Bean
    fun webClient() = WebClient.builder().baseUrl(targetUrl).build()

    @Bean
    fun webTestClient() = WebTestClient.bindToServer().baseUrl(targetUrl).build()

    @Bean
    fun sampleHelper(webClient: WebClient) = SampleHelper(webClient)

    @Bean
    fun fixtureHelperFactory(sampleHelper: SampleHelper) = FixtureHelperFactory(sampleHelper)
}