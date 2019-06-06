import net.dlcruz.gradle.liquibase.LiquibaseExtension
import net.dlcruz.gradle.liquibase.LiquibasePlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.yaml.snakeyaml.Yaml

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("org.yaml:snakeyaml:1.23")
    }
}

plugins {
    kotlin("plugin.jpa") version "1.2.71"
    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
    id("com.gorylenko.gradle-git-properties") version "1.5.1"
}

/* Start Liquibase Config */
val propertiesFile = File("$projectDir/src/main/resources/application.yml").inputStream()
val applicationProperties: Map<String, Any> = Yaml().load(propertiesFile) ?: throw IllegalArgumentException()
val springProperties = applicationProperties["spring"] as Map<String, Any>
val datasource = springProperties["datasource"] as Map<String, Any>

apply<LiquibasePlugin>()

configure<LiquibaseExtension> {
    url = "${datasource["url"]}"
    driver = "${datasource["driver-class-name"]}"
    username = "${datasource["username"]}"
    password = datasource["password"]?.toString()
    hibernateDialect = "org.hibernate.dialect.MySQL5Dialect"
    jpaPackage = "net.dlcruz"
    logLevel = "debug"
    fileExtension = "yml"
}
/* End Liquibase Config */

java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

val functionalTestImplementation: Configuration by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
}

val functionTestRuntimeOnly: Configuration by configurations.creating {
    extendsFrom(configurations.testRuntimeOnly.get())
}

val liquibase = configurations.maybeCreate("liquibase")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.apache.commons:commons-lang3:3.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.ninja-squad:springmockk:1.1.2")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.h2database:h2:1.4.199")

    liquibase(project.sourceSets.getByName("main").runtimeClasspath)
}

sourceSets {
    create("functionalTest") {
        compileClasspath += sourceSets.main.get().output
        compileClasspath += sourceSets.test.get().output

        runtimeClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.test.get().output
        runtimeClasspath += functionTestRuntimeOnly
    }
}

val functionalTest = task<Test>("functionalTest") {
    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
    project.properties["targetUrl"]?.apply { systemProperty("targetUrl", this) }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    buildInfo()
}