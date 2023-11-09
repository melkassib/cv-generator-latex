package com.melkassib.altacv.gen.dsl.domain

import com.melkassib.altacv.gen.dsl.utils.PredefinedColorPalette
import com.melkassib.altacv.gen.dsl.utils.firstColumn
import com.melkassib.altacv.gen.dsl.utils.secondColumn
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        val userInfo = RUser("Your Name Here")
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
    fun `check predefined color palette`() {
        val p1 = PredefinedColorPalette.THEME1
        val p2 = PredefinedColorPalette.THEME2
        val p3 = PredefinedColorPalette.THEME3

        val actualColorAliases = RColorAlias.entries.toSet()
        val expectedColorAliases = setOf(p1, p2, p3).flatMap { it.keys }.toSet()

        assertThat(p1.size, equalTo(6))
        assertThat(p2.size, equalTo(6))
        assertThat(p3.size, equalTo(6))
        assertThat(actualColorAliases, equalTo(expectedColorAliases))
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
        assertThat(resume.header.userInfo, notNullValue(RUser::class.java))

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

    companion object {
        @JvmStatic
        fun buildResumes(): Stream<Arguments> = Stream.of(
            Arguments.of(buildResumeWithoutDSL()),
            Arguments.of(buildResumeWithDSL())
        )

        @Suppress("LongMethod")
        private fun buildResumeWithoutDSL(): Resume {
            val config = ResumeConfig(0.8, PhotoShape.CIRCLE, PredefinedColorPalette.THEME2)

            val header = ResumeHeader(
                "Your Position or Tagline Here",
                RUser(
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
            resume {
                with(it) {
                    config { cb ->
                        cb.columnRatio = 0.8
                        cb.photoShape = PhotoShape.CIRCLE
                        cb.theme = PredefinedColorPalette.THEME2
                    }

                    header { hb ->
                        hb.tagline = "Your Position or Tagline Here"
                        hb.photo = Photo(2.8, "Globe_High.png")
                        hb.userInfo = RUser(
                            "Your Name Here",
                            setOf(
                                EmailField("your_name@email.com"),
                                PhoneField("000-00-0000")
                            )
                        )
                    }

                    sections { slb ->
                        slb.section("My Life Philosophy", secondColumn(1)) { sb ->
                            sb.contents { scb ->
                                scb.quote("Something smart or heartfelt, preferably in one sentence.")
                            }
                        }

                        slb.section("Most Proud of", secondColumn(2), ignored = true) { sb ->
                            sb.contents { scb ->
                                scb.achievement(
                                    "faTrophy",
                                    "Fantastic Achievement",
                                    "and some details about it"
                                )
                                scb.achievement(
                                    "faHeartbeat",
                                    "Another achievement",
                                    "more details about it of course"
                                )
                                scb.achievement(
                                    "faHeartbeat",
                                    "Another achievement",
                                    "more details about it of course"
                                )
                            }
                        }

                        slb.section("Strengths", secondColumn(3)) { sb ->
                            sb.contents { scb ->
                                scb.tag("Hard-working")
                                scb.tag("Eye for detail")
                                scb.content(NewLine)
                                scb.tag("Motivator & Leader")
                                scb.content(Divider)
                                scb.tag("C++")
                                scb.tag("Embedded Systems")
                                scb.content(NewLine)
                                scb.tag("Statistical Analysis")
                            }
                        }

                        slb.section("Languages", secondColumn(4)) { sb ->
                            sb.contents { scb ->
                                scb.skill("Arabic", "Native/Bilingual")
                                scb.skill("English", "Professional working proficiency")
                                scb.skill("Spanish", "Limited working proficiency")
                                scb.skill("German", 2.0)
                            }
                        }

                        slb.section("A day of my life", firstColumn(3)) { sb ->
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
    }
}
