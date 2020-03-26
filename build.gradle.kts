import java.util.Date
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.60"
    jacoco
    java

    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

group = "net.alexbroadbent"
version = "0.1.0"

apply(plugin = "kotlin")
apply(plugin = "maven-publish")

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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("kotlin-dsl") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])

            artifact(sourcesJar)

            pom.withXml {
                asNode().apply {
                    appendNode("description", "https://github.com/AlexBroadbent/jackson-dsl")
                    appendNode("name", rootProject.name)
                    appendNode("url", "https://github.com/AlexBroadbent/jackson-dsl")

                    appendNode("licenses").appendNode("license").apply {

                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", "AlexBroadbent")
                        appendNode("name", "Alex Broadbent")
                    }
                    appendNode("scm").apply {
                        appendNode("url", "https://github.com/AlexBroadbent/jackson-dsl")
                    }
                }
            }
        }
    }
}

bintray {
    user = project.findProperty("bintrayUser").toString()
    key = project.findProperty("bintrayKey").toString()
    publish = true

    setPublications("kotlin-dsl")

    pkg.apply {
        repo = "Jackson-DSL"
        name = project.name
        userOrg = "AlexBroadbent"

        githubRepo = "https://github.com/AlexBroadbent/jackson-dsl"
        vcsUrl = "https://github.com/AlexBroadbent/jackson-dsl.git"
        description = "A type-safe builder wrapped around the Jackson JSON library"
        setLabels("kotlin", "jackson", "dsl", "extension")
        setLicenses("MIT")
        desc = description
        websiteUrl = "https://github.com/AlexBroadbent/jackson-dsl"
        issueTrackerUrl = "https://github.com/AlexBroadbent/jackson-dsl/issues"
        githubReleaseNotesFile = "README.md"

        version.apply {
            name = project.version.toString()
            desc = "https://github.com/AlexBroadbent/jackson-dsl"
            released = Date().toString()
            vcsTag = project.version.toString()
        }
    }
}