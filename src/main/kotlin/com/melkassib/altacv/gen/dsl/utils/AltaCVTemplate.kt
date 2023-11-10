@file:JvmName("ResumeTemplate")

package com.melkassib.altacv.gen.dsl.utils

import com.melkassib.altacv.gen.dsl.domain.*

private val ALTACV_COPYRIGHT =
    """
    %%%%%%%%%%%%%%%%%
    % This is an sample CV template created using altacv.cls
    % (v1.7, 9 August 2023) written by LianTze Lim (liantze@gmail.com). Compiles with pdfLaTeX, XeLaTeX and LuaLaTeX.
    %
    %% It may be distributed and/or modified under the
    %% conditions of the LaTeX Project Public License, either version 1.3
    %% of this license or (at your option) any later version.
    %% The latest version of this license is in
    %%    http://www.latex-project.org/lppl.txt
    %% and version 1.3 or later is part of all distributions of LaTeX
    %% version 2003/12/01 or later.
    %%%%%%%%%%%%%%%%
    """.trimIndent()

private fun generateResumeLatex(resumeInfo: Resume) =
    """
    |${renderTemplatePreamble(resumeInfo.config)}
    |
    |\begin{document}
    |
    |${renderResumeHeader(resumeInfo.header)}
    |
    |\makecvheader
    |%% Depending on your tastes, you may want to make fonts of itemize environments slightly smaller
    |% \AtBeginEnvironment{itemize}{\small}
    |
    |%% Set the left/right column width ratio to 6:4.
    |\columnratio{${resumeInfo.config.columnRatio}}
    |
    |% Start a 2-column paracol. Both the left and right columns will automatically
    |% break across pages if things get too long.
    |\begin{paracol}{2}
    |
    |${renderSections(resumeInfo.sections.filter { it.position.column == 1 })}
    |
    |${
        if (resumeInfo.sections.any { it.position.column == 2 }) {
            """
            |%% Switch to the right column. This will now automatically move to the second
            |%% page if the content is too long.
            |\switchcolumn
            |
            |${renderSections(resumeInfo.sections.filter { it.position.column == 2 })}
            """.trimMargin()
        } else { "" }
    }
    |\end{paracol}
    |
    |\end{document}
    """.trimMargin()

private fun renderResumeColorTheme(resumeTheme: ColorPalette): String {
    val latexColorsDefinition = resumeTheme.values.joinToString("\n") {
        "\\definecolor{${it.colorName}}{HTML}{${it.colorHexValue}}"
    }

    val latexColorAliasesDefinition = resumeTheme.entries.joinToString("\n") {
        "\\colorlet{${it.key.value}}{${it.value.colorName}}"
    }

    return "$latexColorsDefinition\n\n$latexColorAliasesDefinition"
}

@Suppress("LongMethod")
private fun renderTemplatePreamble(resumeConfig: ResumeConfig): String {
    val withNormalPhoto = when (resumeConfig.photoShape) {
        PhotoShape.NORMAL -> ",normalphoto"
        PhotoShape.CIRCLE -> ""
    }

    return """
    |$ALTACV_COPYRIGHT
    |
    |%% Use the "normalphoto" option if you want a normal photo instead of cropped to a circle
    |% \documentclass[10pt,a4paper,normalphoto]{altacv}
    |
    |\documentclass[10pt,a4paper,ragged2e,withhyper$withNormalPhoto]{altacv}
    |%% AltaCV uses the fontawesome5 and packages.
    |%% See http://texdoc.net/pkg/fontawesome5 for full list of symbols.
    |
    |% Change the page layout if you need to
    |\geometry{left=1.25cm,right=1.25cm,top=1.5cm,bottom=1.5cm,columnsep=1.2cm}
    |
    |% The paracol package lets you typeset columns of text in parallel
    |\usepackage{paracol}
    |\usepackage{hyperref}
    |
    |% Change the font if you want to, depending on whether
    |% you're using pdflatex or xelatex/lualatex
    |% WHEN COMPILING WITH XELATEX PLEASE USE
    |% xelatex -shell-escape -output-driver="xdvipdfmx -z 0" sample.tex
    |\ifxetexorluatex
    |% If using xelatex or lualatex:
    |\usepackage{fontspec}
    |\setmainfont[
    |    Path = fonts/Roboto_Slab/,
    |    UprightFont = RobotoSlab-VariableFont_wght.ttf
    |]{Roboto Slab}
    |\setsansfont[
    |    Path = fonts/Lato/,
    |    UprightFont = Lato-Regular.ttf
    |]{Lato}
    |\renewcommand{\familydefault}{\sfdefault}
    |\else
    |% If using pdflatex:
    |\usepackage[rm]{roboto}
    |\usepackage[defaultsans]{lato}
    |% \usepackage{sourcesanspro}
    |\renewcommand{\familydefault}{\sfdefault}
    |\fi
    |
    |% Change the colours if you want to
    |${renderResumeColorTheme(resumeConfig.theme)}
    |
    |% Change some fonts, if necessary
    |\renewcommand{\namefont}{\Huge\rmfamily\bfseries}
    |\renewcommand{\personalinfofont}{\footnotesize}
    |\renewcommand{\cvsectionfont}{\LARGE\rmfamily\bfseries}
    |\renewcommand{\cvsubsectionfont}{\large\bfseries}
    |
    |% Change the bullets for itemize and rating marker
    |% for \cvskill if you want to
    |\renewcommand{\cvItemMarker}{{\small\textbullet}}
    |\renewcommand{\cvRatingMarker}{\faCircle}
    |% ...and the markers for the date/location for \cvevent
    |\renewcommand{\cvDateMarker}{\faCalendar*[regular]}
    |\renewcommand{\cvLocationMarker}{\faMapMarker*}
    |
    |% To display language skill fluency as string rather than stars
    |\newcommand{\cvskillstr}[2]{%
    |    \textcolor{emphasis}{\textbf{#1}}\hfill
    |    \textbf{\color{body}#2}\par
    |}
    |
    |% If your CV/résumé is in a language other than English,
    |% then you probably want to change these so that when you
    |% copy-paste from the PDF or run pdftotext, the location
    |% and date marker icons for \cvevent will paste as correct
    |% translations. For example Spanish:
    |% \renewcommand{\locationname}{Ubicación}
    |% \renewcommand{\datename}{Fecha}
    """.trimMargin()
}

