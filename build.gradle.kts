import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "2.1.0"
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.sonar)
    `java-library`
    jacoco
}

group = "com.melkassib"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(libs.jackson.module.kotlin)

    testImplementation(kotlin("test"))
    testImplementation(libs.hamcrest)
    testImplementation(libs.json.path.assert)
    testImplementation(libs.junit.jupiter.params)

    detektPlugins(libs.detekt.formatting)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Detekt>().configureEach {
    config.setFrom("detekt-config.yml")
    buildUponDefaultConfig = true
    parallel = true
    autoCorrect = true

    reports {
        html.required = true
        xml.required = true
        txt.required = false
        sarif.required = false
        md.required = false
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
}

dokka {
    dokkaSourceSets.main {
        includes.from("docs/AltaCVGeneratorModule.md")

        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://github.com/melkassib/cv-generator-latex/blob/main/src/main/kotlin")
            remoteLineSuffix.set("#L")
        }
    }

    pluginsConfiguration.html {
        footerMessage.set("(c) Mohcine EL KASSIB")
    }
}

sonar {
    properties {
        property("sonar.projectKey", "melkassib:altacv-generator-dsl")
        property("sonar.projectDescription", "Kotlin DSL for AltaCV Resume")
        property("sonar.organization", "melkassib")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
