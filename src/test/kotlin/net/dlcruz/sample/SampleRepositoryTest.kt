package net.dlcruz.sample

import net.dlcruz.config.TestConfiguration
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(TestConfiguration::class)
class SampleRepositoryTest(@Autowired private val sampleRepository: SampleRepository) {

    @Test
    fun `should be able to save`() {
        val sample = PersistentSample(0, "test data")
        val saved = sampleRepository.save(sample)

        assertThat(saved.id, notNullValue())
        assertThat(saved.data, `is`(sample.data))

        val updated = sampleRepository.save(sample.apply { data = "updated data" })

        assertThat(updated.id, `is`(saved.id))
        assertThat(updated.data, `is`("updated data"))
    }

    @Test
    fun `should be able to delete`() {
        val sample = sampleRepository.save(PersistentSample(0, "test data"))

        assertThat(sample.id, greaterThan(0L))

        sampleRepository.delete(sample)

        assertThat(sampleRepository.findById(sample.id).isPresent, `is`(false))
    }
}