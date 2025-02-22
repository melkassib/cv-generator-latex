package com.melkassib.cvgenerator.altacv.domain

import com.melkassib.cvgenerator.common.domain.Item
import com.melkassib.cvgenerator.common.domain.LatexContent
import com.melkassib.cvgenerator.common.domain.Section
import com.melkassib.cvgenerator.common.domain.SectionContent
import com.melkassib.cvgenerator.common.domain.SectionPosition
import com.melkassib.cvgenerator.common.domain.Tag
import com.melkassib.cvgenerator.common.utils.firstColumn
import com.melkassib.cvgenerator.common.utils.secondColumn
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SectionTest {

    @Test
    fun `create a section without contents`() {
        val section = Section("Experience", firstColumn(3))

        assertThat(section, notNullValue())
        assertThat(section.title, equalTo("Experience"))
        assertThat(section.position, notNullValue())
        assertThat(section.position.column, equalTo(1))
        assertThat(section.position.order, equalTo(3))
        assertThat(section.ignored, equalTo(false))
        assertThat(section.contents, emptyIterableOf(SectionContent::class.java))
    }

    @Test
    fun `create a section with contents`() {
        val contents = listOf(
            Item("This is an item"),
            LatexContent("\\medskip"),
            Tag("This is a tag")
        )

        val section = Section("Example", secondColumn(3), contents)

        assertThat(section, notNullValue())
        assertThat(section.title, equalTo("Example"))
        assertThat(section.position, notNullValue())
        assertThat(section.position.column, equalTo(2))
        assertThat(section.position.order, equalTo(3))
        assertThat(section.ignored, equalTo(false))
        assertThat(section.contents, hasSize(3))
        assertThat(section.contents, everyItem(isA(SectionContent::class.java)))
    }

    @Test
    fun `create an ignored section`() {
        val section = Section("Example", secondColumn(1), ignored = true)

        assertThat(section, notNullValue())
        assertThat(section.title, equalTo("Example"))
        assertThat(section.position, notNullValue())
        assertThat(section.position.column, equalTo(2))
        assertThat(section.position.order, equalTo(1))
        assertThat(section.ignored, equalTo(true))
        assertThat(section.contents, emptyIterableOf(SectionContent::class.java))
    }

    @Test
    fun `allow only creation of positions for first and second columns`() {
        val ex = assertThrows<IllegalArgumentException> {
            SectionPosition(5, 6)
        }

        assertThat(ex.message, equalTo("column position: should be 1 or 2"))
    }
}
