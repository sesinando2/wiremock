package net.dlcruz.sample

import net.dlcruz.config.FunctionalTest
import net.dlcruz.fixture.FixtureHelperFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@FunctionalTest
class SampleControllerFunctionalTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val fixtureHelperFactory: FixtureHelperFactory
) {

    @Test
    fun `test post`() {
        val sample = Sample("test data")
        webTestClient.post()
            .uri("/sample")
            .body(BodyInserters.fromObject(sample))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.data").isEqualTo(sample.data)
    }

    @Test
    fun `test get`() {
        val fixtureHelper = fixtureHelperFactory.newFixture()
            .withSample().block() ?: throw AssertionError("Unexpected response.")

        val existingSample = fixtureHelper.sampleResponse ?: throw AssertionError("Unexpected response.")

        webTestClient.get().uri("/sample/{id}", fixtureHelper.sampleResponse?.id)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(existingSample.id)
            .jsonPath("$.data").isEqualTo(existingSample.data)
    }
}