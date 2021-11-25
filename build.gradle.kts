plugins {
    kotlin("jvm") version "1.6.0"
    id("org.jetbrains.dokka") version "1.6.0"
    jacoco
    `maven-publish`
    signing
}

group = "uk.co.alexbroadbent"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.0")

    testImplementation("io.kotest:kotest-runner-junit5:5.0.0")
}

jacoco {
    toolVersion = "0.8.7"
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    compileKotlin {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    compileTestKotlin {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.apply {
                required.set(true)
                destination = file("$buildDir/reports/jacoco/report.xml")
            }
            csv.required.set(false)
            html.required.set(true)
        }
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    artifacts {
        archives(kotlinSourcesJar)
        archives(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])

            pom {
                name.set(rootProject.name)
                description.set("https://github.com/AlexBroadbent/jackson-dsl")
                packaging = "jar"
                url.set("https://github.com/AlexBroadbent/jackson-dsl")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("AlexBroadbent")
                        name.set("Alexander Broadbent")
                        email.set("AlexBroadbent14@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:ssh://github.com/AlexBroadbent/jackson-dsl.git")
                    developerConnection.set("scm:git:ssh://github.com/AlexBroadbent/jackson-dsl.git")
                    url.set("https://github.com/AlexBroadbent/jackson-dsl")
                }
            }
        }
    }

    val NEXUS_USERNAME: String by project
    val NEXUS_PASSWORD: String by project
    repositories {
        maven {
            credentials {
                username = NEXUS_USERNAME
                password = NEXUS_PASSWORD
            }
            name = "jackson-dsl"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        }
    }

    signing {
        sign(publishing.publications)
    }
}
