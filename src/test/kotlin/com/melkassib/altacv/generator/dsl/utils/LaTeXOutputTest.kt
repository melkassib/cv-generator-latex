package com.melkassib.altacv.generator.dsl.utils

import com.melkassib.altacv.generator.dsl.domain.*
import com.melkassib.altacv.generator.dsl.domain.builders.altacv
import com.melkassib.altacv.generator.dsl.domain.section.*
import com.melkassib.altacv.generator.dsl.domain.section.EventPeriodString.Companion.eventDurationStr
import com.melkassib.altacv.generator.dsl.domain.userInfo.*
import com.melkassib.altacv.generator.dsl.serialization.buildResumeFromJson
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class LaTeXOutputTest {

    @Test
    fun `render user personal info`() {
        val userPersonalInfo: UserPersonalInfo = setOf(
            EmailField("your_name@email.com"),
            PhoneField("000-00-0000"),
            MailAddressField("Address, Street, 00000 Country"),
            LocationField("Location, Country"),
            HomePageField("www.homepage.com"),
            TwitterField("@twitterhandle"),
            GithubField("your_id"),
            LinkedinField("your_id"),
            OrcidField("0000-0000-0000-0000"),
            UserInfoField(
                "gitlab",
                "\\faGitlab",
                "https://gitlab.com/",
                "your_id"
            )
        )

        val expectedUserInfoOutput = """
            \email{your_name@email.com}
              \phone{000-00-0000}
              \mailaddress{Address, Street, 00000 Country}
              \location{Location, Country}
              \homepage{www.homepage.com}
              \twitter{@twitterhandle}
              \github{your_id}
              \linkedin{your_id}
              \orcid{0000-0000-0000-0000}
              \NewInfoField{gitlab}{\faGitlab}[https://gitlab.com/]
              \gitlab{your_id}
        """.trimIndent()

        assertThat(renderUserPersonalInfo(userPersonalInfo), equalTo(expectedUserInfoOutput))
    }

    @Test
    fun `render resume header - photo on the right`() {
        val photo = Photo(2.8, "Profile.jpeg", PhotoDirection.RIGHT)
        val user = UserInfo(
            "You & Name % Here",
            setOf(
                EmailField("your_name@email.com"),
                PhoneField("000-00-0000")
            )
        )

        val header = ResumeHeader("This is a tagline", user, photo)

        val expectedHeaderOutput = """
            |\name{You \& Name \% Here}
            |\tagline{This is a tagline}
            |%% You can add multiple photos on the left or right
            |%\photoR{2.8cm}{Profile.jpeg}
            |%\photoL{2.5cm}{Yacht_High,Suitcase_High}
            |
            |\personalinfo{%
            |  \email{your_name@email.com}
            |  \phone{000-00-0000}
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
            |  %% \NewInfoField{fieldname}{symbol}[optional hyperlink prefix] and use it:
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

        assertThat(renderResumeHeader(header), equalTo(expectedHeaderOutput))
    }

    @Test
    fun `render resume header - photo on the left`() {
        val photo = Photo(2.8, "Profile.jpeg", PhotoDirection.LEFT)

        val header = ResumeHeader("This is a tagline", photo = photo)

        val expectedHeaderOutput = """
            |\name{}
            |\tagline{This is a tagline}
            |%% You can add multiple photos on the left or right
            |%\photoL{2.8cm}{Profile.jpeg}
            |%\photoL{2.5cm}{Yacht_High,Suitcase_High}
            |
            |\personalinfo{%
            |
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
            |  %% \NewInfoField{fieldname}{symbol}[optional hyperlink prefix] and use it:
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

        assertThat(renderResumeHeader(header), equalTo(expectedHeaderOutput))
    }

    @Test
    fun `render resume header - no photo and no user`() {
        val header = ResumeHeader("This is a tagline")
        val actualHeaderOutput = renderResumeHeader(header)

        val expectedHeaderOutput = """
            |\name{}
            |\tagline{This is a tagline}
            |%% You can add multiple photos on the left or right
            |
            |%\photoL{2.5cm}{Yacht_High,Suitcase_High}
            |
            |\personalinfo{%
            |
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
            |  %% \NewInfoField{fieldname}{symbol}[optional hyperlink prefix] and use it:
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

        assertThat(actualHeaderOutput, equalTo(expectedHeaderOutput))
    }

    @Test
    fun `render theme color 1`() {
        val actualThemeOutput = renderResumeColorTheme(PredefinedColorPalette.THEME1)
        val expectedThemeOutput = """
            \definecolor{PastelRed}{HTML}{8F0D0D}
            \definecolor{GoldenEarth}{HTML}{E7D192}
            \definecolor{DarkPastelRed}{HTML}{450808}
            \definecolor{SlateGrey}{HTML}{2E2E2E}
            \definecolor{LightGrey}{HTML}{666666}

            \colorlet{tagline}{PastelRed}
            \colorlet{headingrule}{GoldenEarth}
            \colorlet{heading}{DarkPastelRed}
            \colorlet{accent}{PastelRed}
            \colorlet{emphasis}{SlateGrey}
            \colorlet{body}{LightGrey}
        """.trimIndent()

        assertThat(actualThemeOutput, equalTo(expectedThemeOutput))
    }

    @Test
    fun `render theme color 2`() {
        val actualThemeOutput = renderResumeColorTheme(PredefinedColorPalette.THEME2)
        val expectedThemeOutput = """
            \definecolor{VividPurple}{HTML}{3E0097}
            \definecolor{SlateGrey}{HTML}{2E2E2E}
            \definecolor{LightGrey}{HTML}{666666}

            \colorlet{tagline}{VividPurple}
            \colorlet{headingrule}{VividPurple}
            \colorlet{heading}{VividPurple}
            \colorlet{accent}{VividPurple}
            \colorlet{emphasis}{SlateGrey}
            \colorlet{body}{LightGrey}
        """.trimIndent()

        assertThat(actualThemeOutput, equalTo(expectedThemeOutput))
    }

    @Test
    fun `render theme color 3`() {
        val actualThemeOutput = renderResumeColorTheme(PredefinedColorPalette.THEME3)
        val expectedThemeOutput = """
            \definecolor{PastelRed}{HTML}{8F0D0D}
            \definecolor{GoldenEarth}{HTML}{E7D192}
            \definecolor{Sepia}{HTML}{581C09}
            \definecolor{Mulberry}{HTML}{72243D}
            \definecolor{SlateGrey}{HTML}{2E2E2E}
            \definecolor{LightGrey}{HTML}{666666}

            \colorlet{tagline}{PastelRed}
            \colorlet{headingrule}{GoldenEarth}
            \colorlet{heading}{Sepia}
            \colorlet{accent}{Mulberry}
            \colorlet{emphasis}{SlateGrey}
            \colorlet{body}{LightGrey}
        """.trimIndent()

        assertThat(actualThemeOutput, equalTo(expectedThemeOutput))
    }

    @Test
    fun `render sections`() {
        val section1 = Section(
            "SectionA", firstColumn(1),
            listOf(
                Item("This is an item"),
                LatexContent("\\medskip"),
                Tag("This is a tag")
            )
        )

        val section2 = Section(
            "SectionB", firstColumn(2),
            listOf(
                Event.create("Job Title 1") {
                    holder = "Company 1"
                    location = "Location"
                    duration = eventDurationStr("Month XXXX", "Ongoing")
                    description = listOf(
                        Item("Job description 1"),
                        Item("Job description 2"),
                        Item("Job description 3", false)
                    )
                },
                Divider,
                Quote("This is a quote")
            )
        )

        val actualSectionsOutput = renderSections(listOf(section1, section2))

        val expectedSectionsOutput = """
            %------------------------------------SectionA------------------------------------
            \cvsection{SectionA}
            \item This is an item
            \medskip
            \cvtag{This is a tag}
            %--------------------------------------------------------------------------------

            %------------------------------------SectionB------------------------------------
            \cvsection{SectionB}
            \cvevent{Job Title 1}{Company 1}{Month XXXX -- Ongoing}{Location}
            \begin{itemize}
            \item Job description 1
            \item Job description 2
            \item[] Job description 3
            \end{itemize}

            \divider

            \begin{quote}
            ``This is a quote''
            \end{quote}
            %--------------------------------------------------------------------------------
        """.trimIndent()

        assertThat(actualSectionsOutput, equalTo(expectedSectionsOutput))
    }

    @Test
    fun `resume json to latex`() {
        val resumeJson = this.javaClass.getResource("/sample-resume.json")?.readText() ?: ""
        val expectedResumeLatex = this.javaClass.getResource("/sample-resume.tex")?.readText()

        val resume = buildResumeFromJson(resumeJson)
        val actualResumeLatex = resume.toLaTeX()

        assertThat(actualResumeLatex, equalToCompressingWhiteSpace(expectedResumeLatex))
    }

    @Test
    fun `render config with normal photo`() {
        val config = ResumeConfig(photoShape = PhotoShape.NORMAL)

        assertThat(renderTemplatePreamble(config), containsString(",normalphoto"))
    }

    @Test
    fun `render empty resume`() {
        val emptyResume = altacv {}
        val expectedOutput = this.javaClass.getResource("/sample-resume-empty.tex")?.readText()?.replace("\r\n", "\n")
        val actualOutput = emptyResume.toLaTeX()

        assertThat(actualOutput, equalToCompressingWhiteSpace(expectedOutput))
    }
}
