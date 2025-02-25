package com.melkassib.cvgenerator.awesomecv.serialization

import com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import com.melkassib.cvgenerator.awesomecv.domain.*
import com.melkassib.cvgenerator.common.domain.AwesomeCVFooter
import com.melkassib.cvgenerator.common.domain.Divider
import com.melkassib.cvgenerator.common.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.cvgenerator.common.domain.Item
import com.melkassib.cvgenerator.common.domain.PhotoDirection
import com.networknt.schema.InputFormat
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test

class ResumeSerializationTest {

    private val jsonSchema: JsonSchema by lazy {
        val factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
        Files.newInputStream(Path.of("schemas/1.0.0/awesomecv.schema.json")).use { schemaStream ->
            factory.getSchema(schemaStream)
        }
    }

    @Test
    @Suppress("LongMethod")
    fun `serialize resume`() {
        val sampleResume =
            awesomecv {
                config {
                    colorTheme = ColorTheme.ORANGE
                }

                header {
                    photo = Photo(PhotoShape.RECTANGLE, PhotoEdge.NO_EDGE, path = "profile")
                    quote = "Be the change that you want to see in the world."

                    user {
                        firstName = "John"
                        lastName = "Dupont"
                        personalInfo = linkedSetOf(
                            Position("Developer \\enskip\\cdotp\\enskip Cloud Engineer"),
                            MailAddress("Address, Street, City"),
                            Phone("(+212) 000-000-000"),
                            Email("contact@email.com"),
                            HomePage("www.homepage.com"),
                            Github("your_id"),
                            LinkedIn("your_id"),
                            Gitlab("your_id"),
                            Twitter("@your_id"),
                            Skype("your_id"),
                            Reddit("your_id"),
                            Medium("your_id"),
                            StackOverFlow("SO-id", "SO-name"),
                            GoogleScholar("googlescholar-id", "name-to-display"),
                            ExtraInfo("extra information"),
                        )
                    }
                }

                footer {
                    left = "\\today"
                    center = "John Dupont~~~·~~~Résumé"
                    right = "\\thepage"
                }

                sections {
                    section("Summary") {
                        contents {
                            paragraph(
                                """
                                Nulla blandit sapien ligula, sit amet rutrum urna scelerisque a. Fusce malesuada eros erat, eget porttitor enim elementum a.
                                Mauris maximus metus massa, accumsan convallis ligula molestie ut. Sed nulla nibh, venenatis vitae bibendum malesuada, porta venenatis ex.
                                In hac habitasse platea dictumst.
                                """.trimIndent()
                            )
                        }
                    }

                    section("Work Experience") {
                        contents {
                            event("Software Engineer") {
                                holder = "TechNova Solutions"
                                location = "New York, USA"
                                duration = eventDurationStr("Jan. 2022", "Present")
                                description = listOf(
                                    Item("Developed scalable microservices architecture for cloud applications."),
                                    Item("Reduced system downtime by 40% by implementing automated monitoring and alerting."),
                                    Item("Led a team of 5 engineers to build a real-time analytics dashboard using Kafka and Elasticsearch.")
                                )
                            }

                            event("Backend Developer") {
                                holder = "CodeWave Inc."
                                location = "San Francisco, USA"
                                duration = eventDurationStr("Jun. 2019", "Dec. 2021")
                                description = listOf(
                                    Item("Designed RESTful APIs that handled over 1M requests per day."),
                                    Item("Optimized database queries, improving response times by 50%."),
                                    Item("Implemented CI/CD pipelines using Jenkins and Docker to streamline deployments.")
                                )
                            }
                        }
                    }

                    section("Honors & Awards") {
                        contents {
                            honors("International Awards") {
                                honor("Winner", "Google Cloud Hackathon", "Online", "2022")
                                honor("Top 5 Finalist", "Microsoft AI Challenge", "Seattle, USA", "2021")
                            }

                            content("\\medskip")
                            content(Divider)

                            honors("Domestic Awards") {
                                honor("1st Place", "National Coding Championship", "New York, USA", "2020")
                            }
                        }
                    }
                }
            }

        val resumeJson = sampleResume.toJson()

        val assertions = jsonSchema.validate(resumeJson, InputFormat.JSON)
        assertThat(assertions, hasSize(0))

        assertThat(resumeJson, hasJsonPath("$.config.colorTheme", equalTo("ORANGE")))
        assertThat(resumeJson, hasJsonPath("$.config.isSectionHighlighted", equalTo(true)))
        assertThat(resumeJson, hasJsonPath("$.config.headerSocialSeparator", equalTo("\\textbar")))
        assertThat(resumeJson, hasJsonPath("$.header.alignment", equalTo("CENTER")))
        assertThat(resumeJson, hasJsonPath("$.header.quote", equalTo("Be the change that you want to see in the world.")))
        assertThat(resumeJson, hasJsonPath("$.header.photo.shape", equalTo("RECTANGLE")))
        assertThat(resumeJson, hasJsonPath("$.header.photo.edge", equalTo("NO_EDGE")))
        assertThat(resumeJson, hasJsonPath("$.header.photo.direction", equalTo("RIGHT")))
        assertThat(resumeJson, hasJsonPath("$.header.photo.path", equalTo("profile")))
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.firstName", equalTo("John")))
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.lastName", equalTo("Dupont")))
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.personalInfo.length()", equalTo(15)))
        assertThat(resumeJson, hasJsonPath("$.footer.left", equalTo("\\today")))
        assertThat(resumeJson, hasJsonPath("$.footer.center", equalTo("John Dupont~~~·~~~Résumé")))
        assertThat(resumeJson, hasJsonPath("$.footer.right", equalTo("\\thepage")))
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(3)))
    }

    @Test
    fun `deserialize a resume`() {
        val resumeJson = File("src/test/resources/awesomecv/sample-resume.json").readText()
        val resume = buildAwesomeCVResumeFromJson(resumeJson)

        assertThat(resume.config.colorTheme, equalTo(ColorTheme.ORANGE))
        assertThat(resume.config.isSectionHighlighted, equalTo(true))
        assertThat(resume.config.headerSocialSeparator, equalTo("\\textbar"))

        assertThat(resume.header.alignment, equalTo(HeaderAlignment.CENTER))
        assertThat(resume.header.userInfo?.firstName, equalTo("John"))
        assertThat(resume.header.userInfo?.lastName, equalTo("Dupont"))
        assertThat(resume.header.userInfo?.personalInfo, hasSize(15))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Position("Developer \\enskip\\cdotp\\enskip Cloud Engineer")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(MailAddress("Address, Street, City")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Phone("(+212) 000-000-000")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Email("contact@email.com")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(HomePage("www.homepage.com")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Github("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(LinkedIn("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Gitlab("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Twitter("@your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Skype("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Reddit("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Medium("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(StackOverFlow("SO-id", "SO-name")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(GoogleScholar("googlescholar-id", "name-to-display")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(ExtraInfo("extra information")))

        assertThat(resume.header.photo?.shape, equalTo(PhotoShape.RECTANGLE))
        assertThat(resume.header.photo?.edge, equalTo(PhotoEdge.NO_EDGE))
        assertThat(resume.header.photo?.direction, equalTo(PhotoDirection.RIGHT))
        assertThat(resume.header.photo?.path, equalTo("./examples/profile"))

        assertThat(resume.footer, equalTo(AwesomeCVFooter("\\today", "John Dupont~~~·~~~Résumé", "\\thepage")))

        assertThat(resume.sections.size, equalTo(3))
    }
}
