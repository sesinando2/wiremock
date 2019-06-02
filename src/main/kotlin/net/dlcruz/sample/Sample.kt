package net.dlcruz.sample

import javax.validation.constraints.NotNull

open class Sample(@NotNull val data: String)

class SampleResponse(val id: Long, data: String): Sample(data)