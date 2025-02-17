package com.melkassib.cvgenerator.altacv.domain

import com.melkassib.cvgenerator.altacv.domain.EventPeriodDate.Companion.eventDurationDate
import com.melkassib.cvgenerator.altacv.domain.EventPeriodString.Companion.eventDurationStr
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class SectionContentTest {

    @Test
    fun `create an item`() {
        val item1 = Item("Test1", false)
        val item2 = Item("Test2")

        val elements = listOf(item1, item2)
        assertThat(elements, everyItem(instanceOf(SectionContent::class.java)))
        assertThat(elements, everyItem(not(instanceOf(HasSimpleContent::class.java))))

        assertThat(item1.type, equalTo(ContentType.ITEM))
        assertThat(item1.description, equalTo("Test1"))
        assertThat(item1.withBullet, equalTo(false))
        assertThat(item1.render(), equalTo("\\item[] Test1"))

        assertThat(item2.type, equalTo(ContentType.ITEM))
        assertThat(item2.description, equalTo("Test2"))
        assertThat(item2.withBullet, equalTo(true))
        assertThat(item2.render(), equalTo("\\item Test2"))
    }

    @Test
    fun `create an achievement`() {
        val achievement = Achievement("faTrophy", "Fantastic", "some details")
        assertThat(achievement, instanceOf(SectionContent::class.java))
        assertThat(achievement, not(instanceOf(HasSimpleContent::class.java)))

        assertThat(achievement.type, equalTo(ContentType.ACHIEVEMENT))
        assertThat(achievement.iconName, equalTo("faTrophy"))
        assertThat(achievement.achievement, equalTo("Fantastic"))
        assertThat(achievement.detail, equalTo("some details"))

        assertThat(achievement.render(), equalTo("\\cvachievement{\\faTrophy}{Fantastic}{some details}"))
    }

    @Test
    fun `create a skill`() {
        val skill1 = Skill("Programming", 5.0)
        val skill2 = SkillStr("Language", "Fluent")

        val elements = listOf(skill1, skill2)
        assertThat(elements, everyItem(instanceOf(SectionContent::class.java)))
        assertThat(elements, everyItem(not(instanceOf(HasSimpleContent::class.java))))
        assertThat(elements, everyItem(hasProperty("type", equalTo(ContentType.SKILL))))

        assertThat(skill1.skill, equalTo("Programming"))
        assertThat(skill1.rating, equalTo(5.0))
        assertThat(skill2.skill, equalTo("Language"))
        assertThat(skill2.fluency, equalTo("Fluent"))

        assertThat(skill1.render(), equalTo("\\cvskill{Programming}{5.0}"))
        assertThat(skill2.render(), equalTo("\\cvskillstr{Language}{Fluent}"))
    }

    @Test
    fun `throw exception when creating an invalid skill`() {
        val ex = assertThrows<IllegalArgumentException> {
            Skill("A skill", 10.0)
            Skill("A Skill", 0.0)
        }
        assertThat(ex.message, equalTo("Skill rating must be between 1 and 5"))
    }

    @Test
    fun `create an event`() {
        val event = Event.create("Job Title 1") {
            holder = "Company 1"
            location = "Location"
            duration = eventDurationStr("Month XXXX", "Ongoing")
            description = listOf(
                Item("Job description 1"),
                Item("Job description 2"),
                Item("Job description 3", false)
            )
        }

        assertThat(event, instanceOf(SectionContent::class.java))
        assertThat(event, not(instanceOf(HasSimpleContent::class.java)))
        assertThat(event.type, equalTo(ContentType.EVENT))
        assertThat(event.title, equalTo("Job Title 1"))
        assertThat(event.holder, equalTo("Company 1"))
        assertThat(event.location, equalTo("Location"))
        assertThat(event.duration, instanceOf(EventPeriodString::class.java))
        assertThat(event.duration, hasProperty("start", equalTo("Month XXXX")))
        assertThat(event.duration, hasProperty("end", equalTo("Ongoing")))
        assertThat(event.description, hasSize(3))
        assertThat(event.description, everyItem(instanceOf(Item::class.java)))

        val expected =
            """
            \cvevent{Job Title 1}{Company 1}{Month XXXX -- Ongoing}{Location}
            \begin{itemize}
            \item Job description 1
            \item Job description 2
            \item[] Job description 3
            \end{itemize}
            """.trimIndent()
        assertThat(event.render(), equalTo(expected))
    }

    @Test
    fun `create an event without description`() {
        val event = Event.create("Job Title 1") {
            holder = "Company 1"
            location = "Location"
        }

        assertThat(event, instanceOf(SectionContent::class.java))
        assertThat(event, not(instanceOf(HasSimpleContent::class.java)))
        assertThat(event.description, emptyIterableOf(Item::class.java))

        val expected1 = "\\cvevent{Job Title 1}{Company 1}{}{Location}"
        assertThat(event.render(), equalTo(expected1))
    }

    @Test
    fun `create an event with duration as date`() {
        val event = Event.create("Job Title 1") {
            holder = "Company 1"
            duration = eventDurationDate("2023-10", "2023-11")
            location = "Location"
        }

        assertThat(event.duration, instanceOf(EventPeriodDate::class.java))
        assertThat(event.duration, hasProperty("start", equalTo(LocalDate.of(2023, 10, 1))))
        assertThat(event.duration, hasProperty("end", equalTo(LocalDate.of(2023, 11, 1))))

        val expected = "\\cvevent{Job Title 1}{Company 1}{Oct 2023 -- Nov 2023}{Location}"
        assertThat(event.render(), equalTo(expected))
    }

    @Test
    fun `create an event with duration as string - no end`() {
        val event = Event.create("Job Title 1") {
            holder = "Company 1"
            duration = eventDurationStr("Project duration")
            location = "Location"
        }

        assertThat(event.duration, instanceOf(EventPeriodString::class.java))
        assertThat(event.duration, hasProperty("start", equalTo("Project duration")))
        assertThat(event.duration, hasProperty("end", emptyString()))

        val expected = "\\cvevent{Job Title 1}{Company 1}{Project duration}{Location}"
        assertThat(event.render(), equalTo(expected))
    }

    @Test
    fun `create an event without duration`() {
        val event = Event.create("Job Title 1") {
            holder = "Company 1"
            location = "Location"
        }

        event.duration = NoEventPeriod
        assertThat(event.duration, instanceOf(NoEventPeriod::class.java))
        assertThat(event.duration, not(hasProperty("start")))
        assertThat(event.duration, not(hasProperty("end")))

        val expected = "\\cvevent{Job Title 1}{Company 1}{}{Location}"
        assertThat(event.render(), equalTo(expected))
    }

    @Test
    fun `create an empty event`() {
        val event = Event.create("") {}

        assertThat(event, instanceOf(SectionContent::class.java))
        assertThat(event, not(instanceOf(HasSimpleContent::class.java)))
        assertThat(event.type, equalTo(ContentType.EVENT))
        assertThat(event.title, emptyString())
        assertThat(event.holder, emptyString())
        assertThat(event.location, emptyString())
        assertThat(event.duration, instanceOf(NoEventPeriod::class.java))
        assertThat(event.duration, not(hasProperty("start")))
        assertThat(event.duration, not(hasProperty("end")))
        assertThat(event.description, emptyIterableOf(Item::class.java))

        assertThat(event.render(), equalTo("\\cvevent{}{}{}{}"))
    }

    @Test
    fun `create a wheel chart item`() {
        val item = WheelChartItem(10, 8, "accent", "Programming")

        assertThat(item, not(isA(SectionContent::class.java)))
        assertThat(item, not(isA(HasSimpleContent::class.java)))
        assertThat(item.value, equalTo(10))
        assertThat(item.textWidth, equalTo(8))
        assertThat(item.color, equalTo("accent"))
        assertThat(item.detail, equalTo("Programming"))
    }

    @Test
    fun `create a wheel chart`() {
        val wheelChartItems = listOf(
            WheelChartItem(6, 8, "accent!30", "Sleep,\\beautiful sleep"),
            WheelChartItem(3, 8, "accent!40", "Hopeful novelist by night"),
            WheelChartItem(8, 8, "accent!60", "Daytime job"),
            WheelChartItem(2, 10, "accent", "Sports and relaxation"),
            WheelChartItem(5, 8, "accent!20", "Spending time with family")
        )

        val wheelChart = WheelChart(1.5, 0.5, wheelChartItems)

        assertThat(wheelChart, instanceOf(SectionContent::class.java))
        assertThat(wheelChart, not(instanceOf(HasSimpleContent::class.java)))
        assertThat(wheelChart.type, equalTo(ContentType.WHEELCHART))

        assertThat(wheelChart.innerRadius, equalTo(1.5))
        assertThat(wheelChart.outerRadius, equalTo(0.5))
        assertThat(wheelChart.items, hasSize(5))
        assertThat(wheelChart.items, equalTo(wheelChartItems))
        assertThat(wheelChart.items, everyItem(instanceOf(WheelChartItem::class.java)))

        val expected =
            """
            % \wheelchart{outer radius}{inner radius}{
            % comma-separated list of value/text width/color/detail}
            \wheelchart{1.5cm}{0.5cm}{
              6/8em/accent!30/{Sleep,\beautiful sleep},
              3/8em/accent!40/Hopeful novelist by night,
              8/8em/accent!60/Daytime job,
              2/10em/accent/Sports and relaxation,
              5/8em/accent!20/Spending time with family
            }
            """.trimIndent()

        assertThat(wheelChart.render(), equalTo(expected))
    }

    @Test
    fun `create event duration as string`() {
        val eventDuration1 = eventDurationStr("Month XXXX", "Ongoing")
        val eventDuration2 = eventDurationStr("Project Duration")

        assertThat(eventDuration1, instanceOf(EventPeriodString::class.java))
        assertThat(eventDuration2, instanceOf(EventPeriodString::class.java))

        assertThat(eventDuration1.start, equalTo("Month XXXX"))
        assertThat(eventDuration1.end, equalTo("Ongoing"))

        assertThat(eventDuration2.start, equalTo("Project Duration"))
        assertThat(eventDuration2.end, emptyString())
    }

    @Test
    fun `create event duration as date`() {
        val eventDuration2 = eventDurationDate("2023-09", "2023-11")

        assertThat(eventDuration2, instanceOf(EventPeriodDate::class.java))
        assertThat(eventDuration2.start, equalTo(LocalDate.of(2023, 9, 1)))
        assertThat(eventDuration2.end, equalTo(LocalDate.of(2023, 11, 1)))
    }

    @Test
    fun `throw exception when creating invalid event duration as date`() {
        val ex1 = assertThrows<IllegalArgumentException> {
            eventDurationDate("Month XXXX", "Ongoing")
        }
        assertThat(ex1.message, equalTo("Invalid date format: Month XXXX. Expected format: yyyy-MM"))

        val ex2 = assertThrows<IllegalArgumentException> {
            eventDurationDate("2023-09", "Ongoing")
        }
        assertThat(ex2.message, equalTo("Invalid date format: Ongoing. Expected format: yyyy-MM"))
    }

    @Test
    fun `create elements with simple content`() {
        val tag = Tag("This is a tag")
        val quote = Quote("This is a quote")
        val generic = LatexContent("\\vspace{2cm}")

        val elements = listOf(tag, quote, generic)
        assertThat(elements, everyItem(instanceOf(SectionContent::class.java)))
        assertThat(elements, everyItem(instanceOf(HasSimpleContent::class.java)))

        assertThat(tag.type, equalTo(ContentType.TAG))
        assertThat(quote.type, equalTo(ContentType.QUOTE))
        assertThat(generic.type, equalTo(ContentType.GENERIC))

        assertThat(tag.content, equalTo("This is a tag"))
        assertThat(quote.content, equalTo("This is a quote"))
        assertThat(generic.content, equalTo("\\vspace{2cm}"))

        assertThat(tag.render(), equalTo("\\cvtag{This is a tag}"))
        assertThat(quote.render(), equalTo("\\begin{quote}\n``This is a quote''\n\\end{quote}"))
        assertThat(generic.render(), equalTo("\\vspace{2cm}"))
    }

    @Test
    fun `create elements without content`() {
        val elements = listOf(Divider, NewLine, NewPage, NoContent)

        assertThat(elements, everyItem(instanceOf(SectionContent::class.java)))
        assertThat(elements, everyItem(not(instanceOf(HasSimpleContent::class.java))))

        assertThat(Divider.type, equalTo(ContentType.DIVIDER))
        assertThat(NewLine.type, equalTo(ContentType.NEWLINE))
        assertThat(NewPage.type, equalTo(ContentType.NEWPAGE))
        assertThat(NoContent.type, equalTo(ContentType.EMPTY))

        assertThat(Divider.render(), equalTo("\n\\divider\n"))
        assertThat(NewLine.render(), equalTo("\\\\"))
        assertThat(NewPage.render(), equalTo("\\newpage"))
        assertThat(NoContent.render(), emptyString())
    }
}
