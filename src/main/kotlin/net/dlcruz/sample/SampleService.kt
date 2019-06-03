package net.dlcruz.sample

import net.dlcruz.functional.merge
import net.dlcruz.logging.LoggerDelegate
import reactor.core.publisher.toMono
import java.util.*

open class SampleService(private val repository: SampleRepository) {

    private val log by LoggerDelegate()

    fun create(sample: Sample) =
        sample.toMono()
            .doOnNext { log.debug("Creating sample: {}", sample) }
            .map(Sample::toData)
            .map { repository.save(it) }
            .map(PersistentSample::toDto)

    fun get(id: Long) =
        id.toMono()
            .doOnNext { log.debug("Getting sample with id: {}", it) }
            .map(repository::findById)
            .flatMap(Optional<PersistentSample>::merge)
            .map(PersistentSample::toDto)

    fun delete(id: Long) =
        id.toMono()
            .doOnNext { log.debug("Delete sample with id: {}", id) }
            .map(repository::deleteById)

    fun update(id: Long, sample: Sample) =
        id.toMono()
            .map(repository::findById)
            .flatMap(Optional<PersistentSample>::merge)
            .map { it.updateWith(sample) }
            .doOnNext { log.debug("Updating sample: {}", it) }
            .map { repository.save(it) }
            .map(PersistentSample::toDto)
}