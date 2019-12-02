import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.60"
    jacoco
    java
}

apply(plugin = "kotlin")

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

jacoco {
    toolVersion = "0.8.5"
}

tasks.jacocoTestReport {
    reports {
        xml.apply {
            isEnabled = true
            destination = file("$buildDir/reports/jacoco/report.xml")
        }
        csv.isEnabled = false
        html.isEnabled = false
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
