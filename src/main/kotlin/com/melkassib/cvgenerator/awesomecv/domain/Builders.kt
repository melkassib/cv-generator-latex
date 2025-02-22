@file:JvmName("ResumeBuilders")

package com.melkassib.cvgenerator.awesomecv.domain

import com.melkassib.cvgenerator.common.domain.*
import com.melkassib.cvgenerator.common.domain.SectionContentBuilder
import com.melkassib.cvgenerator.common.domain.SectionListBuilder

/**
 * Builds an AwesomeCV resume.
 *
 * @param init The initialization block for the resume builder.
 * @return The built resume.
 */
fun awesomecv(init: ResumeBuilder<AwesomeCVSectionContentBuilder>.() -> Unit) = ResumeBuilder(::AwesomeCVSectionContentBuilder).apply(init).build()

/**
 * Builder class for creating a resume.
 */
class ResumeBuilder<T : SectionContentBuilder>(private val contentFactory: () -> T) {
    private var _config = AwesomeCVConfig()
    private var _header = AwesomeCVHeader()
    private var _footer = AwesomeCVFooter()
    private var _sections = emptyList<Section>()

    /**
     * Configures the resume configuration.
     *
     * @param init The initialization block for the resume configuration.
     */
    fun config(init: AwesomeCVConfig.() -> Unit) {
        _config = AwesomeCVConfig().apply(init)
    }

    /**
     * Configures the resume header.
     *
     * @param init The initialization block for the resume header.
     */
    fun header(init: AwesomeCVHeader.() -> Unit) {
        _header = AwesomeCVHeader().apply(init)
    }

    /**
     * Configures the resume footer.
     *
     * @param init The initialization block for the resume footer.
     */
    fun footer(init: AwesomeCVFooter.() -> Unit) {
        _footer = AwesomeCVFooter().apply(init)
    }

    /**
     * Configures the sections of the resume.
     *
     * @param init The initialization block for the sections of the resume.
     */
    fun sections(init: SectionListBuilder<T>.() -> Unit) {
        _sections = SectionListBuilder(contentFactory).apply(init).build()
    }

    /**
     * Builds the resume.
     *
     * @return The built resume.
     */
    fun build() = AwesomeCVResume(_config, _header, _footer, _sections)
}

/**
 * Builder class for creating the contents of a section for the AwesomeCV template.
 */
class AwesomeCVSectionContentBuilder : SectionContentBuilder {
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
    fun event(title: String, init: Entry.() -> Unit) {
        _contents += Entry.create(title, init)
    }

    /**
     * Adds a paragraph to the section.
     *
     * @param content The content of the paragraph.
     */
    fun paragraph(content: String) {
        _contents += Paragraph(content)
    }

    /**
     * Adds a list of honors to the section.
     *
     * @param subSectionTitle The title of the honors' subsection.
     * @param init The initialization block for the honors.
     */
    fun honors(subSectionTitle: String, init: HonorListBuilder.() -> Unit) {
        val honorItems = HonorListBuilder().apply(init).build()
        _contents += HonorList(subSectionTitle, honorItems)
    }

    /**
     * Builds the contents of the section.
     *
     * @return The built contents of the section.
     */
    override fun build() = _contents.toList()
}

/**
 * Builder class for creating a list of honors. Used in the AwesomeCV template.
 */
class HonorListBuilder {
    private val _items = mutableListOf<HonorItem>()

    /**
     * Adds an honor to the list.
     *
     * @param award The award of the honor.
     * @param event The event of the honor.
     * @param location The location of the honor.
     * @param date The date of the honor.
     */
    fun honor(award: String, event: String, location: String, date: String) {
        _items += HonorItem(award, event, location, date)
    }

    /**
     * Builds the list of honors.
     *
     * @return The built list of honors.
     */
    fun build() = _items.toList()
}
