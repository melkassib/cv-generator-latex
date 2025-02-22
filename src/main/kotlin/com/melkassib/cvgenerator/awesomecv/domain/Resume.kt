@file:JvmName("AwesomeCVResume")

package com.melkassib.cvgenerator.awesomecv.domain

import com.melkassib.cvgenerator.awesomecv.utils.generateResumeLatex
import com.melkassib.cvgenerator.common.domain.*
import com.melkassib.cvgenerator.common.utils.TITLE_WIDTH
import com.melkassib.cvgenerator.common.utils.centered

/**
 * Defines the shape options for the photo in the AwesomeCV resume.
 * Can be either CIRCLE or NORMAL (rectangular).
 */
enum class PhotoShape {
    CIRCLE,
    RECTANGLE
}

/**
 * Represents whether a photo has an edge or not.
 *
 * @property value String representation of the edge type.
 */
enum class PhotoEdge(val value: String) {
    EDGE("edge"),
    NO_EDGE("noedge")
}

/**
 * Represents different color themes available for the AwesomeCV template.
 *
 * @property theme The string identifier for the color theme.
 */
enum class ColorTheme(val theme: String) {
    EMERALD("awesome-emerald"),
    SKYBLUE("awesome-skyblue"),
    RED("awesome-red"),
    PINK("awesome-pink"),
    ORANGE("awesome-orange"),
    NEPHRITIS("awesome-nephritis"),
    CONCRETE("awesome-concrete"),
    DARKNIGHT("awesome-darknight")
}

/**
* Represents the alignment options for an AwesomeCV template's header.
*
* @property value The alignment shortcut value.
*/
enum class HeaderAlignment(val value: String) {
    CENTER("C"),
    LEFT("L"),
    RIGHT("R")
}

/**
 * Represents a photo to be included in the AwesomeCV resume.
 *
 * @property shape The shape of the photo.
 * @property edge The edge style of the photo.
 * @property direction The direction of the photo.
 * @property path The relative file path to the photo.
 */
data class Photo(
    val shape: PhotoShape = PhotoShape.RECTANGLE,
    val edge: PhotoEdge = PhotoEdge.EDGE,
    val direction: PhotoDirection = PhotoDirection.RIGHT,
    val path: String = ""
)

/**
 * Represents the main AwesomeCV resume object.
 *
 * @property config Configuration options for the resume layout and styling
 * @property header Header section containing tagline, user info, and photo
 * @property footer Footer section containing info displayed in the bottom of the resume
 * @property sections List of sections containing resume content
 */
class AwesomeCVResume @JvmOverloads constructor(
    config: AwesomeCVConfig = AwesomeCVConfig(),
    header: AwesomeCVHeader = AwesomeCVHeader(),
    footer: AwesomeCVFooter = AwesomeCVFooter(),
    sections: List<Section> = listOf()
) : Resume<AwesomeCVConfig, AwesomeCVHeader, AwesomeCVFooter>(config, header, footer, sections) {
    override fun toLaTeX(): String = generateResumeLatex(this)

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
        |footer   = $footer
        |sections = [${printSections()}]
        |${"-".repeat(TITLE_WIDTH)}
        """.trimMargin()
}
