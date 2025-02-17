package com.melkassib.cvgenerator.altacv.domain

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ResumeUserTest {

    @Test
    fun `create a user email field`() {
        val email = EmailField("your_name@email.com")

        assertThat(email, isA(UserInfoField::class.java))
        assertThat(email.fieldName, equalTo("email"))
        assertThat(email.symbol, equalTo("\\faAt"))
        assertThat(email.prefix, equalTo("mailto:"))
        assertThat(email.value, equalTo("your_name@email.com"))
    }

    @Test
    fun `create a user phone field`() {
        val phone = PhoneField("000-00-0000")

        assertThat(phone, isA(UserInfoField::class.java))
        assertThat(phone.fieldName, equalTo("phone"))
        assertThat(phone.symbol, equalTo("\\faPhone"))
        assertThat(phone.prefix, equalTo("tel:"))
        assertThat(phone.value, equalTo("000-00-0000"))
    }

    @Test
    fun `create a user mail address field`() {
        val mailAddress = MailAddressField("Address, Street, 00000 Country")

        assertThat(mailAddress, isA(UserInfoField::class.java))
        assertThat(mailAddress.fieldName, equalTo("mailaddress"))
        assertThat(mailAddress.symbol, equalTo("\\faEnvelope"))
        assertThat(mailAddress.prefix, emptyString())
        assertThat(mailAddress.value, equalTo("Address, Street, 00000 Country"))
    }

    @Test
    fun `create a user location field`() {
        val location = LocationField("Location, COUNTRY")

        assertThat(location, isA(UserInfoField::class.java))
        assertThat(location.fieldName, equalTo("location"))
        assertThat(location.symbol, equalTo("\\cvLocationMarker"))
        assertThat(location.prefix, emptyString())
        assertThat(location.value, equalTo("Location, COUNTRY"))
    }

    @Test
    fun `create a user homepage field`() {
        val homepage = HomePageField("www.homepage.com")

        assertThat(homepage, isA(UserInfoField::class.java))
        assertThat(homepage.fieldName, equalTo("homepage"))
        assertThat(homepage.symbol, equalTo("\\faGlobe"))
        assertThat(homepage.prefix, equalTo("https://"))
        assertThat(homepage.value, equalTo("www.homepage.com"))
    }

    @Test
    fun `create a user twitter field`() {
        val twitterField = TwitterField("@twitterhandle")

        assertThat(twitterField, isA(UserInfoField::class.java))
        assertThat(twitterField.fieldName, equalTo("twitter"))
        assertThat(twitterField.symbol, equalTo("\\faTwitter"))
        assertThat(twitterField.prefix, equalTo("https://twitter.com/"))
        assertThat(twitterField.value, equalTo("@twitterhandle"))
    }

    @Test
    fun `create a user linkedin field`() {
        val linkedinField = LinkedinField("your_id")

        assertThat(linkedinField, isA(UserInfoField::class.java))
        assertThat(linkedinField.fieldName, equalTo("linkedin"))
        assertThat(linkedinField.symbol, equalTo("\\faLinkedin"))
        assertThat(linkedinField.prefix, equalTo("https://linkedin.com/in/"))
        assertThat(linkedinField.value, equalTo("your_id"))
    }

    @Test
    fun `create a user github field`() {
        val githubField = GithubField("your_id")

        assertThat(githubField, isA(UserInfoField::class.java))
        assertThat(githubField.fieldName, equalTo("github"))
        assertThat(githubField.symbol, equalTo("\\faGithub"))
        assertThat(githubField.prefix, equalTo("https://github.com/"))
        assertThat(githubField.value, equalTo("your_id"))
    }

    @Test
    fun `create a user orcid field`() {
        val orcidField = OrcidField("0000-0000-0000-0000")

        assertThat(orcidField, isA(UserInfoField::class.java))
        assertThat(orcidField.fieldName, equalTo("orcid"))
        assertThat(orcidField.symbol, equalTo("\\faOrcid"))
        assertThat(orcidField.prefix, equalTo("https://orcid.org/"))
        assertThat(orcidField.value, equalTo("0000-0000-0000-0000"))

        val ex = assertThrows<IllegalArgumentException> {
            OrcidField("0000")
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
            EmailField("your_name@email.com"),
            PhoneField("000-00-0000"),
            MailAddressField("Address, Street, 00000 Country"),
            LocationField("Location, COUNTRY")
        )

        val user = UserInfo("Your Name Here", personalInfo)

        assertThat(user, notNullValue())
        assertThat(user.name, equalTo("Your Name Here"))
        assertThat(user.personalInfo, everyItem(isA(UserInfoField::class.java)))
        assertThat(user.personalInfo, hasSize(4))
        assertThat(user.personalInfo, equalTo(personalInfo))
    }
}
