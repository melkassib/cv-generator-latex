package com.melkassib.cvgenerator.altacv.serialization

import com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import com.melkassib.cvgenerator.altacv.domain.*
import com.melkassib.cvgenerator.altacv.utils.PredefinedColorPalette
import com.melkassib.cvgenerator.common.domain.Divider
import com.melkassib.cvgenerator.common.domain.EventPeriodDate.Companion.eventDurationDate
import com.melkassib.cvgenerator.common.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.cvgenerator.common.domain.Item
import com.melkassib.cvgenerator.common.domain.NewLine
import com.melkassib.cvgenerator.common.domain.PhotoDirection
import com.melkassib.cvgenerator.common.utils.firstColumn
import com.melkassib.cvgenerator.common.utils.secondColumn
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class ResumeSerializationTest {

    @Test
    @Suppress("LongMethod")
    fun `serialize resume`() {
        val userPersonalInfo = setOf(
            Email("your_name@email.com"),
            Phone("000-00-0000"),
            MailAddress("Address, Street, 00000 Country"),
            Location("Location, COUNTRY"),
            HomePage("www.homepage.com"),
            Twitter("@twitterhandle"),
            LinkedIn("your_id"),
            Github("your_id"),
            Orcid("0000-0000-0000-0000"),
            UserInfoField("gitlab", "\\faGitlab", "https://gitlab.com/", "your_id")
        )

        val sampleResume =
            altacv {
                config {
                    photoShape = PhotoShape.NORMAL
                    theme = PredefinedColorPalette.THEME3
                }

                header {
                    tagline = "Your Tagline Here"
                    photo = Photo(2.8, "Globe_High.png")
                    userInfo = AltaCVUserInfo("Your Name Here", userPersonalInfo)
                }

                sections {
                    section("Experience", firstColumn(1), Divider) {
                        contents {
                            event("Job Title 1") {
                                holder = "Company 1"
                                location = "Location"
                                duration = eventDurationStr("Month XXXX", "Ongoing")
                                description = listOf(
                                    Item("Job description 1"),
                                    Item("Job description 2"),
                                    Item("Job description 3", false)
                                )
                            }

                            event("Job Title 2") {
                                holder = "Company 2"
                                location = "Location"
                                duration = eventDurationDate("2023-10", "2023-10")
                                description = listOf(Item("Item1"))
                            }

                            event("Job Title 3") {
                                holder = "Company 3"
                                location = "Location"
                            }
                        }
                    }

                    section("Most Proud of", secondColumn(2), ignored = true) {
                        contents {
                            achievement("faTrophy", "Fantastic Achievement", "and some details about it")
                            achievement("faHeartbeat", "Another achievement", "more details about it of course")
                            achievement("faHeartbeat", "Another achievement", "more details about it of course")
                        }
                    }

                    section("Strengths", secondColumn(3)) {
                        contents {
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

                    section("Languages", secondColumn(4)) {
                        contents {
                            skill("Arabic", "Native/Bilingual")
                            skill("English", "Professional working proficiency")
                            skill("Spanish", "Limited working proficiency")
                        }
                    }

                    section("A day of my life", firstColumn(3)) {
                        contents {
                            wheelchart(1.5, 0.5) {
                                item(6, 8, "accent!30", "Sleep,\\\\beautiful sleep")
                                item(3, 8, "accent!40", "Hopeful novelist by night")
                                item(8, 8, "accent!60", "Daytime job")
                                item(2, 10, "accent", "Sports and relaxation")
                                item(5, 8, "accent!20", "Spending time with family")
                            }
                            content("\\newpage")
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
        val resumeJson = this.javaClass.getResource("/altacv/sample-resume.json")?.readText() ?: ""
        val resume = buildAltaCVResumeFromJson(resumeJson)

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
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Orcid("0000-0000-0000-0000")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(HomePage("www.homepage.com")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Phone("000-00-0000")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Twitter("@twitterhandle")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Email("your_name@email.com")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Github("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(LinkedIn("your_id")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(Location("Location, COUNTRY")))
        assertThat(resume.header.userInfo?.personalInfo, hasItem(MailAddress("Address, Street, 00000 Country")))
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
