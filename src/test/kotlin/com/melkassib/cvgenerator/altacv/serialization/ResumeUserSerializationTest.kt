package com.melkassib.cvgenerator.altacv.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath
import com.melkassib.cvgenerator.altacv.domain.*
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

class ResumeUserSerializationTest {

    @Test
    fun `serialize user personal info fields`() {
        val email = Email("your_name@email.com")
        val phone = Phone("000-00-0000")
        val mailAddress = MailAddress("Address, Street, 00000 Country")
        val location = Location("Location, Country")
        val homepage = HomePage("www.homepage.com")
        val twitter = Twitter("@twitterhandle")
        val github = Github("your_id")
        val linkedin = LinkedIn("your_id")
        val orcid = Orcid("0000-0000-0000-0000")
        val gitlab = AltaCVUserInfoField(
            "gitlab",
            "\\faGitlab",
            "https://gitlab.com/",
            "your_id"
        )

        assertThat(email.toJson(), equalTo("""{"fieldName":"email","value":"your_name@email.com"}"""))
        assertThat(phone.toJson(), equalTo("""{"fieldName":"phone","value":"000-00-0000"}"""))
        assertThat(mailAddress.toJson(), equalTo("""{"fieldName":"mailaddress","value":"Address, Street, 00000 Country"}"""))
        assertThat(location.toJson(), equalTo("""{"fieldName":"location","value":"Location, Country"}"""))
        assertThat(homepage.toJson(), equalTo("""{"fieldName":"homepage","value":"www.homepage.com"}"""))
        assertThat(twitter.toJson(), equalTo("""{"fieldName":"twitter","value":"@twitterhandle"}"""))
        assertThat(github.toJson(), equalTo("""{"fieldName":"github","value":"your_id"}"""))
        assertThat(linkedin.toJson(), equalTo("""{"fieldName":"linkedin","value":"your_id"}"""))
        assertThat(orcid.toJson(), equalTo("""{"fieldName":"orcid","value":"0000-0000-0000-0000"}"""))
        assertThat(gitlab.toJson(), equalTo("""{"fieldName":"gitlab","symbol":"\\faGitlab","prefix":"https://gitlab.com/","value":"your_id"}"""))
    }

    @Test
    fun `deserialize user personal info fields`() {
        val emailJson = """{"fieldName":"email","value":"your_name@email.com"}"""
        val phoneJson = """{"fieldName":"phone","value":"000-00-0000"}"""
        val locationJson = """{"fieldName":"location","value":"Location, Country"}"""
        val mailAddressJson = """{"fieldName":"mailaddress","value":"Address, Street, 00000 Country"}"""
        val homePageJson = """{"fieldName":"homepage","value":"www.homepage.com"}"""
        val twitterJson = """{"fieldName":"twitter","value":"@twitterhandle"}"""
        val githubJson = """{"fieldName":"github","value":"your_id"}"""
        val linkedinJson = """{"fieldName":"linkedin","value":"your_id"}"""
        val orcidJson = """{"fieldName":"orcid","value":"0000-0000-0000-0000"}"""
        val gitlabJson = """{"fieldName":"gitlab","symbol":"\\faGitlab","prefix":"https://gitlab.com/","value":"your_id"}"""

        fun String.toField() = JSON_MAPPER.readValue<AltaCVUserInfoField>(this)

        assertThat(emailJson.toField(), equalTo(Email("your_name@email.com")))
        assertThat(phoneJson.toField(), equalTo(Phone("000-00-0000")))
        assertThat(mailAddressJson.toField(), equalTo(MailAddress("Address, Street, 00000 Country")))
        assertThat(locationJson.toField(), equalTo(Location("Location, Country")))
        assertThat(homePageJson.toField(), equalTo(HomePage("www.homepage.com")))
        assertThat(twitterJson.toField(), equalTo(Twitter("@twitterhandle")))
        assertThat(githubJson.toField(), equalTo(Github("your_id")))
        assertThat(linkedinJson.toField(), equalTo(LinkedIn("your_id")))
        assertThat(orcidJson.toField(), equalTo(Orcid("0000-0000-0000-0000")))
        assertThat(
            gitlabJson.toField(),
            equalTo(
                AltaCVUserInfoField(
                    "gitlab",
                    "\\faGitlab",
                    "https://gitlab.com/",
                    "your_id"
                )
            )
        )
    }

    @Test
    fun `serialize a user`() {
        val user = AltaCVUserInfo(
            "John Doe",
            setOf(
                Email("your_name@email.com"),
                Phone("000-00-0000")
            )
        )

        val userJson = JSON_MAPPER.writeValueAsString(user)
        assertThat(userJson, hasJsonPath("$.name", equalTo("John Doe")))
        assertThat(userJson, hasJsonPath("$.personalInfo.length()", equalTo(2)))
        assertThat(userJson, hasJsonPath("$.personalInfo[0].fieldName", equalTo("email")))
        assertThat(userJson, hasJsonPath("$.personalInfo[0].value", equalTo("your_name@email.com")))
        assertThat(userJson, hasJsonPath("$.personalInfo[1].fieldName", equalTo("phone")))
        assertThat(userJson, hasJsonPath("$.personalInfo[1].value", equalTo("000-00-0000")))
    }

    @Test
    fun `deserialize a user`() {
        val userJson = """
            {
                "name": "Your Name Here",
                "personalInfo": [
                    {
                        "fieldName": "orcid",
                        "value": "0000-0000-0000-0000"
                    },
                    {
                        "fieldName": "homepage",
                        "value": "www.homepage.com"
                    },
                    {
                        "fieldName": "github",
                        "value": "your_id"
                    }
                ]
            }
        """.trimIndent()

        val expectedPersonalInfo = setOf(
            Orcid("0000-0000-0000-0000"),
            HomePage("www.homepage.com"),
            Github("your_id")
        )

        val user = JSON_MAPPER.readValue<AltaCVUserInfo>(userJson)

        assertThat(user.name, equalTo("Your Name Here"))
        assertThat(user.personalInfo, hasSize(3))
        assertThat(user.personalInfo, equalTo(expectedPersonalInfo))
    }
}
