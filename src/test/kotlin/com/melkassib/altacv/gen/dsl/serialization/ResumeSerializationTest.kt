package com.melkassib.altacv.gen.dsl.serialization

import com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import com.melkassib.altacv.gen.dsl.domain.*
import com.melkassib.altacv.gen.dsl.domain.EventPeriodDate.Companion.eventDurationDate
import com.melkassib.altacv.gen.dsl.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.altacv.gen.dsl.utils.PredefinedColorPalette
import com.melkassib.altacv.gen.dsl.utils.firstColumn
import com.melkassib.altacv.gen.dsl.utils.secondColumn
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import java.io.File

class ResumeSerializationTest {
    @Test
    @Suppress("LongMethod")
    fun `serialize resume`() {
        val userPersonalInfo = setOf(
            EmailField("your_name@email.com"),
            PhoneField("000-00-0000"),
            MailAddressField("Address, Street, 00000 Country"),
            LocationField("Location, COUNTRY"),
            HomePageField("www.homepage.com"),
            TwitterField("@twitterhandle"),
            LinkedinField("your_id"),
            GithubField("your_id"),
            OrcidField("0000-0000-0000-0000"),
            UserInfoField("gitlab", "\\faGitlab", "https://gitlab.com/", "your_id")
        )

        val sampleResume =
            resume { rb ->
                rb.config {
                    it.photoShape = PhotoShape.NORMAL
                    it.theme = PredefinedColorPalette.THEME3
                }

                rb.header {
                    it.tagline = "Your Tagline Here"
                    it.photo = Photo(2.8, "Globe_High.png")
                    it.userInfo = RUser("Your Name Here", userPersonalInfo)
                }

                rb.sections {
                    with(it) {
                        section("Experience", firstColumn(1), Divider) { sb ->
                            sb.contents { scb ->
                                scb.event("Job Title 1") { ev ->
                                    ev.holder = "Company 1"
                                    ev.location = "Location"
                                    ev.duration = eventDurationStr("Month XXXX", "Ongoing")
                                    ev.description = listOf(
                                        Item("Job description 1"),
                                        Item("Job description 2"),
                                        Item("Job description 3", false)
                                    )
                                }
                                scb.event("Job Title 2") { ev ->
                                    ev.holder = "Company 2"
                                    ev.location = "Location"
                                    ev.duration = eventDurationDate("2023-10", "2023-10")
                                    ev.description = listOf(Item("Item1"))
                                }

                                scb.event("Job Title 3") { ev ->
                                    ev.holder = "Company 3"
                                    ev.location = "Location"
                                }
                            }
                        }

                        section("Most Proud of", secondColumn(2), ignored = true) { sb ->
                            sb.contents { scb ->
                                scb.achievement("faTrophy", "Fantastic Achievement", "and some details about it")
                                scb.achievement("faHeartbeat", "Another achievement", "more details about it of course")
                                scb.achievement("faHeartbeat", "Another achievement", "more details about it of course")
                            }
                        }

                        section("Strengths", secondColumn(3)) { sb ->
                            sb.contents { scb ->
                                with(scb) {
                                    tag("Hard-working")
                                    tag("Eye for detail")
                                    content(NewLine)

                                    tag("Motivator & Leader")
                                    content(Divider)

                                    tag("C++")
                                    tag("Embedded Systems")
                                    content(NewLine)

                                    tag("Statistical Analysis")
                                }
                            }
                        }

                        section("Languages", secondColumn(4)) { sb ->
                            sb.contents { scb ->
                                scb.skill("Arabic", "Native/Bilingual")
                                scb.skill("English", "Professional working proficiency")
                                scb.skill("Spanish", "Limited working proficiency")
                            }
                        }

                        section("A day of my life", firstColumn(3)) { sb ->
                            sb.contents { scb ->
                                scb.wheelchart(1.5, 0.5) { wc ->
                                    wc.item(6, 8, "accent!30", "Sleep,\\\\beautiful sleep")
                                    wc.item(3, 8, "accent!40", "Hopeful novelist by night")
                                    wc.item(8, 8, "accent!60", "Daytime job")
                                    wc.item(2, 10, "accent", "Sports and relaxation")
                                    wc.item(5, 8, "accent!20", "Spending time with family")
                                }
                                scb.content("\\newpage")
                            }
                        }
                    }
                }
            }

        val resumeJson = sampleResume.toJson()
        assertThat(resumeJson, hasJsonPath("$.config.columnRatio", equalTo(0.6)))
        assertThat(resumeJson, hasJsonPath("$.config.photoShape", equalTo("NORMAL")))
        assertThat(resumeJson, hasJsonPath("$.config.theme.length()", equalTo(6)))
        assertThat(resumeJson, hasJsonPath("$.header.tagline", equalTo("Your Tagline Here")))
        assertThat(resumeJson, hasJsonPath("$.header.photo.size", equalTo(2.8)))
        assertThat(resumeJson, hasJsonPath("$.header.photo.path", equalTo("Globe_High.png")))
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.name", equalTo("Your Name Here")))
        assertThat(resumeJson, hasJsonPath("$.header.userInfo.personalInfo.length()", equalTo(10)))
        assertThat(resumeJson, hasJsonPath("$.sections.length()", equalTo(5)))
    }

    @Test
    fun `deserialize a resume`() {
        val resumeJson = File("src/test/resources/sample-resume.json").readText()
        val resume = buildResumeFromJson(resumeJson)

        assertThat(resume.config.columnRatio, equalTo(0.6))
        assertThat(resume.config.photoShape, equalTo(PhotoShape.CIRCLE))

        assertThat(resume.config.theme[RColorAlias.TAGLINE], equalTo(RColor.PASTEL_RED))
        assertThat(resume.config.theme[RColorAlias.HEADING_RULE], equalTo(RColor.GOLDEN_EARTH))
        assertThat(resume.config.theme[RColorAlias.HEADING], equalTo(RColor.DARK_PASTEL_RED))
        assertThat(resume.config.theme[RColorAlias.ACCENT], equalTo(RColor.PASTEL_RED))
        assertThat(resume.config.theme[RColorAlias.EMPHASIS], equalTo(RColor.SLATE_GREY))
        assertThat(resume.config.theme[RColorAlias.BODY], equalTo(RColor.LIGHT_GREY))

        assertThat(resume.header.tagline, equalTo("Your Position or Tagline Here"))
        assertThat(resume.header.userInfo?.name, equalTo("Your Name Here"))
        assertThat(resume.header.userInfo?.personalInfo, hasSize(10))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(OrcidField("0000-0000-0000-0000")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(HomePageField("www.homepage.com")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(PhoneField("000-00-0000")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(TwitterField("@twitterhandle")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(EmailField("your_name@email.com")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(GithubField("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(LinkedinField("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(LocationField("Location, COUNTRY")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(MailAddressField("Address, Street, 00000 Country")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(UserInfoField("gitlab", "\\faGitlab", "https://gitlab.com/", "gitlab_id")))

        assertThat(resume.header.photo?.size, equalTo(2.8))
        assertThat(resume.header.photo?.path, equalTo("Globe_High.png"))
        assertThat(resume.header.photo?.direction, equalTo(PhotoDirection.RIGHT))

        val numberOfSectionsInFirstColumn = resume.sections.count { it.position.column == 1 }
        val numberOfSectionsInSecondColumn = resume.sections.count { it.position.column == 2 }

        assertThat(resume.sections.size, equalTo(8))
        assertThat(numberOfSectionsInFirstColumn, equalTo(3))
        assertThat(numberOfSectionsInSecondColumn, equalTo(5))
    }
}
