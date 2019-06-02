package net.dlcruz.gradle.liquibase

import kotlin.reflect.full.memberProperties

open class LiquibaseExtension {
    var fileExtension: String = "yml"
    var directory: String = "db/changelog"
    var changeLog: String = "db.changelog-master"
    var url: String? = null
    var driver: String? = null
    var hibernateDialect: String? = null
    var username: String? = null
    var password: String? = null
    var jpaPackage: String? = null
    var logLevel: String = "info"

    override fun toString() = LiquibaseExtension::class.memberProperties.fold("", {
        text, property -> "$text\n${property.name}: ${property.get(this)}"
    })
}