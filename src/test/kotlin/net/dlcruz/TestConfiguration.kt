package net.dlcruz

import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class TestConfiguration {

    @Bean
    fun buildProperties() = BuildProperties(Properties())

    @Bean
    fun gitProperties() = GitProperties(Properties())
}