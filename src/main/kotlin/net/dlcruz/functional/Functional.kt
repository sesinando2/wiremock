package net.dlcruz.functional

import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.*

fun <T : Any> Optional<T>.merge(): Mono<T> = this.map { it.toMono() }.orElse(Mono.empty())