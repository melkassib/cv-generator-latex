@file:JvmName("ResumeConstants")

package com.melkassib.altacv.gen.dsl.utils

import com.melkassib.altacv.gen.dsl.domain.RColor
import com.melkassib.altacv.gen.dsl.domain.RColorAlias
import java.time.format.DateTimeFormatter

typealias ColorPalette = Map<RColorAlias, RColor>

internal val USER_CONTACT_FIELDS =
    setOf("email", "phone", "mailaddress", "location", "homepage", "twitter", "linkedin", "github", "orcid")

internal const val TITLE_WIDTH = 80

object PredefinedColorPalette {
    @JvmField
    val THEME1: ColorPalette = mapOf(
        RColorAlias.TAGLINE to RColor.PASTEL_RED,
        RColorAlias.HEADING_RULE to RColor.GOLDEN_EARTH,
        RColorAlias.HEADING to RColor.DARK_PASTEL_RED,
        RColorAlias.ACCENT to RColor.PASTEL_RED,
        RColorAlias.EMPHASIS to RColor.SLATE_GREY,
        RColorAlias.BODY to RColor.LIGHT_GREY
    )

    @JvmField
    val THEME2: ColorPalette = mapOf(
        RColorAlias.TAGLINE to RColor.VIVID_PURPLE,
        RColorAlias.HEADING_RULE to RColor.VIVID_PURPLE,
        RColorAlias.HEADING to RColor.VIVID_PURPLE,
        RColorAlias.ACCENT to RColor.VIVID_PURPLE,
        RColorAlias.EMPHASIS to RColor.SLATE_GREY,
        RColorAlias.BODY to RColor.LIGHT_GREY
    )

    @JvmField
    val THEME3: ColorPalette = mapOf(
        RColorAlias.TAGLINE to RColor.PASTEL_RED,
        RColorAlias.HEADING_RULE to RColor.GOLDEN_EARTH,
        RColorAlias.HEADING to RColor.SEPIA,
        RColorAlias.ACCENT to RColor.MULBERRY,
        RColorAlias.EMPHASIS to RColor.SLATE_GREY,
        RColorAlias.BODY to RColor.LIGHT_GREY
    )
}

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
}

internal object SectionEventDuration {
    val DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val RENDER_DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
}
