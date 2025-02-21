@file:JvmName("ResumeHelper")

package com.melkassib.cvgenerator.common.utils

import com.melkassib.cvgenerator.common.domain.NoContent
import com.melkassib.cvgenerator.common.domain.Section
import com.melkassib.cvgenerator.common.domain.SectionContent
import com.melkassib.cvgenerator.common.domain.SectionPosition

/**
 * The width of the title.
 */
internal const val TITLE_WIDTH = 80

/**
 * Separates the contents of a list with a separator element.
 *
 * @param separator The [SectionContent] element to insert between list items
 * @return A new list containing the original elements with separators inserted between them.
 *         Returns the original list if [separator] is [NoContent]
 */
internal fun List<SectionContent>.separateWith(separator: SectionContent): List<SectionContent> {
    if (separator == NoContent) {
        return this
    }

    val result = mutableListOf<SectionContent>()
    this.forEachIndexed { index, sectionContent ->
        result.add(sectionContent)
        if (index < size - 1) {
            result.add(separator)
        }
    }

    return result
}

/**
 * Creates a [SectionPosition] for the first column with specified order.
 *
 * @param order The vertical ordering position within the column
 * @return A [SectionPosition] instance representing the first column position
 */
fun firstColumn(order: Int) = SectionPosition(1, order)

/**
 * Creates a [SectionPosition] for the second column with specified order.
 *
 * @param order The vertical ordering position within the column
 * @return A [SectionPosition] instance representing the second column position
 */
fun secondColumn(order: Int) = SectionPosition(2, order)

/**
 * Renders resume sections in LaTeX format.
 *
 * @param sections List of [Section] objects to be rendered
 * @return LaTeX code for all sections, sorted by position and filtered for ignored sections
 */
internal fun renderSections(sections: List<Section>) =
    sections.filterNot { it.ignored }.sortedBy { it.position.order }.joinToString("\n\n") { section ->
        """
        |%${section.title.centered()}
        |\cvsection{${section.title.escapeSpecialChars()}}
        |
        |${section.contents.joinToString("\n") { it.render() }}
        |%${"-".repeat(TITLE_WIDTH)}
        """.trimMargin()
    }

/**
 * Centers a string by padding it with repeated characters on both sides.
 *
 * @param char The character to use for padding (defaults to "-")
 * @param width The total desired width of the resulting string (defaults to [TITLE_WIDTH])
 * @return A new string with the original content centered and padded with [char]
 */
internal fun String.centered(char: String = "-", width: Int = TITLE_WIDTH): String {
    val leftPadding = (width - length) / 2
    return char.repeat(leftPadding) + this + char.repeat(leftPadding)
}

/**
 * Escapes LaTeX special characters in a string to ensure proper rendering.
 *
 * @return A new string with LaTeX special characters properly escaped
 */
internal fun String.escapeSpecialChars() =
    this.replace("_", "\\_")
        .replace("#", "\\#")
        .replace("%", "\\%")
        .replace("&", "\\&")
        .replace("$", "\\$")
        .replace("{", "\\{")
        .replace("}", "\\}")
        .replace("^", "\\textasciicircum")
        .replace("~", "\\textasciitilde")
