package com.melkassib.cvgenerator.awesomecv.domain

import com.melkassib.cvgenerator.altacv.domain.Section
import com.melkassib.cvgenerator.awesomecv.utils.generateResumeLatex
import com.melkassib.cvgenerator.common.domain.AwesomeCVConfig
import com.melkassib.cvgenerator.common.domain.AwesomeCVFooter
import com.melkassib.cvgenerator.common.domain.AwesomeCVHeader
import com.melkassib.cvgenerator.common.domain.Resume
import com.melkassib.cvgenerator.common.utils.TITLE_WIDTH
import com.melkassib.cvgenerator.common.utils.centered

enum class PhotoShape {
    CIRCLE,
    RECTANGLE
}

enum class PhotoDirection {
    LEFT,
    RIGHT
}

enum class PhotoEdge(val value: String) {
    EDGE("edge"),
    NO_EDGE("noedge")
}

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

enum class HeaderAlignment(val value: String) {
    CENTER("C"),
    LEFT("L"),
    RIGHT("R")
}

data class Photo(
    val shape: PhotoShape = PhotoShape.RECTANGLE,
    val edge: PhotoEdge = PhotoEdge.EDGE,
    val direction: PhotoDirection = PhotoDirection.RIGHT,
    val path: String = ""
)

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