private fun renderResumeHeader(header: ResumeHeader): String {
    val photo = when (header.photo?.direction) {
        PhotoDirection.LEFT -> "\\photoL{${header.photo?.size}cm}{${header.photo?.path}}"
        PhotoDirection.RIGHT -> "\\photoR{${header.photo?.size}cm}{${header.photo?.path}}"
        else -> ""
    }

    return """
    |\name{${header.userInfo?.name?.escapeSpecialChars()}}
    |\tagline{${header.tagline.escapeSpecialChars()}}
    |%% You can add multiple photos on the left or right
    |$photo
    |% \photoL{2.5cm}{Yacht_High,Suitcase_High}
    |
    |\personalinfo{%
    |  ${header.userInfo?.personalInfo?.let { renderUserPersonalInfo(it) }}
    |
    |  % Not all of these are required!
    |  %\email{your_name@email.com}
    |  %\phone{000-00-0000}
    |  %\mailaddress{Address, Street, 00000 Country}
    |  %\location{Location, COUNTRY}
    |  %\homepage{www.homepage.com}
    |  %\twitter{@twitterhandle}
    |  %\linkedin{your_id}
    |  %\github{your_id}
    |  %\orcid{0000-0000-0000-0000}
    |
    |  %% You can add your own arbitrary detail with
    |  %% \printinfo{symbol}{detail}[optional hyperlink prefix]
    |  % \printinfo{\faPaw}{Hey ho!}[https://example.com/]
    |  %% Or you can declare your own field with
    |  %% \NewInfoFiled{fieldname}{symbol}[optional hyperlink prefix] and use it:
    |  % \NewInfoField{gitlab}{\faGitlab}[https://gitlab.com/]
    |  % \gitlab{your_id}
    |  %%
    |  %% For services and platforms like Mastodon where there isn't a
    |  %% straightforward relation between the user ID/nickname and the hyperlink,
    |  %% you can use \printinfo directly e.g.
    |  % \printinfo{\faMastodon}{@username@instace}[https://instance.url/@username]
    |  %% But if you absolutely want to create new dedicated info fields for
    |  %% such platforms, then use \NewInfoField* with a star:
    |  % \NewInfoField*{mastodon}{\faMastodon}
    |  %% then you can use \mastodon, with TWO arguments where the 2nd argument is
    |  %% the full hyperlink.
    |  % \mastodon{@username@instance}{https://instance.url/@username}
    |}
    """.trimMargin()
}

private fun renderUserPersonalInfo(personalInfo: UserPersonalInfo): String {
    return personalInfo.joinToString("\n  ") { userInfo ->
        val fieldName = userInfo.fieldName
        val fieldValue = userInfo.value

        if (fieldName in USER_CONTACT_FIELDS) {
            "\\$fieldName{$fieldValue}"
        } else {
            """
            \NewInfoField{$fieldName}{${userInfo.symbol}}[${userInfo.prefix}]
              \$fieldName{$fieldValue}
            """.trimIndent()
        }
    }
}

private fun renderSections(sections: List<Section>) =
    sections.filterNot { it.ignored }.sortedBy { it.position.order }.joinToString("\n\n") { section ->
        """
        |%${section.title.centered()}
        |\cvsection{${section.title}}
        |${section.contents.joinToString("\n") { it.render() }}
        |%${"-".repeat(TITLE_WIDTH)}
        """.trimMargin()
    }

fun Resume.toLaTeX() = generateResumeLatex(this)
