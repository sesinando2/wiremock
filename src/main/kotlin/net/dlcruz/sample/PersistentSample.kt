package net.dlcruz.sample

import javax.persistence.* // ktlint-disable no-wildcard-imports

@Entity
data class PersistentSample(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var data: String
) {
    constructor(data: String) : this(0, data)
}

fun PersistentSample.toDto() = this.let { SampleResponse(id, data) }

fun PersistentSample.updateWith(sample: Sample) = this.apply { data = sample.data }