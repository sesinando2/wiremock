package net.dlcruz.gradle.liquibase

import net.dlcruz.gradle.liquibase.DiffType.DB
import net.dlcruz.gradle.liquibase.DiffType.HIBERNATE
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class LiquibaseDiffTask: LiquibaseTask() {

    var type = HIBERNATE

    override fun setupLiquibaseArgs() {
        val reason = project.properties["reason"]?.toString() ?: "put-reason-here"
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"))
        args("--changeLogFile=${project.projectDir}/src/main/resources/${config.directory}/migrations/$date-$reason.${config.fileExtension}")
        args("--logLevel=${config.logLevel}")
        args("--referenceDriver=liquibase.ext.hibernate.database.connection.HibernateDriver")

        when(type) {
            HIBERNATE -> {
                args("--url=${config.url}")
                args("--username=${config.username}")
                args("--driver=${config.driver}")
                args("--referenceUrl=hibernate:spring:${config.jpaPackage}?dialect=${config.hibernateDialect}&hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy&hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy")
            }
            DB -> {
                args("--referenceUrl=${config.url}")
                args("--referenceUsername=${config.username}")
                args("--referenceDriver=${config.driver}")
                args("--url=hibernate:spring:${config.jpaPackage}?dialect=${config.hibernateDialect}&hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy&hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy")
            }
        }

        args("diffChangeLog")
    }
}

enum class DiffType { DB, HIBERNATE }