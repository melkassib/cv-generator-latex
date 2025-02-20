package com.melkassib.cvgenerator.common.utils

import com.melkassib.cvgenerator.altacv.domain.Section

/**
 * The width of the title.
 */
internal const val TITLE_WIDTH = 80

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
