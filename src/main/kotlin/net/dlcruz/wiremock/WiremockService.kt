package net.dlcruz.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import net.dlcruz.logging.LoggerDelegate
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class WiremockService {

    private val logger by LoggerDelegate()
    private lateinit var wiremock: WireMockServer

    @PostConstruct
    fun init() {
        wiremock = WireMockServer()
        wiremock.start()
    }

    @PreDestroy
    fun cleanup() {
        wiremock.stop()
    }
}