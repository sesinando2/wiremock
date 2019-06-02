package net.dlcruz.sample

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/sample")
class SampleController(private val sampleService: SampleService) {

    @PostMapping
    fun post(@Valid @RequestBody sample: Sample) = sampleService
        .newData(sample.data)
        .map(PersistentSample::toSample)
        .map { ResponseEntity(it, HttpStatus.CREATED) }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = sampleService
        .get(id)
        .map(PersistentSample::toSample)
        .map  { ResponseEntity(it, HttpStatus.OK) }
}