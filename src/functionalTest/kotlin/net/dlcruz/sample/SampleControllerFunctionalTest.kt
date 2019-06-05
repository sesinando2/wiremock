package net.dlcruz.sample

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

class SampleControllerFunctionalTest() {

    @Test
    fun `test post`() {
        assertThat(true).isTrue()
    }
}