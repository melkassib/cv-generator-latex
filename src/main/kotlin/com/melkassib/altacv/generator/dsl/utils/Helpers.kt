@file:JvmName("ResumeHelper")

package com.melkassib.altacv.generator.dsl.utils

import com.melkassib.altacv.generator.dsl.domain.section.NoContent
import com.melkassib.altacv.generator.dsl.domain.section.SectionContent
import com.melkassib.altacv.generator.dsl.domain.section.SectionPosition

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
