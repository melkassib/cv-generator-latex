@file:JvmName("ResumeBuilders")

package com.melkassib.altacv.generator.dsl.domain.builders

import com.melkassib.altacv.generator.dsl.domain.Resume
import com.melkassib.altacv.generator.dsl.domain.ResumeConfig
import com.melkassib.altacv.generator.dsl.domain.ResumeHeader
import com.melkassib.altacv.generator.dsl.domain.section.*
import com.melkassib.altacv.generator.dsl.utils.separateWith

/**
 * Builds a resume using a builder pattern.
 *
 * @param init The initialization block for the resume builder.
 * @return The built resume.
 */
fun altacv(init: ResumeBuilder.() -> Unit) = ResumeBuilder().apply(init).build()

/**
 * Builder class for creating a resume.
 */
class ResumeBuilder {
    private var _config = ResumeConfig()
    private var _header = ResumeHeader()
    private var _sections = emptyList<Section>()

    /**
     * Configures the resume configuration.
     *
     * @param init The initialization block for the resume configuration.
     */
    fun config(init: ResumeConfig.() -> Unit) {
        _config = ResumeConfig().apply(init)
    }

    /**
     * Configures the resume header.
     *
     * @param init The initialization block for the resume header.
     */
    fun header(init: ResumeHeader.() -> Unit) {
        _header = ResumeHeader().apply(init)
    }

    /**
     * Configures the sections of the resume.
     *
     * @param init The initialization block for the sections of the resume.
     */
    fun sections(init: SectionListBuilder.() -> Unit) {
        _sections = SectionListBuilder().apply(init).build()
    }

    /**
     * Builds the resume.
     *
     * @return The built resume.
     */
    fun build() = Resume(_config, _header, _sections)
}

/**
 * Builder class for creating a list of sections.
 */
class SectionListBuilder {
    private val _sections = mutableListOf<Section>()

    /**
     * Adds a [Section] to the list of sections.
     *
     * @param title The title of the section.
     * @param position The position of the section.
     * @param separator The separator of the section.
     * @param ignored Whether the section is ignored.
     * @param init The initialization block for the section.
     */
    @JvmOverloads
    fun section(
        title: String,
        position: SectionPosition,
        separator: SectionContent = NoContent,
        ignored: Boolean = false,
        init: SectionBuilder.() -> Unit
    ) {
        val contents = SectionBuilder().apply(init).build().separateWith(separator)
        _sections += Section(title, position, contents, ignored)
    }

    /**
     * Builds the list of sections.
     *
     * @return The built list of sections.
     */
    fun build() = _sections.toList()
}

/**
 * Builder class for creating a section.
 */
class SectionBuilder {
    private val _contents = mutableListOf<SectionContent>()

    /**
     * Adds the contents of the section.
     *
     * @param init The initialization block for the contents of the section.
     */
    fun contents(init: SectionContentBuilder.() -> Unit) {
        _contents += SectionContentBuilder().apply(init).build()
    }

    /**
     * Builds the contents of the section.
     *
     * @return The built contents of the section.
     */
    fun build() = _contents.toList()
}

/**
 * Builder class for creating the contents of a section.
 */
class SectionContentBuilder {
    private val _contents = mutableListOf<SectionContent>()

    /**
     * Adds a content to the section.
     *
     * @param item The content to add.
     */
    fun content(item: SectionContent) {
        _contents += item
    }

    /**
     * Adds a latex content to the section.
     *
     * @param value The latex content to add.
     */
    fun content(value: String) {
        _contents += LatexContent(value)
    }

    /**
     * Adds an event to the section.
     *
     * @param title The title of the event.
     * @param init The initialization block for the event.
     */
    fun event(title: String, init: Event.() -> Unit) {
        _contents += Event.create(title, init)
    }

    /**
     * Adds a tag to the section.
     *
     * @param title The title of the tag.
     */
    fun tag(title: String) {
        _contents += Tag(title)
    }

    /**
     * Adds a quote to the section.
     *
     * @param title The title of the quote.
     */
    fun quote(title: String) {
        _contents += Quote(title)
    }

    /**
     * Adds a skill to the section.
     *
     * @param title The title of the skill.
     * @param fluency The fluency of the skill.
     */
    fun skill(title: String, fluency: String) {
        _contents += SkillStr(title, fluency)
    }

    /**
     * Adds a skill to the section.
     *
     * @param title The title of the skill.
     * @param rating The rating of the skill.
     */
    fun skill(title: String, rating: Double) {
        _contents += Skill(title, rating)
    }

    /**
     * Adds an achievement to the section.
     *
     * @param iconName The name of the icon.
     * @param achievement The achievement.
     * @param detail The detail of the achievement.
     */
    fun achievement(iconName: String, achievement: String, detail: String) {
        _contents += Achievement(iconName, achievement, detail)
    }

    /**
     * Adds a wheel chart to the section.
     *
     * @param innerRadius The inner radius of the wheel chart.
     * @param outerRadius The outer radius of the wheel chart.
     * @param init The initialization block for the wheel chart.
     */
    fun wheelchart(innerRadius: Double, outerRadius: Double, init: WheelChartBuilder.() -> Unit) {
        val wheelChartItems = WheelChartBuilder().apply(init).build()
        _contents += WheelChart(innerRadius, outerRadius, wheelChartItems)
    }

    /**
     * Builds the contents of the section.
     *
     * @return The built contents of the section.
     */
    fun build() = _contents.toList()
}

/**
 * Represents the builder for a wheel chart.
 * This class is used to build a wheel chart.
 */
class WheelChartBuilder {
    private val _items = mutableListOf<WheelChartItem>()

    /**
     * Adds an item to the wheel chart.
     *
     * @param value The value of the item.
     * @param textWidth The text width of the item.
     * @param color The color of the item.
     * @param detail The detail of the item.
     */
    fun item(value: Int, textWidth: Int, color: String, detail: String) {
        _items += WheelChartItem(value, textWidth, color, detail)
    }

    /**
     * Builds the items of the wheel chart.
     *
     * @return The built items of the wheel chart.
     */
    fun build() = _items.toList()
}
