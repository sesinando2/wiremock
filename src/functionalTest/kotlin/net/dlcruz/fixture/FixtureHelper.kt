package net.dlcruz.fixture

import net.dlcruz.sample.SampleResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.util.function.* // ktlint-disable no-wildcard-imports

class FixtureHelper(
    private val sampleHelper: SampleHelper
) {

    var sampleResponse: SampleResponse? = null
    private set

    fun withSample() = Mono
        .zip(this.toMono(), sampleHelper.createSample())
        .map { (fixture, response) -> fixture.apply { this.sampleResponse = response } }
}