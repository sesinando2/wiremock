package net.dlcruz.fixture

class FixtureHelperFactory(private val sampleHelper: SampleHelper) {

    fun newFixture() = FixtureHelper(sampleHelper)
}