@file:JvmName("Resume")

package com.melkassib.cvgenerator.altacv.domain

import com.fasterxml.jackson.annotation.JsonValue
import com.melkassib.cvgenerator.altacv.utils.generateResumeLatex
import com.melkassib.cvgenerator.common.domain.*
import com.melkassib.cvgenerator.common.domain.Section
import com.melkassib.cvgenerator.common.utils.TITLE_WIDTH
import com.melkassib.cvgenerator.common.utils.centered

/**
 * Represents color aliases used in the resume theme.
 * Each alias maps to a specific color role in the resume's design.
 *
 * @property value The string value used for JSON serialization
 */
enum class RColorAlias(@JsonValue val value: String) {
    TAGLINE("tagline"),
    HEADING_RULE("headingrule"),
    HEADING("heading"),
    ACCENT("accent"),
    EMPHASIS("emphasis"),
    BODY("body")
}

/**
 * Represents a color definition with a name and hex value.
 *
 * @property colorName The display name of the color
 * @property colorHexValue The hex code value of the color without # prefix
 */
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

/**
 * Defines the shape options for the photo in the resume.
 * Can be either CIRCLE or NORMAL (rectangular).
 */
enum class PhotoShape {
    CIRCLE,
    NORMAL
}

/**
 * Represents a photo to be included in the resume.
 *
 * @property size The size/dimensions of the photo
 * @property path The file path to the photo
 * @property direction The alignment direction of the photo (defaults to RIGHT)
 */
@JvmRecord
data class Photo @JvmOverloads constructor(
    val size: Double,
    val path: String,
    val direction: PhotoDirection = PhotoDirection.RIGHT
)

/**
 * Represents the main resume object.
 *
 * @property config Configuration options for the resume layout and styling
 * @property header Header section containing tagline, user info, and photo
 * @property sections List of sections containing resume content
 */
class AltaCVResume @JvmOverloads constructor(
    config: AltaCVConfig = AltaCVConfig(),
    header: AltaCVHeader = AltaCVHeader(),
    sections: List<Section> = listOf()
) : Resume<AltaCVConfig, AltaCVHeader, NoFooter>(config, header, NoFooter, sections) {
    /**
     * Extension function to convert a [Resume] object to LaTeX format.
     *
     * @return Complete LaTeX document as a string
     */
    override fun toLaTeX() = generateResumeLatex(this)

    /**
     * Prints the sections of the resume in a formatted string.
     *
     * @return A string representation of the sections
     */
    private fun printSections(): String {
        val sectionPerLine = sections.joinToString("\n  ")
        return if (sections.isEmpty()) "" else "\n  $sectionPerLine\n"
    }

    override fun toString() =
        """
        |${"ResumeInfo".centered()}
        |config   = $config
        |header   = $header
        |sections = [${printSections()}]
        |${"-".repeat(TITLE_WIDTH)}
        """.trimMargin()
}
