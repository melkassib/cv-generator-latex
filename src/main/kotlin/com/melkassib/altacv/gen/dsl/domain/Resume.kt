@file:JvmName("Resume")

package com.melkassib.altacv.gen.dsl.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.melkassib.altacv.gen.dsl.serialization.JSON_MAPPER
import com.melkassib.altacv.gen.dsl.utils.ColorPalette
import com.melkassib.altacv.gen.dsl.utils.PredefinedColorPalette
import com.melkassib.altacv.gen.dsl.utils.TITLE_WIDTH
import com.melkassib.altacv.gen.dsl.utils.centered

enum class RColorAlias(@JsonValue val value: String) {
    TAGLINE("tagline"),
    HEADING_RULE("headingrule"),
    HEADING("heading"),
    ACCENT("accent"),
    EMPHASIS("emphasis"),
    BODY("body")
}

data class RColor(val colorName: String, val colorHexValue: String) {
    companion object {
        val PASTEL_RED = RColor("PastelRed", "8F0D0D")
        val GOLDEN_EARTH = RColor("GoldenEarth", "E7D192")
        val DARK_PASTEL_RED = RColor("DarkPastelRed", "450808")
        val SLATE_GREY = RColor("SlateGrey", "2E2E2E")
        val LIGHT_GREY = RColor("LightGrey", "666666")
        val MULBERRY = RColor("Mulberry", "72243D")
        val VIVID_PURPLE = RColor("VividPurple", "3E0097")
        val SEPIA = RColor("Sepia", "581C09")
    }
}

enum class PhotoShape {
    CIRCLE,
    NORMAL
}

enum class PhotoDirection {
    LEFT,
    RIGHT
}

@JvmRecord
data class Photo @JvmOverloads constructor(
    val size: Double,
    val path: String,
    val direction: PhotoDirection = PhotoDirection.RIGHT
)

data class ResumeConfig @JvmOverloads constructor(
    @JvmField var columnRatio: Double = 0.6,
    @JvmField var photoShape: PhotoShape = PhotoShape.NORMAL,
    @JvmField var theme: ColorPalette = PredefinedColorPalette.THEME1
)

data class ResumeHeader @JvmOverloads constructor(
    @JvmField var tagline: String = "",
    @JvmField var userInfo: RUser? = null,
    @JvmField var photo: Photo? = null
)

@JvmRecord
data class Resume @JvmOverloads constructor(
    val config: ResumeConfig = ResumeConfig(),
    val header: ResumeHeader = ResumeHeader(),
    val sections: List<Section> = listOf()
) {
    fun toJson(): String = JSON_MAPPER.writeValueAsString(this)

    override fun toString() =
        """
        |${"ResumeInfo".centered()}
        |config   = $config
        |header   = $header
        |sections = [${printSections()}]
        |${"-".repeat(TITLE_WIDTH)}
        """.trimMargin()

    private fun printSections(): String {
        val sectionPerLine = sections.joinToString("\n  ")
        return if (sections.isEmpty()) "" else "\n  $sectionPerLine\n"
    }
}
