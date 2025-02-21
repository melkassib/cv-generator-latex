@file:JvmName("ResumeBuilders")

package com.melkassib.cvgenerator.awesomecv.domain

import com.melkassib.cvgenerator.common.domain.*
import com.melkassib.cvgenerator.common.domain.SectionContentBuilder
import com.melkassib.cvgenerator.common.domain.SectionListBuilder

fun awesomecv(init: ResumeBuilder<AwesomeCVSectionContentBuilder>.() -> Unit) = ResumeBuilder(::AwesomeCVSectionContentBuilder).apply(init).build()

/**
 * Builder class for creating an AltaCV resume.
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
 * Builder class for creating the contents of a section.
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

    fun event(title: String, init: Entry.() -> Unit) {
        _contents += Entry.create(title, init)
    }

    fun paragraph(content: String) {
        _contents += Paragraph(content)
    }

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

class HonorListBuilder {
    private val _items = mutableListOf<HonorItem>()

    fun honor(award: String, event: String, location: String, date: String) {
        _items += HonorItem(award, event, location, date)
    }

    fun build() = _items.toList()
}
