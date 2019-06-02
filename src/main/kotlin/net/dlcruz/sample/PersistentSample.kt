package net.dlcruz.sample

import javax.persistence.*

@Entity
data class PersistentSample(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var data: String
)

fun PersistentSample.toSample() = this.let { SampleResponse(id, data) }