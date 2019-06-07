package net.dlcruz.springfox

import com.fasterxml.classmate.TypeResolver
import io.swagger.annotations.Api
import org.apache.commons.lang3.StringUtils.EMPTY
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.AlternateTypeRules
import springfox.documentation.schema.AlternateTypeRules.*
import springfox.documentation.schema.WildcardType
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiInfo.DEFAULT_CONTACT
import springfox.documentation.service.VendorExtension
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration(
        private val resolver: TypeResolver,
        private val buildProperties: BuildProperties) {

    @Bean
    fun api() = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api::class.java))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .alternateTypeRules(monoTypeRule())
            .alternateTypeRules(fluxTypeRule())

    private fun monoTypeRule() =
            RecursiveAlternateTypeRule(resolver, listOf(
                    newRule(resolver.resolve(Mono::class.java, WildcardType::class.java),
                            resolver.resolve(WildcardType::class.java)),

                    newRule(resolver.resolve(ResponseEntity::class.java, WildcardType::class.java),
                            resolver.resolve(WildcardType::class.java))))

    private fun fluxTypeRule() =
            RecursiveAlternateTypeRule(resolver, listOf(
                    newRule(resolver.resolve(Flux::class.java, WildcardType::class.java),
                            resolver.resolve(List::class.java, WildcardType::class.java)),

                    newRule(resolver.resolve(ResponseEntity::class.java, WildcardType::class.java),
                            resolver.resolve(WildcardType::class.java))))

    private fun apiInfo() = ApiInfo(
            "Springboot Template",
            "Template for Springboot Projects",
            buildProperties.version,
            EMPTY, DEFAULT_CONTACT, EMPTY, EMPTY, emptyList<VendorExtension<Any>>()
    )
}