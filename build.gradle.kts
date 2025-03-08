import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "2.1.0"
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.sonar)
    alias(libs.plugins.maven.publish)
    `java-library`
    jacoco
}

group = "com.melkassib"
version = "0.1.0"

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
    testImplementation(libs.json.schema.validator)

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
        property("sonar.projectKey", "melkassib:cv-generator-latex")
        property("sonar.projectDescription", "Kotlin DSL for AltaCV/AwesomeCV Résumés")
        property("sonar.organization", "melkassib")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

mavenPublishing {
    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaGenerate"),
            sourcesJar = true
        )
    )

    coordinates("com.melkassib", "cv-generator-latex", project.version.toString())

    pom {
        name = project.name
        description = "Kotlin DSL for AltaCV/AwesomeCV Résumés"
        url = "https://github.com/melkassib/cv-generator-latex/"
        inceptionYear = "2025"

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        developers {
            developer {
                id = "melkassib"
                name = "Mohcine EL KASSIB"
                email = "elkassib.mohcine@gmail.com"
                url = "com.melkassib"
            }
        }

        scm {
            connection = "scm:git:git://github.com/melkassib/cv-generator-latex.git"
            developerConnection = "scm:git:ssh://git@github.com/melkassib/cv-generator-latex.git"
            url = "https://github.com/melkassib/cv-generator-latex/"
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}
