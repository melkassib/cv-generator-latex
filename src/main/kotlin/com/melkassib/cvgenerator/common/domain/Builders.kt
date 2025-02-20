package com.melkassib.cvgenerator.common.domain

import com.melkassib.cvgenerator.altacv.domain.NoContent
import com.melkassib.cvgenerator.altacv.domain.Section
import com.melkassib.cvgenerator.altacv.domain.SectionContent
import com.melkassib.cvgenerator.altacv.domain.SectionPosition
import com.melkassib.cvgenerator.altacv.utils.separateWith

fun interface SectionContentBuilder {
    fun build(): List<SectionContent>
}

class SectionListBuilder<T : SectionContentBuilder>(private val contentFactory: () -> T) {
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
    fun section(
        title: String,
        position: SectionPosition = SectionPosition(1, 1),
        separator: SectionContent = NoContent,
        ignored: Boolean = false,
        init: SectionBuilder<T>.() -> Unit
    ) {
        val contents = SectionBuilder(contentFactory).apply(init).build().separateWith(separator)
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
class SectionBuilder<T : SectionContentBuilder>(private val contentFactory: () -> T) {
    private val _contents = mutableListOf<SectionContent>()

    /**
     * Adds the contents of the section.
     *
     * @param init The initialization block for the contents of the section.
     */
    fun contents(init: T.() -> Unit) {
        _contents += contentFactory().apply(init).build()
    }

    /**
     * Builds the contents of the section.
     *
     * @return The built contents of the section.
     */
    fun build() = _contents.toList()
}
