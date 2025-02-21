@file:JvmName("SectionContent")

package com.melkassib.cvgenerator.common.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.common.serialization.EventPeriodDeserializer
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.common.serialization.SectionContentSerializers
import com.melkassib.cvgenerator.common.utils.SectionEventDuration
import com.melkassib.cvgenerator.common.utils.escapeSpecialChars
import java.time.LocalDate

/**
 * Represents the different types of content that can be added to a section of the resume.
 */
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
    EVENT_ENTRY,
    ITEM,
    WHEELCHART,
    HONOR_LIST,
    PARAGRAPH
}

/**
 * Represents a wrapper for a section content.
 * This class is used to wrap a section content in a JSON object.
 */
@JsonSerialize(using = SectionContentSerializers.ContentWrapperSerializer::class)
@JsonDeserialize(using = SectionContentSerializers.ContentWrapperDeserializer::class)
data class ContentWrapper(val content: SectionContent)

/**
 * Represents a content that has a simple string content.
 */
interface HasSimpleContent {
    val content: String
}

/**
 * Represents a section content.
 * This class is used to represent a section content in the resume.
 *
 * @property type The type of the section content.
 */
sealed class SectionContent(@JsonIgnore val type: ContentType) {
    /**
     * Renders the section content as a string.
     *
     * @return The rendered string of the section content.
     */
    abstract fun render(): String

    /**
     * Wraps the section content in a ContentWrapper.
     *
     * @return The wrapped section content.
     */
    fun wrapped() = ContentWrapper(this)

    /**
     * Converts the section content to a JSON string.
     *
     * @return The JSON string representation of the section content.
     */
    fun toJson(): String = JSON_MAPPER.writeValueAsString(wrapped())
}

/**
 * Represents a divider content.
 * This class is used to represent a divider content in the resume.
 */
data object Divider : SectionContent(ContentType.DIVIDER) {
    override fun render() = "\n\\divider\n"
}

/**
 * Represents a newline content.
 * This class is used to represent a newline content in the resume.
 */
data object NewLine : SectionContent(ContentType.NEWLINE) {
    override fun render() = "\\\\"
}

/**
 * Represents a new page content.
 * This class is used to represent a new page content in the resume.
 */
data object NewPage : SectionContent(ContentType.NEWPAGE) {
    override fun render() = "\\newpage"
}

/**
 * Represents an empty content.
 * This class is used to represent an empty content in the resume.
 */
data object NoContent : SectionContent(ContentType.EMPTY) {
    override fun render() = ""
}

/**
 * Represents a tag content.
 * This class is used to represent a tag content in the resume.
 *
 * @property content The content of the tag.
 */
data class Tag(override val content: String) : SectionContent(ContentType.TAG), HasSimpleContent {
    override fun render() = "\\cvtag{${content.escapeSpecialChars()}}"
}

/**
 * Represents a quote content.
 * This class is used to represent a quote content in the resume.
 *
 * @property content The content of the quote.
 */
data class Quote(override val content: String) : SectionContent(ContentType.QUOTE), HasSimpleContent {
    override fun render() = "\\begin{quote}\n``${content.escapeSpecialChars()}''\n\\end{quote}"
}

data class Paragraph(override val content: String) : SectionContent(ContentType.PARAGRAPH), HasSimpleContent {
    override fun render() = "\\begin{cvparagraph}\n${content.escapeSpecialChars()}\n\\end{cvparagraph}"
}

/**
 * Represents a latex content.
 * This class is used to represent a latex content in the resume.
 *
 * @property content The latex content.
 */
data class LatexContent(override val content: String) : SectionContent(ContentType.GENERIC), HasSimpleContent {
    override fun render() = content
}

/**
 * Represents an achievement content.
 * This class is used to represent an achievement content in the resume.
 *
 * @property iconName The name of the icon.
 * @property achievement The achievement description.
 * @property detail The detail of the achievement.
 */
data class Achievement(
    val iconName: String,
    val achievement: String,
    val detail: String
) : SectionContent(ContentType.ACHIEVEMENT) {
    override fun render() =
        "\\cvachievement{\\$iconName}{${achievement.escapeSpecialChars()}}{${detail.escapeSpecialChars()}}"
}

/**
 * Represents a skill content.
 * This class is used to represent a skill content in the resume.
 *
 * @property skill The name of the skill.
 * @property rating The rating of the skill.
 */
data class Skill(val skill: String, val rating: Double) : SectionContent(ContentType.SKILL) {
    init {
        require(rating in 1.0..5.0) {
            "Skill rating must be between 1 and 5"
        }
    }

    override fun render() = "\\cvskill{${skill.escapeSpecialChars()}}{$rating}"
}

/**
 * Represents a skill content.
 * This class is used to represent a skill content in the resume.
 *
 * @property skill The name of the skill.
 * @property fluency The fluency level of the skill.
 */
