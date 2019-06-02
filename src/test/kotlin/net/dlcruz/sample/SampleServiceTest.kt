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
        val sampleData = PersistentSample(0, "test data")
        val expectedResponse = PersistentSample(1, sampleData.data)
        every { repository.save(sampleData) } returns expectedResponse

        val publisher = sampleService.newData(sampleData.data)

        StepVerifier.create(publisher).expectNext(expectedResponse).expectComplete().verify()
        verify(exactly = 1) { repository.save(sampleData) }
    }

    @Test
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
    }
}
