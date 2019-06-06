package net.dlcruz.fixture

import net.dlcruz.sample.Sample
import net.dlcruz.sample.SampleResponse
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

class SampleHelper(private val webClient: WebClient) {

    fun createSample(sample: Sample = Sample("Test Data")) = webClient.post().uri("/sample")
        .body(BodyInserters.fromObject(sample))
        .retrieve()
        .bodyToMono(SampleResponse::class.java)
}