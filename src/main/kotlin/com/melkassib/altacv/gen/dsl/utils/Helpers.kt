@file:JvmName("ResumeHelper")

package com.melkassib.altacv.gen.dsl.utils

import com.melkassib.altacv.gen.dsl.domain.NoContent
import com.melkassib.altacv.gen.dsl.domain.SectionContent
import com.melkassib.altacv.gen.dsl.domain.SectionPosition

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

internal fun String.centered(char: String = "-", width: Int = TITLE_WIDTH): String {
    val leftPadding = (width - length) / 2
    return char.repeat(leftPadding) + this + char.repeat(leftPadding)
}

fun firstColumn(order: Int) = SectionPosition(1, order)
fun secondColumn(order: Int) = SectionPosition(2, order)

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
