package net.dlcruz.sample

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleControllerIntegrationTest(
        @Autowired private val client: WebTestClient,
        @Autowired private val sampleRepository: SampleRepository
) {

    @Test
    fun `test get`() {
        val persistentSample = sampleRepository.save(PersistentSample("test data"))

        client.get().uri("/sample/{id}", persistentSample.id)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(persistentSample.id)
                .jsonPath("$.data").isEqualTo(persistentSample.data)
    }
}