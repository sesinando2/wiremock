import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    kotlin("kapt") version "1.2.71"
    id("com.gorylenko.gradle-git-properties") version "2.0.0"
}

var ktlint = configurations.maybeCreate("ktlint")

java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly: Configuration by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.tomakehurst:wiremock-jre8:2.23.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.ninja-squad:springmockk:1.1.2")
    testImplementation("com.h2database:h2:1.4.199")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    ktlint("com.github.shyiko:ktlint:0.31.0")
}

sourceSets {
    create("functionalTest") {
        compileClasspath += sourceSets.main.get().output
        compileClasspath += sourceSets.main.get().compileClasspath
        compileClasspath += sourceSets.test.get().compileClasspath
        runtimeClasspath += sourceSets.test.get().runtimeClasspath
    }
}

val functionalTest = task<Test>("functionalTest") {
    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
    project.properties["targetUrl"]?.apply { systemProperty("targetUrl", this) }
}

val ktlintTask = task<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    main = "com.github.shyiko.ktlint.Main"
    classpath = ktlint
    args("src/**/*.kt")
    // to generate report in checkstyle format prepend following args:
    // "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
    // see https://github.com/shyiko/ktlint#usage for more
}

val ktlintFormat = task<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    main = "com.github.shyiko.ktlint.Main"
    classpath = ktlint
    args("-F", "src/**/*.kt")
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

tasks.check { dependsOn(ktlintTask) }