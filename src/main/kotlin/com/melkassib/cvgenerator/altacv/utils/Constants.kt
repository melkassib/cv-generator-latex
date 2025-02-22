@file:JvmName("ResumeConstants")

package com.melkassib.cvgenerator.altacv.utils

import com.melkassib.cvgenerator.altacv.domain.RColor
import com.melkassib.cvgenerator.altacv.domain.RColorAlias

/**
 * Represents a color palette mapping color aliases to colors.
 */
typealias ColorPalette = Map<RColorAlias, RColor>

/**
 * A set of user contact fields. Used in the AltaCV resume.
 */
internal val USER_CONTACT_FIELDS =
    setOf("email", "phone", "mailaddress", "location", "homepage", "twitter", "linkedin", "github", "orcid")

/**
 * Predefined color palettes for the AltaCV resume.
 */
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
