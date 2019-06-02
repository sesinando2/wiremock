package net.dlcruz.sample

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SampleConfiguration(private val sampleRepository: SampleRepository) {

    @Bean
    fun sampleService() = SampleService(sampleRepository)
}