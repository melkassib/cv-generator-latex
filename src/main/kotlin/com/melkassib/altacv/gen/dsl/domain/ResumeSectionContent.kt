@file:JvmName("SectionContent")

package com.melkassib.altacv.gen.dsl.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.altacv.gen.dsl.serialization.EventPeriodDeserializer
import com.melkassib.altacv.gen.dsl.serialization.JSON_MAPPER
import com.melkassib.altacv.gen.dsl.serialization.SectionContentSerializers
import com.melkassib.altacv.gen.dsl.utils.SectionEventDuration
import com.melkassib.altacv.gen.dsl.utils.escapeSpecialChars
import java.time.LocalDate

enum class ContentType {
    DIVIDER,
    NEWLINE,
    NEWPAGE,
    EMPTY,
    TAG,
    QUOTE,
    GENERIC,
    ACHIEVEMENT,
    SKILL,
    EVENT,
    ITEM,
    WHEELCHART
}

@JsonSerialize(using = SectionContentSerializers.ContentWrapperSerializer::class)
@JsonDeserialize(using = SectionContentSerializers.ContentWrapperDeserializer::class)
data class ContentWrapper(val content: SectionContent)

interface HasSimpleContent {
    val content: String
}

sealed class SectionContent(@JsonIgnore val type: ContentType) {
    abstract fun render(): String

    fun wrapped() = ContentWrapper(this)
    fun toJson(): String = JSON_MAPPER.writeValueAsString(wrapped())
}

data object Divider : SectionContent(ContentType.DIVIDER) {
    override fun render() = "\n\\divider\n"
}

data object NewLine : SectionContent(ContentType.NEWLINE) {
    override fun render() = "\\\\"
}

data object NewPage : SectionContent(ContentType.NEWPAGE) {
    override fun render() = "\\newpage"
}

data object NoContent : SectionContent(ContentType.EMPTY) {
    override fun render() = ""
}

data class Tag(override val content: String) : SectionContent(ContentType.TAG), HasSimpleContent {
    override fun render() = "\\cvtag{${content.escapeSpecialChars()}}"
}

data class Quote(override val content: String) : SectionContent(ContentType.QUOTE), HasSimpleContent {
    override fun render() = "\\begin{quote}\n``${content.escapeSpecialChars()}''\n\\end{quote}"
}

data class LatexContent(override val content: String) : SectionContent(ContentType.GENERIC), HasSimpleContent {
    override fun render() = content
}

data class Achievement(
    val iconName: String,
    val achievement: String,
    val detail: String
) : SectionContent(ContentType.ACHIEVEMENT) {
    override fun render() =
        "\\cvachievement{\\$iconName}{${achievement.escapeSpecialChars()}}{${detail.escapeSpecialChars()}}"
}

data class Skill(val skill: String, val rating: Double) : SectionContent(ContentType.SKILL) {
    init {
        require(rating in 1.0..5.0) {
            "Skill rating must be between 1 and 5"
        }
    }

    override fun render() = "\\cvskill{${skill.escapeSpecialChars()}}{$rating}"
}

data class SkillStr(val skill: String, val fluency: String) : SectionContent(ContentType.SKILL) {
    override fun render() = "\\cvskillstr{${skill.escapeSpecialChars()}}{${fluency.escapeSpecialChars()}}"
}

data class Item @JvmOverloads constructor(
    val description: String,
    val withBullet: Boolean = true
) : SectionContent(ContentType.ITEM) {
    override fun render() = if (withBullet) {
        "\\item ${description.escapeSpecialChars()}"
    } else {
        "\\item[] ${description.escapeSpecialChars()}"
    }
}

@Suppress("MaxLineLength")
class Event private constructor(
    var title: String = "",
    @JvmField var holder: String = "",
    @JvmField var location: String = "",
    @JsonDeserialize(using = EventPeriodDeserializer::class)
    @JvmField var duration: EventPeriod = NoEventPeriod,
    @JvmField var description: List<Item> = mutableListOf()
) : SectionContent(ContentType.EVENT) {
    private fun LocalDate.render() = format(SectionEventDuration.RENDER_DATE_PATTERN)

    override fun render(): String {
        val durationStr: String = when (duration) {
            is EventPeriodString -> with(duration as EventPeriodString) {
                if (end.isEmpty()) start else "$start -- $end"
            }
            is EventPeriodDate -> with(duration as EventPeriodDate) {
                "${start.render()} -- ${end.render()}"
            }
            is NoEventPeriod -> ""
        }

        return buildString {
            append(
                "\\cvevent{${title.escapeSpecialChars()}}{${holder.escapeSpecialChars()}}{$durationStr}{${location.escapeSpecialChars()}}"
            )

            if (description.isNotEmpty()) {
                append("\n\\begin{itemize}\n")
                append(description.joinToString("\n") { it.render() })
                append("\n\\end{itemize}")
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(title: String, init: Event.() -> Unit) = Event().apply {
            this.title = title
            init()
        }
    }
}

data class WheelChart(
    val innerRadius: Double,
    val outerRadius: Double,
    val items: List<WheelChartItem>
) : SectionContent(ContentType.WHEELCHART) {
    override fun render() =
        """
        |% \wheelchart{outer radius}{inner radius}{
        |% comma-separated list of value/text width/color/detail}
        |\wheelchart{${innerRadius}cm}{${outerRadius}cm}{
        |  ${items.joinToString(",\n  ") { it.toString() }}
        |}
        """.trimMargin()
}

data class WheelChartItem(val value: Int, val textWidth: Int, val color: String, val detail: String) {
    override fun toString(): String {
        val itemDetail = detail.escapeSpecialChars().let {
            if (it.contains(",")) "{$it}" else it
        }

        return "$value/${textWidth}em/$color/$itemDetail"
    }
}
