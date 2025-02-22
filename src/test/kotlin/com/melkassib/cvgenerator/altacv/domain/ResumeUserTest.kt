package com.melkassib.cvgenerator.altacv.domain

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ResumeUserTest {

    @Test
    fun `create a user email field`() {
        val email = Email("your_name@email.com")

        assertThat(email, isA(UserInfoField::class.java))
        assertThat(email.fieldName, equalTo("email"))
        assertThat(email.symbol, equalTo("\\faAt"))
        assertThat(email.prefix, equalTo("mailto:"))
        assertThat(email.value, equalTo("your_name@email.com"))
    }

    @Test
    fun `create a user phone field`() {
        val phone = Phone("000-00-0000")

        assertThat(phone, isA(UserInfoField::class.java))
        assertThat(phone.fieldName, equalTo("phone"))
        assertThat(phone.symbol, equalTo("\\faPhone"))
        assertThat(phone.prefix, equalTo("tel:"))
        assertThat(phone.value, equalTo("000-00-0000"))
    }

    @Test
    fun `create a user mail address field`() {
        val mailAddress = MailAddress("Address, Street, 00000 Country")

        assertThat(mailAddress, isA(UserInfoField::class.java))
        assertThat(mailAddress.fieldName, equalTo("mailaddress"))
        assertThat(mailAddress.symbol, equalTo("\\faEnvelope"))
        assertThat(mailAddress.prefix, emptyString())
        assertThat(mailAddress.value, equalTo("Address, Street, 00000 Country"))
    }

    @Test
    fun `create a user location field`() {
        val location = Location("Location, COUNTRY")

        assertThat(location, isA(UserInfoField::class.java))
        assertThat(location.fieldName, equalTo("location"))
        assertThat(location.symbol, equalTo("\\cvLocationMarker"))
        assertThat(location.prefix, emptyString())
        assertThat(location.value, equalTo("Location, COUNTRY"))
    }

    @Test
    fun `create a user homepage field`() {
        val homepage = HomePage("www.homepage.com")

        assertThat(homepage, isA(UserInfoField::class.java))
        assertThat(homepage.fieldName, equalTo("homepage"))
        assertThat(homepage.symbol, equalTo("\\faGlobe"))
        assertThat(homepage.prefix, equalTo("https://"))
        assertThat(homepage.value, equalTo("www.homepage.com"))
    }

    @Test
    fun `create a user twitter field`() {
        val twitterField = Twitter("@twitterhandle")

        assertThat(twitterField, isA(UserInfoField::class.java))
        assertThat(twitterField.fieldName, equalTo("twitter"))
        assertThat(twitterField.symbol, equalTo("\\faTwitter"))
        assertThat(twitterField.prefix, equalTo("https://twitter.com/"))
        assertThat(twitterField.value, equalTo("@twitterhandle"))
    }

    @Test
    fun `create a user linkedin field`() {
        val linkedIn = LinkedIn("your_id")

        assertThat(linkedIn, isA(UserInfoField::class.java))
        assertThat(linkedIn.fieldName, equalTo("linkedin"))
        assertThat(linkedIn.symbol, equalTo("\\faLinkedin"))
        assertThat(linkedIn.prefix, equalTo("https://linkedin.com/in/"))
        assertThat(linkedIn.value, equalTo("your_id"))
    }

    @Test
    fun `create a user github field`() {
        val github = Github("your_id")

        assertThat(github, isA(UserInfoField::class.java))
        assertThat(github.fieldName, equalTo("github"))
        assertThat(github.symbol, equalTo("\\faGithub"))
        assertThat(github.prefix, equalTo("https://github.com/"))
        assertThat(github.value, equalTo("your_id"))
    }

    @Test
    fun `create a user orcid field`() {
        val orcid = Orcid("0000-0000-0000-0000")

        assertThat(orcid, isA(UserInfoField::class.java))
        assertThat(orcid.fieldName, equalTo("orcid"))
        assertThat(orcid.symbol, equalTo("\\faOrcid"))
        assertThat(orcid.prefix, equalTo("https://orcid.org/"))
        assertThat(orcid.value, equalTo("0000-0000-0000-0000"))

        val ex = assertThrows<IllegalArgumentException> {
            Orcid("0000")
        }
        assertThat(ex.message, equalTo("Invalid ORCID: 0000. Expected format: dddd-dddd-dddd-dddd"))
    }

    @Test
    fun `create a user custom field`() {
        val gitlabField = UserInfoField(
            "gitlab",
            "\\faGitlab",
            "https://gitlab.com/",
            "your_id"
        )

        assertThat(gitlabField, isA(UserInfoField::class.java))
        assertThat(gitlabField.fieldName, equalTo("gitlab"))
        assertThat(gitlabField.symbol, equalTo("\\faGitlab"))
        assertThat(gitlabField.prefix, equalTo("https://gitlab.com/"))
        assertThat(gitlabField.value, equalTo("your_id"))
    }

    @Test
    fun `create a user object`() {
        val personalInfo = setOf(
            Email("your_name@email.com"),
            Phone("000-00-0000"),
            MailAddress("Address, Street, 00000 Country"),
            Location("Location, COUNTRY")
        )

        val user = AltaCVUserInfo("Your Name Here", personalInfo)

        assertThat(user, notNullValue())
        assertThat(user.name, equalTo("Your Name Here"))
        assertThat(user.personalInfo, everyItem(isA(UserInfoField::class.java)))
        assertThat(user.personalInfo, hasSize(4))
        assertThat(user.personalInfo, equalTo(personalInfo))
    }
}
