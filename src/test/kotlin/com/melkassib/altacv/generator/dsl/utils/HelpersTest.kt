package com.melkassib.altacv.generator.dsl.utils

import com.melkassib.altacv.generator.dsl.domain.section.Divider
import com.melkassib.altacv.generator.dsl.domain.section.Item
import com.melkassib.altacv.generator.dsl.domain.section.SectionPosition
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HelpersTest {

    @Test
    fun `separate content with a divider`() {
        val contents = listOf(
            Item("Item1"),
            Item("Item2"),
            Item("Item3")
        )

        val actual = contents.separateWith(Divider)
        val expected = listOf(
            Item("Item1"),
            Divider,
            Item("Item2"),
            Divider,
            Item("Item3")
        )

        assertThat(actual.size, equalTo(expected.size))
        assertThat(actual, equalTo(expected))
    }

    @Test
    fun `escape special characters`() {
        val s1 = "John% Doe & his_son".escapeSpecialChars()
        assertThat(s1, equalTo("John\\% Doe \\& his\\_son"))

        val s2 = "{\$attr:'%value$'}".escapeSpecialChars()
        assertThat(s2, equalTo("\\{\\\$attr:'\\%value\\$'\\}"))
    }

    @Test
    fun `center a string padded with a character`() {
        val s1 = "Example".centered(width = 30)
        val s2 = "Example".centered("+", 30)

        assertThat(s1, equalTo("-----------Example-----------"))
        assertThat(s2, equalTo("+++++++++++Example+++++++++++"))
    }

    @Test
    fun `create section positions`() {
        val p1 = firstColumn(2)
        val p2 = SectionPosition(1, 2)
        val p3 = secondColumn(5)
        val p4 = SectionPosition(2, 5)

        assertThat(p1.column, equalTo(1))
        assertThat(p1.order, equalTo(2))
        assertThat(p2.column, equalTo(1))
        assertThat(p2.order, equalTo(2))
        assertThat(p1, equalTo(p2))

        assertThat(p3.column, equalTo(2))
        assertThat(p3.order, equalTo(5))
        assertThat(p4.column, equalTo(2))
        assertThat(p4.order, equalTo(5))
        assertThat(p3, equalTo(p4))

        val ex = assertThrows<IllegalArgumentException> {
            SectionPosition(5, 6)
        }
        assertThat(ex.message, equalTo("column position: should be 1 or 2"))
    }
}
