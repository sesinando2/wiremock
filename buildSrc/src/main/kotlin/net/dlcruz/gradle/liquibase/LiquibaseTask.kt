package net.dlcruz.gradle.liquibase

import org.gradle.api.tasks.JavaExec

open class LiquibaseTask: JavaExec() {

    init {
        group = "liquibase"
        classpath(project.configurations.getByName("liquibase"))
        main = "liquibase.integration.commandline.Main"
    }

    lateinit var config: LiquibaseExtension

    override fun exec() {
        setupLiquibaseArgs()
        logger.debug("Running liquibase with args: {}", args.joinToString(" "))
        super.exec()
    }

    protected open fun setupLiquibaseArgs() {
        args("--changeLogFile=${project.projectDir}/src/main/resources/${config.directory}/${config.changeLog}.${config.fileExtension}")
        args("--url=${config.url}")
        args("--username=${config.username}")
        args("--driver=${config.driver}")
        args("--logLevel=${config.logLevel}")

        config.password?.apply { args("--password=${this}") }

        when {
            project.hasProperty("rollbackCount") -> {
                args("rollbackCount")
                args("${project.properties["rollbackCount"]}")
            }

            project.hasProperty("update") -> args("update")
            project.hasProperty("generateChangeLog") -> args("generateChangeLog")
            project.hasProperty("status") -> args("status")
        }
    }
}