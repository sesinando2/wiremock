package net.dlcruz.gradle.liquibase

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import net.dlcruz.gradle.liquibase.DiffType.DB

class LiquibasePlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.create("liquibaseConfig", LiquibaseExtension::class.java)
            val liquibase = configurations.maybeCreate("liquibase")

            dependencies {
                liquibase("org.liquibase:liquibase-core")
                liquibase("org.liquibase.ext:liquibase-hibernate5:3.6")
                liquibase("org.springframework.boot:spring-boot-starter-data-jpa")
            }

            task("printConfig") {
                group = "liquibase"
                doLast {
                    println(extension)
                }
            }

            tasks.register("liquibase", LiquibaseTask::class.java) {
                config = extension
            }

            tasks.register("diffHibernate", LiquibaseDiffTask::class.java) {
                config = extension
            }

            tasks.register("diffDB", LiquibaseDiffTask::class.java) {
                config = extension
                type = DB
            }
        }
    }
}