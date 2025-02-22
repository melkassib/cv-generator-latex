package com.melkassib.cvgenerator.awesomecv.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import com.melkassib.cvgenerator.awesomecv.domain.*
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

class ResumeUserSerializationTest {

    @Test
    fun `serialize user personal info fields`() {
        val email = Email("your_name@email.com")
        val phone = Phone("(+212) 000-000-000")
        val mailAddress = MailAddress("Address, Street, 00000 Country")
        val homepage = HomePage("www.homepage.com")
        val twitter = Twitter("@your_id")
        val github = Github("your_id")
        val gitlab = Gitlab("your_id")
        val skype = Skype("your_id")
        val medium = Medium("your_id")
        val reddit = Reddit("your_id")
        val position = Position("Developer")
        val extraInfo = ExtraInfo("extra information")
        val googleScholar = GoogleScholar("googlescholar-id", "name-to-display")
        val stackOverFlow = StackOverFlow("SO-id", "SO-name")
        val linkedin = LinkedIn("your_id")

        assertThat(email.toJson(), equalTo("""{"fieldName":"email","value":"your_name@email.com"}"""))
        assertThat(phone.toJson(), equalTo("""{"fieldName":"mobile","value":"(+212) 000-000-000"}"""))
        assertThat(mailAddress.toJson(), equalTo("""{"fieldName":"address","value":"Address, Street, 00000 Country"}"""))
        assertThat(homepage.toJson(), equalTo("""{"fieldName":"homepage","value":"www.homepage.com"}"""))
        assertThat(twitter.toJson(), equalTo("""{"fieldName":"twitter","value":"@your_id"}"""))
        assertThat(github.toJson(), equalTo("""{"fieldName":"github","value":"your_id"}"""))
        assertThat(linkedin.toJson(), equalTo("""{"fieldName":"linkedin","value":"your_id"}"""))
        assertThat(gitlab.toJson(), equalTo("""{"fieldName":"gitlab","value":"your_id"}"""))
        assertThat(skype.toJson(), equalTo("""{"fieldName":"skype","value":"your_id"}"""))
        assertThat(medium.toJson(), equalTo("""{"fieldName":"medium","value":"your_id"}"""))
        assertThat(reddit.toJson(), equalTo("""{"fieldName":"reddit","value":"your_id"}"""))
        assertThat(position.toJson(), equalTo("""{"fieldName":"position","value":"Developer"}"""))
        assertThat(extraInfo.toJson(), equalTo("""{"fieldName":"extrainfo","value":"extra information"}"""))
        assertThat(googleScholar.toJson(), equalTo("""{"fieldName":"googlescholar","value":"name-to-display","valueId":"googlescholar-id"}"""))
        assertThat(stackOverFlow.toJson(), equalTo("""{"fieldName":"stackoverflow","value":"SO-name","valueId":"SO-id"}"""))
    }

    @Test
    fun `deserialize user personal info fields`() {
        val emailJson = """{"fieldName":"email","value":"your_name@email.com"}"""
        val phoneJson = """{"fieldName":"mobile","value":"(+212) 000-000-000"}"""
        val mailAddressJson = """{"fieldName":"address","value":"Address, Street, 00000 Country"}"""
        val homePageJson = """{"fieldName":"homepage","value":"www.homepage.com"}"""
        val twitterJson = """{"fieldName":"twitter","value":"@your_id"}"""
        val githubJson = """{"fieldName":"github","value":"your_id"}"""
        val linkedinJson = """{"fieldName":"linkedin","value":"your_id"}"""
        val gitlabJson = """{"fieldName":"gitlab","value":"your_id"}"""
        val skypeJson = """{"fieldName":"skype","value":"your_id"}"""
        val mediumJson = """{"fieldName":"medium","value":"your_id"}"""
        val redditJson = """{"fieldName":"reddit","value":"your_id"}"""
        val positionJson = """{"fieldName":"position","value":"Developer"}"""
        val extraInfoJson = """{"fieldName":"extrainfo","value":"extra information"}"""
        val googleScholarJson = """{"fieldName":"googlescholar","value":"name-to-display","valueId":"googlescholar-id"}"""
        val stackOverFlowJson = """{"fieldName":"stackoverflow","value":"SO-name","valueId":"SO-id"}"""

        fun String.toField() = JSON_MAPPER.readValue<UserInfoField>(this)

        assertThat(emailJson.toField(), equalTo(Email("your_name@email.com")))
        assertThat(phoneJson.toField(), equalTo(Phone("(+212) 000-000-000")))
        assertThat(mailAddressJson.toField(), equalTo(MailAddress("Address, Street, 00000 Country")))
        assertThat(homePageJson.toField(), equalTo(HomePage("www.homepage.com")))
        assertThat(twitterJson.toField(), equalTo(Twitter("@your_id")))
        assertThat(githubJson.toField(), equalTo(Github("your_id")))
        assertThat(linkedinJson.toField(), equalTo(LinkedIn("your_id")))
        assertThat(gitlabJson.toField(), equalTo(Gitlab("your_id")))
        assertThat(skypeJson.toField(), equalTo(Skype("your_id")))
        assertThat(mediumJson.toField(), equalTo(Medium("your_id")))
        assertThat(redditJson.toField(), equalTo(Reddit("your_id")))
        assertThat(positionJson.toField(), equalTo(Position("Developer")))
        assertThat(extraInfoJson.toField(), equalTo(ExtraInfo("extra information")))
        assertThat(googleScholarJson.toField(), equalTo(GoogleScholar("googlescholar-id", "name-to-display")))
        assertThat(stackOverFlowJson.toField(), equalTo(StackOverFlow("SO-id", "SO-name")))
    }

    @Test
    fun `serialize a user`() {
        val user = AwesomeCVUserInfo(
            "John",
            "Dupont",
            linkedSetOf(
                Email("your_name@email.com"),
                Phone("000-00-0000")
            )
        )

        val userJson = JSON_MAPPER.writeValueAsString(user)
        assertThat(userJson, hasJsonPath("$.firstName", equalTo("John")))
        assertThat(userJson, hasJsonPath("$.lastName", equalTo("Dupont")))
        assertThat(userJson, hasJsonPath("$.personalInfo.length()", equalTo(2)))
        assertThat(userJson, hasJsonPath("$.personalInfo[0].fieldName", equalTo("email")))
        assertThat(userJson, hasJsonPath("$.personalInfo[0].value", equalTo("your_name@email.com")))
        assertThat(userJson, hasJsonPath("$.personalInfo[1].fieldName", equalTo("mobile")))
        assertThat(userJson, hasJsonPath("$.personalInfo[1].value", equalTo("000-00-0000")))
    }

    @Test
    fun `deserialize a user`() {
        val userJson = """
            {
                "firstName": "John",
                "lastName": "Dupont",
                "personalInfo": [
                    {
                        "value": "Developer",
                        "fieldName": "position"
                    },
                    {
                        "value": "Address, Street, City",
                        "fieldName": "address"
                    },
                    {
                        "value": "(+212) 000-000-000",
                        "fieldName": "mobile"
                    }
                ]
            }
        """.trimIndent()

        val expectedPersonalInfo = setOf(
            Position("Developer"),
            MailAddress("Address, Street, City"),
            Phone("(+212) 000-000-000")
        )

        val user = JSON_MAPPER.readValue<AwesomeCVUserInfo>(userJson)

        assertThat(user.firstName, equalTo("John"))
        assertThat(user.lastName, equalTo("Dupont"))
        assertThat(user.personalInfo, hasSize(3))
        assertThat(user.personalInfo, equalTo(expectedPersonalInfo))
    }
}
