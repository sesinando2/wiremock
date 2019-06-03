package net.dlcruz.sample

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class SampleServiceTest {

    private lateinit var repository: SampleRepository
    private lateinit var sampleService: SampleService

    @BeforeEach
    fun setUp() {
        repository = mockk()
        sampleService = SampleService(repository)
    }

    @Test
    fun `should create new data`() {
        val sample = Sample("test data")
        val generatedId = 1L
        val slot = slot<PersistentSample>()

        every { repository.save(capture(slot)) } answers { PersistentSample(generatedId, slot.captured.data) }

        val publisher = sampleService.create(sample)

        StepVerifier.create(publisher)
            .expectNextMatches { it.id == generatedId && it.data == sample.data }
            .expectComplete()
            .verify()

        assertThat(slot.captured).isEqualTo(PersistentSample(0, sample.data))
    }

    /*@Test
    fun `should be able to get added data`() {
        val sampleData = PersistentSample(0, "test data")
        val expectedResponse = PersistentSample(1, sampleData.data)
        every { repository.findById(1) } returns Optional.of(expectedResponse)

        val publisher = sampleService.get(1)

        StepVerifier.create(publisher).expectNext(expectedResponse).expectComplete().verify()
        verify(exactly = 1) { repository.findById(1) }
    }

    @Test
    fun `should be able to delete existing data`() {
        val publisher = sampleService.delete(1)
        every { repository.deleteById(1) } returns Unit

        StepVerifier.create(publisher).expectNext(Unit).expectComplete().verify()

        verify(exactly = 1) { repository.deleteById(1) }
    }*/
}
