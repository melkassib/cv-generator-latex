package com.melkassib.cvgenerator.awesomecv.utils

import com.melkassib.cvgenerator.altacv.serialization.buildAwesomeCVResumeFromJson
import com.melkassib.cvgenerator.awesomecv.domain.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.equalToCompressingWhiteSpace
import org.junit.jupiter.api.Test

class LaTeXOutputTest {

    @Test
    fun `render user personal info`() {
        val userPersonalInfo: AwesomeCVUserPersonalInfo =  linkedSetOf(
            Position("Developer"),
            MailAddress("Address, Street, City"),
            Phone("(+212) 000-000-000"),
            Email("hello@email.com"),
            HomePage("www.homepage.com"),
            Github("github-id"),
            LinkedIn("linkedin-id"),
            Gitlab("gitlab-id"),
            Twitter("@twitter"),
            Skype("skype-id"),
            Reddit("reddit-id"),
            Medium("medium-id"),
            StackOverFlow("SO-id", "SO-name"),
            GoogleScholar("googlescholar-id", "name-to-display"),
            ExtraInfo("extra information"),
        )

        val expectedUserInfoOutput = """
            \position{Developer}
            \address{Address, Street, City}
            \mobile{(+212) 000-000-000}
            \email{hello@email.com}
            \homepage{www.homepage.com}
            \github{github-id}
            \linkedin{linkedin-id}
            \gitlab{gitlab-id}
            \twitter{@twitter}
            \skype{skype-id}
            \reddit{reddit-id}
            \medium{medium-id}
            \stackoverflow{SO-id}{SO-name}
            \googlescholar{googlescholar-id}{name-to-display}
            \extrainfo{extra information}
        """.trimIndent()

        assertThat(renderUserPersonalInfo(userPersonalInfo), equalTo(expectedUserInfoOutput))
    }

    @Test
    fun `resume json to latex`() {
        val resumeJson = this.javaClass.getResource("/awesomecv/sample-resume.json")?.readText() ?: ""
        val expectedResumeLatex = this.javaClass.getResource("/awesomecv/sample-resume.tex")?.readText()

        val resume = buildAwesomeCVResumeFromJson(resumeJson)
        val actualResumeLatex = resume.toLaTeX()

        assertThat(actualResumeLatex, equalToCompressingWhiteSpace(expectedResumeLatex))
    }

    @Test
    fun `render empty resume`() {
        val emptyResume = awesomecv {}
        val expectedOutput = this.javaClass.getResource("/awesomecv/sample-resume-empty.tex")?.readText()?.replace("\r\n", "\n")
        val actualOutput = emptyResume.toLaTeX()

        assertThat(actualOutput, equalToCompressingWhiteSpace(expectedOutput))
    }
}
