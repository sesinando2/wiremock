package net.dlcruz.sample

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromObject
import java.util.*

@WebFluxTest
@Import(SampleConfiguration::class)
class TestSampleController(@Autowired private val client: WebTestClient) {

    @MockkBean private lateinit var sampleRepository: SampleRepository

    @Test
    fun `test post`() {
        val sample = Sample("test data")
        val generatedId = 1L
        val slot = slot<PersistentSample>()

        every { sampleRepository.save(capture(slot)) } answers { PersistentSample(generatedId, slot.captured.data) }

        client.post().uri("/sample")
            .body(fromObject(sample))
            .exchange()
            .expectStatus().isCreated
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.id").isEqualTo(generatedId)
            .jsonPath("$.data").isEqualTo(sample.data)
    }

    @Test
    fun `test get`() {
        val existingPersistentSample = PersistentSample(1, "test data")

        every { sampleRepository.findById(existingPersistentSample.id) } returns Optional.of(existingPersistentSample)

        client.get().uri("/sample/{id}", existingPersistentSample.id)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.id").isEqualTo(existingPersistentSample.id)
            .jsonPath("$.data").isEqualTo(existingPersistentSample.data)
    }

    @Test
    fun `test put`() {
        val sample = Sample("updated data")
        val existingId = 1L
        val slot = slot<PersistentSample>()

        every { sampleRepository.save(capture(slot)) } answers { PersistentSample(existingId, slot.captured.data) }

        client.put().uri("/sample/{id}", existingId)
            .body(fromObject(sample))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.id").isEqualTo(existingId)
            .jsonPath("$.data").isEqualTo(sample.data)
    }
}
