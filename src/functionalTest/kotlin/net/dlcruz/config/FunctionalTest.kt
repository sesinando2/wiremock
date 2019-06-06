package net.dlcruz.config

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(
        webEnvironment = NONE,
        classes = [
            TestConfiguration::class
        ]
)
annotation class FunctionalTest