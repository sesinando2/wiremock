package net.dlcruz.sample

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.util.*

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
        val createdData = PersistentSample(1, sample.data)

        every { repository.save(sample.toData()) } returns createdData

        val publisher = sampleService.create(sample)

        StepVerifier.create(publisher)
            .expectNextMatches(createdData::matches)
            .expectComplete()
            .verify()

        verify (exactly = 1) { repository.save(sample.toData()) }
    }

    @Test
    fun `should be able to get added data`() {
        val existingData = PersistentSample(1, "test data")
        every { repository.findById(existingData.id) } returns Optional.of(existingData)

        val publisher = sampleService.get(existingData.id)

        StepVerifier.create(publisher)
            .expectNextMatches(existingData::matches)
            .expectComplete()
            .verify()

        verify (exactly = 1) { repository.findById(existingData.id) }
    }

    @Test
    fun `should be able to delete existing data`() {
        val publisher = sampleService.delete(1)
        every { repository.deleteById(1) } returns Unit

        StepVerifier.create(publisher).expectNext(Unit).expectComplete().verify()

        verify(exactly = 1) { repository.deleteById(1) }
    }
}

private fun PersistentSample.matches(sample: SampleResponse) = this.id == sample.id && this.data == sample.data
