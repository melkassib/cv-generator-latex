package com.melkassib.cvgenerator.awesomecv.domain

import com.melkassib.cvgenerator.altacv.domain.*
import com.melkassib.cvgenerator.altacv.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.cvgenerator.altacv.utils.firstColumn
import com.melkassib.cvgenerator.common.domain.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ResumeTest {

    @Test
    fun `create a resume photo`() {
        val photo1 = Photo(PhotoShape.CIRCLE, PhotoEdge.NO_EDGE, PhotoDirection.LEFT, "examples/profile")
        assertThat(photo1.shape, equalTo(PhotoShape.CIRCLE))
        assertThat(photo1.edge, equalTo(PhotoEdge.NO_EDGE))
        assertThat(photo1.direction, equalTo(PhotoDirection.LEFT))
        assertThat(photo1.path, equalTo("examples/profile"))

        val photo2 = Photo()
        assertThat(photo2.shape, equalTo(PhotoShape.RECTANGLE))
        assertThat(photo2.edge, equalTo(PhotoEdge.EDGE))
        assertThat(photo2.direction, equalTo(PhotoDirection.RIGHT))
        assertThat(photo2.path, emptyString())
    }

    @Test
    fun `create a resume config with default values`() {
        val config = AwesomeCVConfig()
        assertThat(config.colorTheme, equalTo(ColorTheme.RED))
        assertThat(config.isSectionHighlighted, equalTo(true))
        assertThat(config.headerSocialSeparator, equalTo("\\textbar"))
    }

    @Test
    fun `create a resume header with default values`() {
        val header = AwesomeCVHeader()
        assertThat(header.alignment, equalTo(HeaderAlignment.CENTER))
        assertThat(header.userInfo, nullValue())
        assertThat(header.photo, nullValue())
        assertThat(header.quote, emptyString())
    }

    @Test
    fun `create a resume header`() {
        val photo = Photo(PhotoShape.CIRCLE, PhotoEdge.NO_EDGE, PhotoDirection.LEFT, "examples/profile")
        val userInfo = UserInfo("John", "Dupont")
        val header = AwesomeCVHeader(HeaderAlignment.CENTER, userInfo, photo)

        assertThat(header.photo, equalTo(photo))
        assertThat(header.photo, equalTo(photo))
        assertThat(header.userInfo, equalTo(userInfo))
    }

    @ParameterizedTest
    @MethodSource("buildResumes")
    fun `create a resume`(resume: AwesomeCVResume) {
        val resumePhoto = resume.header.photo

        assertThat(resume.config, notNullValue(AwesomeCVConfig::class.java))
        assertThat(resume.config.colorTheme, equalTo(ColorTheme.PINK))
        assertThat(resume.config.isSectionHighlighted, equalTo(false))
        assertThat(resume.config.headerSocialSeparator, equalTo("\\cdotp"))

        assertThat(resume.header, notNullValue())
        assertThat(resume.header.quote, equalTo("Be the change that you want to see in the world."))
        assertThat(resume.header.photo, notNullValue(Photo::class.java))

        assertThat(resumePhoto, notNullValue())
        assertThat(resumePhoto?.edge, equalTo(PhotoEdge.NO_EDGE))
        assertThat(resumePhoto?.shape, equalTo(PhotoShape.RECTANGLE))
        assertThat(resumePhoto?.path, equalTo("profile"))
        assertThat(resumePhoto?.direction, equalTo(PhotoDirection.RIGHT))
        assertThat(resume.header.userInfo, notNullValue(UserInfo::class.java))

        val sections = resume.sections

        val actualSectionTitles = sections.map { it.title }
        val expectedSectionTitles = listOf("Summary", "Work Experience", "Honors & Awards")

        val numberOfIgnoredSections = sections.count { it.ignored }
        val numberOfSectionInFirstColumn = sections.count { it.position.column == 1 }
        val numberOfSectionInSecondColumn = sections.count { it.position.column == 2 }

        assertThat(sections, hasSize(3))
        assertThat(sections, everyItem(isA(Section::class.java)))
        assertThat(actualSectionTitles, equalTo(expectedSectionTitles))
        assertThat(numberOfIgnoredSections, equalTo(0))
        assertThat(numberOfSectionInFirstColumn, equalTo(3))
        assertThat(numberOfSectionInSecondColumn, equalTo(0))
    }

    @Test
    fun `create an empty resume`() {
        val myResume = awesomecv {}

        assertThat(myResume.config, notNullValue(AwesomeCVConfig::class.java))
        assertThat(myResume.config.colorTheme, equalTo(ColorTheme.RED))
        assertThat(myResume.config.isSectionHighlighted, equalTo(true))
        assertThat(myResume.config.headerSocialSeparator, equalTo("\\textbar"))

        assertThat(myResume.header, notNullValue())
        assertThat(myResume.header.alignment, equalTo(HeaderAlignment.CENTER))
        assertThat(myResume.header.photo, nullValue(Photo::class.java))
        assertThat(myResume.header.quote, emptyString())
        assertThat(myResume.header.userInfo, nullValue(UserInfo::class.java))
        assertThat(myResume.sections, emptyIterableOf(Section::class.java))

        assertThat(myResume.toString(), containsString("sections = []"))
    }

    companion object {
        @JvmStatic
        fun buildResumes(): Stream<AwesomeCVResume> = Stream.of(
            buildResumeWithoutDSL(),
            buildResumeWithDSL()
        )

        private fun buildResumeWithoutDSL(): AwesomeCVResume {
            val config = AwesomeCVConfig(ColorTheme.PINK, false, "\\cdotp")

            val header = AwesomeCVHeader(
                photo = Photo(PhotoShape.RECTANGLE, PhotoEdge.NO_EDGE, path = "profile"),
                quote = "Be the change that you want to see in the world.",
                userInfo = UserInfo(
                    firstName = "John",
                    lastName = "Dupont",
                    personalInfo = linkedSetOf(
                        Position("Developer \\enskip\\cdotp\\enskip Cloud Engineer"),
                        MailAddress("Address, Street, City"),
                        Phone("(+212) 000-000-000"),
                        Email("contact@email.com")
                    )
                )
            )

            val footer = AwesomeCVFooter("\\today", "John Dupont~~~·~~~Résumé", "\\thepage")

            val s1 = Section(
                "Summary",
                firstColumn(1),
                listOf(
                    Paragraph(
                        """
                        Nulla blandit sapien ligula, sit amet rutrum urna scelerisque a. Fusce malesuada eros erat, eget porttitor enim elementum a.
                        Mauris maximus metus massa, accumsan convallis ligula molestie ut. Sed nulla nibh, venenatis vitae bibendum malesuada, porta venenatis ex.
                        In hac habitasse platea dictumst.
                        """.trimIndent()
                    )
                )
            )

            val workEvent = Event.create("Software Engineer") {
                holder = "TechNova Solutions"
                location = "New York, USA"
                duration = eventDurationStr("Jan. 2022", "Present")
                description = listOf(
                    Item("Developed scalable microservices architecture for cloud applications."),
                    Item("Reduced system downtime by 40% by implementing automated monitoring and alerting."),
                    Item("Led a team of 5 engineers to build a real-time analytics dashboard using Kafka and Elasticsearch.")
                )
            }

            val s2 = Section("Work Experience", firstColumn(2), listOf(workEvent))

            val honorList1 = HonorList(
                "International Awards",
                listOf(
                    HonorItem("Winner", "Google Cloud Hackathon", "Online", "2022"),
                    HonorItem("Top 5 Finalist", "Microsoft AI Challenge", "Seattle, USA", "2021")
                )
            )

            val honorList2 = HonorList(
                "Domestic Awards",
                listOf(
                    HonorItem("1st Place", "National Coding Championship", "New York, USA", "2020")
                )
            )

            val s3 = Section("Honors & Awards", firstColumn(3), listOf(honorList1, honorList2))

            return AwesomeCVResume(config, header, footer, listOf(s1, s2, s3))
        }

        @Suppress("LongMethod")
        private fun buildResumeWithDSL(): AwesomeCVResume =
            awesomecv {
                config {
                    colorTheme = ColorTheme.PINK
                    isSectionHighlighted = false
                    headerSocialSeparator = "\\cdotp"
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
                            Email("contact@email.com")
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
                        }
                    }

                    section("Honors & Awards") {
                        contents {
                            honors("International Awards") {
                                honor("Winner", "Google Cloud Hackathon", "Online", "2022")
                                honor("Top 5 Finalist", "Microsoft AI Challenge", "Seattle, USA", "2021")
                            }

                            honors("Domestic Awards") {
                                honor("1st Place", "National Coding Championship", "New York, USA", "2020")
                            }
                        }
                    }
                }
            }
    }
}
