package net.dlcruz.sample

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest
@Import(SampleConfiguration::class)
class SampleControllerTest(@Autowired private val client: WebTestClient) {

    @MockkBean private lateinit var sampleRepository: SampleRepository

    @Test
    fun `test post sample`() {
        val sample = Sample("test data")
        val persistentSample = PersistentSample(0, sample.data)
        val response = PersistentSample(1, sample.data)

        every { sampleRepository.save(persistentSample) } returns response

        client.post().uri("/sample")
            .body(BodyInserters.fromObject(sample))
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.id").isEqualTo(response.id)
            .jsonPath("$.data").isEqualTo(response.data)
    }

    /*fun `test get sample`() {
        val sample = Sample("test data")
        val persistentSample = PersistentSample(0, sample.data)
        val response = PersistentSample(1, sample.data)
        every { sampleRepository. }
    }*/
}
