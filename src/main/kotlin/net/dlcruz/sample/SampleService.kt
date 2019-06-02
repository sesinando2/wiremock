package net.dlcruz.sample

import net.dlcruz.functional.merge
import net.dlcruz.logging.LoggerDelegate
import reactor.core.publisher.toMono
import java.util.*

open class SampleService(private val repository: SampleRepository) {

    private val log by LoggerDelegate()

    fun newData(data: String) =
        data.toMono()
            .doOnNext { log.debug("Creating new data: {}", it) }
            .map { PersistentSample(0, it) }
            .map { repository.save(it) }

    fun get(id: Long) =
        id.toMono()
            .doOnNext { log.debug("Getting sample with id: {}", it) }
            .map(repository::findById)
            .flatMap(Optional<PersistentSample>::merge)

    fun delete(id: Long) =
        id.toMono()
            .doOnNext { log.debug("Delete sample with id: {}", id) }
            .map(repository::deleteById)
}