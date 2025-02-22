package com.melkassib.cvgenerator.altacv.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import com.jayway.jsonpath.matchers.JsonPathMatchers.hasNoJsonPath
import com.melkassib.cvgenerator.common.domain.*
import com.melkassib.cvgenerator.common.domain.EventPeriodDate.Companion.eventDurationDate
import com.melkassib.cvgenerator.common.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.cvgenerator.common.domain.Section
import com.melkassib.cvgenerator.common.domain.SectionPosition
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.common.utils.firstColumn
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SectionContentSerializationTest {

    @Test
    fun `serialize event with duration as date`() {
        val event = Event.create("") {
            duration = eventDurationDate("2023-10", "2023-11")
        }

        val eventJson = event.toJson()
        assertThat(eventJson, hasJsonPath("$.type", equalTo("EVENT")))
        assertThat(eventJson, hasJsonPath("$.content.duration.start", equalTo("2023-10")))
        assertThat(eventJson, hasJsonPath("$.content.duration.end", equalTo("2023-11")))
    }

    @Test
    fun `serialize event with duration as string`() {
        val event = Event.create("") {
            duration = eventDurationStr("Oct 23", "Nov 23")
        }

        val eventJson = event.toJson()
        assertThat(eventJson, hasJsonPath("$.type", equalTo("EVENT")))
        assertThat(eventJson, hasJsonPath("$.content.duration.start", equalTo("Oct 23")))
        assertThat(eventJson, hasJsonPath("$.content.duration.end", equalTo("Nov 23")))
    }

    @Test
    fun `serialize event with no duration`() {
        val event = Event.create("") {}

        val eventJson = event.toJson()
        assertThat(eventJson, hasJsonPath("$.type", equalTo("EVENT")))
        assertThat(event.duration, instanceOf(NoEventPeriod::class.java))
        assertThat(eventJson, hasNoJsonPath("$.content.duration.start"))
        assertThat(eventJson, hasNoJsonPath("$.content.duration.end"))
    }

    @Test
    fun `deserialize event with duration as date`() {
        val eventJson = """{"type":"EVENT","content":{"duration":{"start":"2023-10","end":"2023-11"}}}"""
        val event = JSON_MAPPER.readValue<ContentWrapper>(eventJson).content as Event

        assertThat(event.duration, instanceOf(EventPeriodDate::class.java))
        assertThat(event.duration, hasProperty("start", equalTo(LocalDate.of(2023, 10, 1))))
        assertThat(event.duration, hasProperty("end", equalTo(LocalDate.of(2023, 11, 1))))
    }

    @Test
    fun `deserialize event with duration as string`() {
        val eventJson = """{"type":"EVENT","content":{"duration":{"start":"Oct 23","end":"Ongoing"}}}"""
        val event = JSON_MAPPER.readValue<ContentWrapper>(eventJson).content as Event

        assertThat(event.duration, instanceOf(EventPeriodString::class.java))
        assertThat(event.duration, hasProperty("start", equalTo("Oct 23")))
        assertThat(event.duration, hasProperty("end", equalTo("Ongoing")))
    }

    @Test
    fun `serialize event (entry) with duration as date`() {
        val event = Entry.create("") {
            duration = eventDurationDate("2023-10", "2023-11")
        }

        val eventJson = event.toJson()
        assertThat(eventJson, hasJsonPath("$.type", equalTo("EVENT_ENTRY")))
        assertThat(eventJson, hasJsonPath("$.content.duration.start", equalTo("2023-10")))
        assertThat(eventJson, hasJsonPath("$.content.duration.end", equalTo("2023-11")))
    }

    @Test
    fun `serialize event (entry) with duration as string`() {
        val event = Entry.create("") {
            duration = eventDurationStr("Oct 23", "Nov 23")
        }

        val eventJson = event.toJson()
        assertThat(eventJson, hasJsonPath("$.type", equalTo("EVENT_ENTRY")))
        assertThat(eventJson, hasJsonPath("$.content.duration.start", equalTo("Oct 23")))
        assertThat(eventJson, hasJsonPath("$.content.duration.end", equalTo("Nov 23")))
    }

    @Test
    fun `serialize event (entry) with no duration`() {
        val event = Entry.create("") {}

        val eventJson = event.toJson()
        assertThat(eventJson, hasJsonPath("$.type", equalTo("EVENT_ENTRY")))
        assertThat(event.duration, instanceOf(NoEventPeriod::class.java))
        assertThat(eventJson, hasNoJsonPath("$.content.duration.start"))
        assertThat(eventJson, hasNoJsonPath("$.content.duration.end"))
    }

    @Test
    fun `deserialize event (entry) with duration as date`() {
        val eventJson = """{"type":"EVENT_ENTRY","content":{"duration":{"start":"2023-10","end":"2023-11"}}}"""
        val event = JSON_MAPPER.readValue<ContentWrapper>(eventJson).content as Entry

        assertThat(event.duration, instanceOf(EventPeriodDate::class.java))
        assertThat(event.duration, hasProperty("start", equalTo(LocalDate.of(2023, 10, 1))))
        assertThat(event.duration, hasProperty("end", equalTo(LocalDate.of(2023, 11, 1))))
    }

    @Test
    fun `deserialize event (entry) with duration as string`() {
        val eventJson = """{"type":"EVENT_ENTRY","content":{"duration":{"start":"Oct 23","end":"Ongoing"}}}"""
        val event = JSON_MAPPER.readValue<ContentWrapper>(eventJson).content as Entry

        assertThat(event.duration, instanceOf(EventPeriodString::class.java))
        assertThat(event.duration, hasProperty("start", equalTo("Oct 23")))
        assertThat(event.duration, hasProperty("end", equalTo("Ongoing")))
    }

    @Test
    fun `serialize a section with list of contents`() {
        val section = Section(
            "SectionA", firstColumn(3),
            listOf(
                Tag("tag1"),
                LatexContent("\\medskip"),
                NewLine,
                Achievement("faTrophy", "Fantastic", "some details")
            )
        )

        val sectionJson = JSON_MAPPER.writeValueAsString(section)

        assertThat(sectionJson, hasJsonPath("$.title", equalTo("SectionA")))
        assertThat(sectionJson, hasJsonPath("$.position.column", equalTo(1)))
        assertThat(sectionJson, hasJsonPath("$.position.order", equalTo(3)))
        assertThat(sectionJson, hasJsonPath("$.contents[0].type", equalTo("TAG")))
        assertThat(sectionJson, hasJsonPath("$.contents[0].content", equalTo("tag1")))
        assertThat(sectionJson, hasJsonPath("$.contents[1].type", equalTo("GENERIC")))
        assertThat(sectionJson, hasJsonPath("$.contents[1].content", equalTo("\\medskip")))
        assertThat(sectionJson, hasJsonPath("$.contents[2].type", equalTo("NEWLINE")))
        assertThat(sectionJson, hasNoJsonPath("$.contents[2].content"))
        assertThat(sectionJson, hasJsonPath("$.contents[3].type", equalTo("ACHIEVEMENT")))
        assertThat(sectionJson, hasJsonPath("$.contents[3].content.iconName", equalTo("faTrophy")))
        assertThat(sectionJson, hasJsonPath("$.contents[3].content.achievement", equalTo("Fantastic")))
        assertThat(sectionJson, hasJsonPath("$.contents[3].content.detail", equalTo("some details")))
    }

    @Test
    fun `deserialize section with contents`() {
        val sectionJson = """
            {
                "title":"SectionB",
                "contents":[
                   {"type":"TAG","content":"T1"},
                   {"type":"DIVIDER"},
                   {"type":"TAG","content":"T2"}
                ],
                "position":{"column":2,"order":3}
            }
        """.trimIndent()

        JSON_MAPPER.readValue<Section>(sectionJson).apply {
            assertThat(title, equalTo("SectionB"))
            assertThat(position, equalTo(SectionPosition(2, 3)))
            assertThat(contents, hasSize(3))
            assertThat(contents[0], equalTo(Tag("T1")))
            assertThat(contents[1], equalTo(Divider))
            assertThat(contents[2], equalTo(Tag("T2")))
        }
    }

    @Test
    fun `serialize a skill`() {
        val skill1 = Skill("SkillA", 5.0)
        val skill2 = SkillStr("SkillB", "Fluent")

        val skill1Json = JSON_MAPPER.writeValueAsString(skill1.wrapped())
        val skill2Json = JSON_MAPPER.writeValueAsString(skill2.wrapped())

        assertThat(skill1Json, hasJsonPath("$.type", equalTo("SKILL")))
        assertThat(skill1Json, hasJsonPath("$.content.skill", equalTo("SkillA")))
        assertThat(skill1Json, hasJsonPath("$.content.rating", equalTo(5.0)))
        assertThat(skill1Json, hasNoJsonPath("$.content.fluency"))

        assertThat(skill2Json, hasJsonPath("$.type", equalTo("SKILL")))
        assertThat(skill2Json, hasJsonPath("$.content.skill", equalTo("SkillB")))
        assertThat(skill2Json, hasJsonPath("$.content.fluency", equalTo("Fluent")))
        assertThat(skill2Json, hasNoJsonPath("$.content.rating"))
    }

    @Test
    fun `deserialize a skill`() {
        val skill1Json =
            """
            {
                "type": "SKILL",
                "content": {
                    "skill": "Arabic",
                    "fluency": "Native/Bilingual"
                }
            }
            """.trimIndent()

        val skill2Json = """
            {
                "type": "SKILL",
                "content": {
                    "skill": "English",
                    "rating": 5.0
                }
            }
        """.trimIndent()

        val dSkill1 = JSON_MAPPER.readValue<ContentWrapper>(skill1Json)
        assertThat(dSkill1.content, equalTo(SkillStr("Arabic", "Native/Bilingual")))

        val dSkill2 = JSON_MAPPER.readValue<ContentWrapper>(skill2Json)
        assertThat(dSkill2.content, equalTo(Skill("English", 5.0)))
    }

    @Test
    fun `simple contents`() {
        val tag = Tag("A tag")
        val quote = Quote("A quote")
        val paragraph = Paragraph("A paragraph")
        val generic = LatexContent("\\medskip")

        val tagJson = """{"type":"TAG","content":"A tag"}"""
        val quoteJson = """{"type":"QUOTE","content":"A quote"}"""
        val paragraphJson = """{"type":"PARAGRAPH","content":"A paragraph"}"""
        val genericJson = """{"type":"GENERIC","content":"\\medskip"}"""
        val newLineJson = """{"type":"NEWLINE"}"""
        val newPageJson = """{"type":"NEWPAGE"}"""
        val dividerJson = """{"type":"DIVIDER"}"""

        assertThat(tag.toJson(), equalTo(tagJson))
        assertThat(quote.toJson(), equalTo(quoteJson))
        assertThat(paragraph.toJson(), equalTo(paragraphJson))
        assertThat(generic.toJson(), equalTo(genericJson))
        assertThat(NewLine.toJson(), equalTo(newLineJson))
        assertThat(NewPage.toJson(), equalTo(newPageJson))
        assertThat(Divider.toJson(), equalTo(dividerJson))
        assertThat(NoContent.toJson(), emptyString())

        fun String.toContent() = JSON_MAPPER.readValue<ContentWrapper>(this).content
        assertThat(tagJson.toContent(), equalTo(tag))
        assertThat(quoteJson.toContent(), equalTo(quote))
        assertThat(paragraphJson.toContent(), equalTo(paragraph))
        assertThat(genericJson.toContent(), equalTo(generic))
        assertThat(newLineJson.toContent(), equalTo(NewLine))
        assertThat(newPageJson.toContent(), equalTo(NewPage))
        assertThat(dividerJson.toContent(), equalTo(Divider))
        assertThat("""{"type":"EMPTY"}""".toContent(), equalTo(NoContent))
    }

    @Test
    fun item() {
        val item1 = Item("item1")
        val item2 = Item("item2", false)
        val item1Json = """{"type":"ITEM","content":{"description":"item1","withBullet":true}}"""
        val item2Json = """{"type":"ITEM","content":{"description":"item2","withBullet":false}}"""

        assertThat(item1.toJson(), equalTo(item1Json))
        assertThat(item2.toJson(), equalTo(item2Json))

        val dItem1 = JSON_MAPPER.readValue<ContentWrapper>(item1Json).content as Item
        assertThat(dItem1.description, equalTo("item1"))
        assertThat(dItem1.withBullet, equalTo(true))

        val dItem2 = JSON_MAPPER.readValue<ContentWrapper>(item2Json).content as Item
        assertThat(dItem2.description, equalTo("item2"))
        assertThat(dItem2.withBullet, equalTo(false))
    }

    @Test
    fun achievement() {
        val achievement = Achievement("faTrophy", "Fantastic", "some details")
        val achievementJson = """{"type":"ACHIEVEMENT","content":{"iconName":"faTrophy","achievement":"Fantastic","detail":"some details"}}"""

        assertThat(achievement.toJson(), equalTo(achievementJson))

        val dAchievement = JSON_MAPPER.readValue<ContentWrapper>(achievementJson).content as Achievement
        assertThat(dAchievement.iconName, equalTo("faTrophy"))
        assertThat(dAchievement.achievement, equalTo("Fantastic"))
        assertThat(dAchievement.detail, equalTo("some details"))
    }

    @Test
    fun wheelchart() {
        val wheelChart = WheelChart(
            1.5, 0.5,
            listOf(
                WheelChartItem(8, 8, "accent!60", "Daytime job"),
                WheelChartItem(2, 10, "accent", "Sports and relaxation")
            )
        )

        val wheelChartJson = """
            {"type":"WHEELCHART","content":{"innerRadius":1.5,"outerRadius":0.5,"items":[{"value":8,"textWidth":8,
            "color":"accent!60","detail":"Daytime job"},{"value":2,"textWidth":10,"color":"accent","detail":
            "Sports and relaxation"}]}}
        """.trimIndent().replace(Regex("(\n*)\n"), "$1")

        val dWheelChart = JSON_MAPPER.readValue<ContentWrapper>(wheelChartJson).content as WheelChart

        assertThat(wheelChart.toJson(), equalTo(wheelChartJson))
        assertThat(dWheelChart.innerRadius, equalTo(1.5))
        assertThat(dWheelChart.outerRadius, equalTo(0.5))
        assertThat(dWheelChart.items, hasSize(2))
        assertThat(dWheelChart.items, everyItem(instanceOf(WheelChartItem::class.java)))
    }
}
