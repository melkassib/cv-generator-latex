@file:JvmName("ResumeHelper")

package com.melkassib.cvgenerator.altacv.utils

import com.melkassib.cvgenerator.altacv.domain.NoContent
import com.melkassib.cvgenerator.altacv.domain.SectionContent
import com.melkassib.cvgenerator.altacv.domain.SectionPosition

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
