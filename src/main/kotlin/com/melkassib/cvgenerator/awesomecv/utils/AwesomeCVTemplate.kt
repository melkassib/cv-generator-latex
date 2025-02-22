@file:JvmName("ResumeTemplate")

package com.melkassib.cvgenerator.awesomecv.utils

import com.melkassib.cvgenerator.awesomecv.domain.AwesomeCVResume
import com.melkassib.cvgenerator.awesomecv.domain.AwesomeCVUserPersonalInfo
import com.melkassib.cvgenerator.common.domain.AwesomeCVConfig
import com.melkassib.cvgenerator.common.domain.AwesomeCVHeader
import com.melkassib.cvgenerator.common.utils.renderSections

/**
 * Represents the copyright of the AwesomeCV template.
 */
private val AWESOME_CV_COPYRIGHT =
    """
    %!TEX TS-program = xelatex
    %!TEX encoding = UTF-8 Unicode
    % Awesome CV LaTeX Template for CV/Resume
    %
    % This template has been downloaded from:
    % https://github.com/posquit0/Awesome-CV
    %
    % Author:
    % Claud D. Park <posquit0.bj@gmail.com>
    % http://www.posquit0.com
    %
    % Template license:
    % CC BY-SA 4.0 (https://creativecommons.org/licenses/by-sa/4.0/)
    %
    """.trimIndent()

/**
 * Generates the complete LaTeX document for an AwesomeCV resume.
 *
 * @param resumeInfo The [AwesomeCVResume] object containing all resume information
 * @return The LaTeX document as a string
 */
internal fun generateResumeLatex(resumeInfo: AwesomeCVResume): String {
    return """
    |${renderTemplatePreamble(resumeInfo.config)}
    |
    |${renderResumeHeader(resumeInfo.header)}
    |
    |\begin{document}
    |
    |% Print the header with above personal information
    |% Give optional argument to change alignment(C: center, L: left, R: right)
    |\makecvheader[${resumeInfo.header.alignment.value}]
    |
    |% Print the footer with 3 arguments(<left>, <center>, <right>)
    |% Leave any of these blank if they are not needed
    |\makecvfooter
    |  {${resumeInfo.footer.left}}
    |  {${resumeInfo.footer.center}}
    |  {${resumeInfo.footer.right}}
    |
    |
    |%-------------------------------------------------------------------------------
    |%	CV/RESUME CONTENT
    |%-------------------------------------------------------------------------------
    |
    |${renderSections(resumeInfo.sections)}
    |
    |\end{document}
    """.trimMargin()
}

/**
 * Generates the LaTeX preamble containing document class settings and styling configurations (AwesomeCV template).
 *
 * @param resumeConfig The [AwesomeCVConfig] containing resume-wide settings
 * @return LaTeX preamble as a string with all necessary configurations
 */
internal fun renderTemplatePreamble(resumeConfig: AwesomeCVConfig): String {
    return """
    |$AWESOME_CV_COPYRIGHT
    |%-------------------------------------------------------------------------------
    |% CONFIGURATIONS
    |%-------------------------------------------------------------------------------
    |% A4 paper size by default, use 'letterpaper' for US letter
    |\documentclass[11pt, a4paper]{awesome-cv}
    |
    |% Configure page margins with geometry
    |\geometry{left=1.4cm, top=.8cm, right=1.4cm, bottom=1.8cm, footskip=.5cm}
    |
    |% Color for highlights
    |% Awesome Colors: awesome-emerald, awesome-skyblue, awesome-red, awesome-pink, awesome-orange
    |%                 awesome-nephritis, awesome-concrete, awesome-darknight
    |\colorlet{awesome}{${resumeConfig.colorTheme.theme}}
    |% Uncomment if you would like to specify your own color
    |% \definecolor{awesome}{HTML}{3E6D9C}
    |
    |% Colors for text
    |% Uncomment if you would like to specify your own color
    |% \definecolor{darktext}{HTML}{414141}
    |% \definecolor{text}{HTML}{333333}
    |% \definecolor{graytext}{HTML}{5D5D5D}
    |% \definecolor{lighttext}{HTML}{999999}
    |% \definecolor{sectiondivider}{HTML}{5D5D5D}
    |
    |% Set false if you don't want to highlight section with awesome color
    |\setbool{acvSectionColorHighlight}{${resumeConfig.isSectionHighlighted}}
    |
    |% If you would like to change the social information separator from a pipe (|) to something else
    |\renewcommand{\acvHeaderSocialSep}{\quad${resumeConfig.headerSocialSeparator}\quad}
    """.trimMargin()
}

/**
 * Generates the LaTeX code for the AwesomeCV resume header section.
 *
 * @param header The [AwesomeCVHeader] containing name, quote, photo, and personal information
 * @return LaTeX commands for rendering the resume header
 */
internal fun renderResumeHeader(header: AwesomeCVHeader): String {
    val photo = header.photo?.let {
        "\\photo[${it.shape.name.lowercase()},${it.edge.value.lowercase()},${it.direction.name.lowercase()}]{${it.path}}"
    } ?: "%\\photo[rectangle,edge,right]{./examples/profile}"

    val userPersonalInfo = header.userInfo?.personalInfo?.let {
        renderUserPersonalInfo(it)
    } ?: ""

    return """
     |%-------------------------------------------------------------------------------
     |%	PERSONAL INFORMATION
     |%	Comment any of the lines below if they are not required
     |%-------------------------------------------------------------------------------
     |% Available options: circle|rectangle,edge/noedge,left/right
     |$photo
     |
     |\name{${header.userInfo?.firstName ?: ""}}{${header.userInfo?.lastName ?: ""}}
     |$userPersonalInfo
     |
     |${if (header.quote.isEmpty()) "" else "\\quote{``${header.quote}''}"}
    """.trimMargin()
}

/**
 * Renders personal information fields in LaTeX format (AwesomeCV template).
 *
 * @param personalInfo The [AwesomeCVUserPersonalInfo] containing contact and social media information
 * @return LaTeX commands for displaying personal information fields
 */
internal fun renderUserPersonalInfo(personalInfo: AwesomeCVUserPersonalInfo): String {
    return personalInfo.joinToString("\n") { userInfo ->
        val fieldName = userInfo.fieldName
        val fieldValue = userInfo.value
        val fieldValueId = userInfo.valueId

        "\\$fieldName${if (userInfo.valueId.isNullOrEmpty()) "" else "{$fieldValueId}"}{$fieldValue}"
    }
}