data class SkillStr(val skill: String, val fluency: String) : SectionContent(ContentType.SKILL) {
    override fun render() = "\\cvskillstr{${skill.escapeSpecialChars()}}{${fluency.escapeSpecialChars()}}"
}

/**
 * Represents an item content.
 * This class is used to represent an item content in the resume.
 *
 * @property description The description of the item.
 * @property withBullet Whether the item should be displayed with a bullet.
 */
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

/**
 * Represents an event content.
 * This class is used to represent an event content in the resume.
 *
 * @property title The title of the event.
 * @property holder The holder of the event.
 * @property location The location of the event.
 * @property duration The duration of the event.
 * @property description The description of the event.
 */
sealed class BaseEvent(
    type: ContentType,
    var title: String = "",
    var holder: String = "",
    var location: String = "",
    open var duration: EventPeriod = NoEventPeriod,
    var description: List<Item> = mutableListOf()
) : SectionContent(type) {
    private fun LocalDate.render() = format(SectionEventDuration.RENDER_DATE_PATTERN)

    protected fun renderDuration(): String = when (duration) {
        is EventPeriodString -> with(duration as EventPeriodString) {
            if (end.isEmpty()) start else "$start -- $end"
        }
        is EventPeriodDate -> with(duration as EventPeriodDate) {
            "${start.render()} -- ${end.render()}"
        }
        is NoEventPeriod -> ""
    }

    protected fun renderDescription(): String = if (description.isNotEmpty()) {
        "\n\\begin{itemize}\n" + description.joinToString("\n") { it.render() } + "\n\\end{itemize}"
    } else {
        ""
    }
}

class Event(
    @JsonDeserialize(using = EventPeriodDeserializer::class)
    override var duration: EventPeriod = NoEventPeriod,
) : BaseEvent(type = ContentType.EVENT) {

    override fun render(): String {
        val durationStr = renderDuration()
        return buildString {
            append("\\cvevent{${title.escapeSpecialChars()}}{${holder.escapeSpecialChars()}}{$durationStr}{${location.escapeSpecialChars()}}")
            append(renderDescription())
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

class Entry(
    @JsonDeserialize(using = EventPeriodDeserializer::class)
    override var duration: EventPeriod = NoEventPeriod,
) : BaseEvent(type = ContentType.EVENT_ENTRY) {
    override fun render(): String {
        return """
        |\cventry
        |   {${title.escapeSpecialChars()}} % Job title
        |   {${holder.escapeSpecialChars()}} % Organization
        |   {${location.escapeSpecialChars()}} % Location
        |   {${renderDuration()}} % Date(s)
        |   ${
            if (description.isNotEmpty()) {
                """
                |{
                |     \begin{cvitems} % Description(s) of tasks/responsibilities
                |     ${description.joinToString(prefix = "\t", separator = "\n\t\t") { it.render() }}
                |     \end{cvitems}
                |   }
                """.trimMargin()
            } else {
                "{}"
            }
        }
        """.trimMargin()
    }

    companion object {
        @JvmStatic
        fun create(title: String, init: Entry.() -> Unit) = Entry().apply {
            this.title = title
            init()
        }
    }
}

/**
 * Represents a wheel chart content.
 * This class is used to represent a wheel chart content in the resume.
 *
 * @property innerRadius The inner radius of the wheel chart.
 * @property outerRadius The outer radius of the wheel chart.
 * @property items The list of items in the wheel chart.
 */
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

/**
 * Represents an item for a wheel chart.
 * This class is used to represent an item for a wheel chart in the resume.
 *
 * @property value The value of the item.
 * @property textWidth The text width of the item.
 * @property color The color of the item.
 * @property detail The detail of the item.
 */
data class WheelChartItem(val value: Int, val textWidth: Int, val color: String, val detail: String) {
    override fun toString(): String {
        val itemDetail = detail.escapeSpecialChars().let {
            if (it.contains(",")) "{$it}" else it
        }

        return "$value/${textWidth}em/$color/$itemDetail"
    }
}

data class HonorList(
    val sectionTitle: String,
    val honorItems: List<HonorItem>
) : SectionContent(ContentType.HONOR_LIST) {
    override fun render() =
        """
        |\cvsubsection{$sectionTitle}
        |
        |\begin{cvhonors}
        |
        |${honorItems.joinToString("\n\n") { it.toString() }}
        |
        |\end{cvhonors}
        |
        """.trimMargin()
}

data class HonorItem(val award: String, val event: String, val location: String, val date: String) {
    override fun toString(): String {
        return """
        |\cvhonor
        |    {$award} % Award
        |    {$event} % Event
        |    {$location} % Location
        |    {$date} % Date(s)
        """.trimMargin()
    }
}
