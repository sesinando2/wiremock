package net.dlcruz.sample

import net.dlcruz.config.FunctionalTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient

@FunctionalTest
class SampleControllerFunctionalTest(
        @Autowired private val webTestClient: WebTestClient
) {

    @Test
    fun `test post`() {
        assertThat(webTestClient).isNotNull
        assertThat(true).isTrue()
    }
}