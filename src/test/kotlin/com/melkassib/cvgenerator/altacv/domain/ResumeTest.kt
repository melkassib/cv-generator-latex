package com.melkassib.cvgenerator.altacv.domain

import com.melkassib.cvgenerator.altacv.utils.ColorPalette
import com.melkassib.cvgenerator.altacv.utils.PredefinedColorPalette
import com.melkassib.cvgenerator.altacv.utils.firstColumn
import com.melkassib.cvgenerator.altacv.utils.secondColumn
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ResumeTest {

    @Test
    fun `create a resume photo`() {
        val photo1 = Photo(2.8, "Globe_High.png")
        assertThat(photo1.size, equalTo(2.8))
        assertThat(photo1.path, equalTo("Globe_High.png"))
        assertThat(photo1.direction, equalTo(PhotoDirection.RIGHT))

        val photo2 = Photo(2.8, "Globe_High.png", PhotoDirection.LEFT)
        assertThat(photo2.direction, equalTo(PhotoDirection.LEFT))
    }

    @Test
    fun `create a resume config with default values`() {
        val config = ResumeConfig()
        assertThat(config.columnRatio, equalTo(0.6))
        assertThat(config.photoShape, equalTo(PhotoShape.NORMAL))
        assertThat(config.theme, equalTo(PredefinedColorPalette.THEME1))
    }

    @Test
    fun `create a resume header with default values`() {
        val header = ResumeHeader()
        assertThat(header.tagline, emptyString())
        assertThat(header.photo, nullValue())
        assertThat(header.userInfo, nullValue())
    }

    @Test
    fun `create a resume header`() {
        val userInfo = UserInfo("Your Name Here")
        val photo = Photo(2.8, "Globe_High.png")
        val header = ResumeHeader("Your Position or Tagline Here", userInfo, photo)

        assertThat(header.tagline, equalTo("Your Position or Tagline Here"))
        assertThat(header.photo, equalTo(photo))
        assertThat(header.userInfo, equalTo(userInfo))
    }

    @Test
    fun `create a resume with default values`() {
        val resume = Resume()
        assertThat(resume.config, notNullValue())
        assertThat(resume.config.columnRatio, equalTo(0.6))
        assertThat(resume.config.photoShape, equalTo(PhotoShape.NORMAL))
        assertThat(resume.config.theme, equalTo(PredefinedColorPalette.THEME1))

        assertThat(resume.header, notNullValue())
        assertThat(resume.header.tagline, emptyString())
        assertThat(resume.header.photo, nullValue())
        assertThat(resume.header.userInfo, nullValue())

        assertThat(resume.sections, emptyIterableOf(Section::class.java))
    }

    @Test
    fun `create a new theme`() {
        val theme: ColorPalette = buildMap {
            put(RColorAlias.TAGLINE, RColor("newColor1", "FFFDDD"))
            put(RColorAlias.HEADING_RULE, RColor("newColor2", "AFFDDD"))
            put(RColorAlias.HEADING, RColor("newColor3", "BFFDDD"))
            put(RColorAlias.ACCENT, RColor("newColor4", "CFFDDD"))
            put(RColorAlias.EMPHASIS, RColor("newColor5", "DFFDDD"))
            put(RColorAlias.BODY, RColor("newColor6", "EFFDDD"))
        }

        assertThat(theme.values, not(emptyIterableOf(RColor::class.java)))
        assertThat(theme.values, everyItem(hasProperty("colorName", not(emptyString()))))
        assertThat(theme.values, everyItem(hasProperty("colorHexValue", not(emptyString()))))
    }

    @ParameterizedTest
    @MethodSource("buildResumes")
    fun `create a resume`(resume: Resume) {
        val resumePhoto = resume.header.photo

        assertThat(resume.config, notNullValue(ResumeConfig::class.java))
        assertThat(resume.config.columnRatio, equalTo(0.8))
        assertThat(resume.config.photoShape, equalTo(PhotoShape.CIRCLE))
        assertThat(resume.config.theme, equalTo(PredefinedColorPalette.THEME2))

        assertThat(resume.header, notNullValue())
        assertThat(resume.header.tagline, equalTo("Your Position or Tagline Here"))
        assertThat(resume.header.photo, notNullValue(Photo::class.java))

        assertThat(resumePhoto, notNullValue())
        assertThat(resumePhoto?.size, equalTo(2.8))
        assertThat(resumePhoto?.path, equalTo("Globe_High.png"))
        assertThat(resumePhoto?.direction, equalTo(PhotoDirection.RIGHT))
        assertThat(resume.header.userInfo, notNullValue(UserInfo::class.java))

        val sections = resume.sections

        val actualSectionTitles = sections.map { it.title }
        val expectedSectionTitles = listOf(
            "My Life Philosophy",
            "Most Proud of",
            "Strengths",
            "Languages",
            "A day of my life"
        )

        val numberOfIgnoredSections = sections.count { it.ignored }
        val numberOfSectionInFirstColumn = sections.count { it.position.column == 1 }
        val numberOfSectionInSecondColumn = sections.count { it.position.column == 2 }

        assertThat(sections, hasSize(5))
        assertThat(sections, everyItem(isA(Section::class.java)))
        assertThat(actualSectionTitles, equalTo(expectedSectionTitles))
        assertThat(numberOfIgnoredSections, equalTo(1))
        assertThat(numberOfSectionInFirstColumn, equalTo(1))
        assertThat(numberOfSectionInSecondColumn, equalTo(4))
    }

    @Test
    fun `create an empty resume`() {
        val myResume = altacv {}

        assertThat(myResume.config, notNullValue(ResumeConfig::class.java))
        assertThat(myResume.config.columnRatio, equalTo(0.6))
        assertThat(myResume.config.photoShape, equalTo(PhotoShape.NORMAL))
        assertThat(myResume.config.theme, equalTo(PredefinedColorPalette.THEME1))

        assertThat(myResume.header, notNullValue())
        assertThat(myResume.header.tagline, emptyString())
        assertThat(myResume.header.photo, nullValue(Photo::class.java))
        assertThat(myResume.header.userInfo, nullValue(UserInfo::class.java))
        assertThat(myResume.sections, emptyIterableOf(Section::class.java))

        assertThat(myResume.toString(), containsString("sections = []"))
    }

    companion object {
        @JvmStatic
        fun buildResumes(): Stream<Resume> = Stream.of(
            buildResumeWithoutDSL(),
            buildResumeWithDSL()
        )

        @Suppress("LongMethod")
        private fun buildResumeWithoutDSL(): Resume {
            val config = ResumeConfig(0.8, PhotoShape.CIRCLE, PredefinedColorPalette.THEME2)

            val header = ResumeHeader(
                "Your Position or Tagline Here",
                UserInfo(
                    "Your Name Here",
                    setOf(
                        EmailField("your_name@email.com"),
                        PhoneField("000-00-0000")
                    )
                ),
                Photo(2.8, "Globe_High.png")
            )

            val s1 = Section(
                "My Life Philosophy",
                secondColumn(1),
                listOf(
                    Quote("Something smart or heartfelt, preferably in one sentence.")
                )
            )

            val s2 = Section(
                "Most Proud of",
                secondColumn(2),
                listOf(
                    Achievement("faTrophy", "Fantastic Achievement", "and some details about it"),
                    Achievement("faHeartbeat", "Another achievement", "more details about it of course"),
                    Achievement("faHeartbeat", "Another achievement", "more details about it of course")
                ),
                ignored = true
            )

            val s3 = Section(
                "Strengths",
                secondColumn(3),
                listOf(
                    Tag("Hard-working"),
                    Tag("Eye for detail"),
                    NewLine,
                    Tag("Motivator & Leader"),
                    Divider,
                    Tag("C++"),
                    Tag("Embedded Systems"),
                    NewLine,
                    Tag("Statistical Analysis")
                )
            )

            val s4 = Section(
                "Languages",
                secondColumn(4),
                listOf(
                    SkillStr("Arabic", "Native/Bilingual"),
                    SkillStr("English", "Professional working proficiency"),
                    SkillStr("Spanish", "Limited working proficiency"),
                    Skill("German", 2.0)
                )
            )

            val s5 = Section(
                "A day of my life",
                firstColumn(3),
                listOf(
                    WheelChart(
                        1.5,
                        0.5,
                        listOf(
                            WheelChartItem(6, 8, "accent!30", "Sleep,\\\\beautiful sleep"),
                            WheelChartItem(3, 8, "accent!40", "Hopeful novelist by night"),
                            WheelChartItem(8, 8, "accent!60", "Daytime job"),
                            WheelChartItem(2, 10, "accent", "Sports and relaxation"),
                            WheelChartItem(5, 8, "accent!20", "Spending time with family")
                        )
                    ),
                    LatexContent("\\newpage")
                )
            )

            return Resume(config, header, listOf(s1, s2, s3, s4, s5))
        }

        @Suppress("LongMethod")
        private fun buildResumeWithDSL(): Resume =
            altacv {
                config {
                    columnRatio = 0.8
                    photoShape = PhotoShape.CIRCLE
                    theme = PredefinedColorPalette.THEME2
                }

                header {
                    tagline = "Your Position or Tagline Here"
                    photo = Photo(2.8, "Globe_High.png")
                    userInfo = UserInfo(
                        "Your Name Here",
                        setOf(
                            EmailField("your_name@email.com"),
                            PhoneField("000-00-0000")
                        )
                    )
                }

                sections {
                    section("My Life Philosophy", secondColumn(1)) {
                        contents {
                            quote("Something smart or heartfelt, preferably in one sentence.")
                        }
                    }

                    section("Most Proud of", secondColumn(2), ignored = true) {
                        contents {
                            achievement(
                                "faTrophy",
                                "Fantastic Achievement",
                                "and some details about it"
                            )
                            achievement(
                                "faHeartbeat",
                                "Another achievement",
                                "more details about it of course"
                            )
                            achievement(
                                "faHeartbeat",
                                "Another achievement",
                                "more details about it of course"
                            )
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
                            skill("German", 2.0)
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
    }
}
