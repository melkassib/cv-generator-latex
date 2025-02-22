@file:JvmName("ResumeConstants")

package com.melkassib.cvgenerator.common.utils

import java.time.format.DateTimeFormatter

/**
 * JSON field names used in the resumes.
 */
internal object JsonFieldNames {
    const val TYPE = "type"
    const val CONTENT = "content"
    const val SKILL = "skill"
    const val FLUENCY = "fluency"
    const val RATING = "rating"
    const val START = "start"
    const val END = "end"
    const val FIELD_NAME = "fieldName"
    const val SYMBOL = "symbol"
    const val PREFIX = "prefix"
    const val VALUE = "value"
    const val VALUE_ID = "valueId"
}

/**
 * Date patterns used for event durations in the resumes.
 */
internal object SectionEventDuration {
    /**
     * Date pattern for parsing dates.
     */
    val DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Date pattern for rendering dates.
     */
    val RENDER_DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
}
