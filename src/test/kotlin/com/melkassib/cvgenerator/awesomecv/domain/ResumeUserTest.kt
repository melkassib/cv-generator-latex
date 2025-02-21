package com.melkassib.cvgenerator.awesomecv.domain

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

class ResumeUserTest {

    @Test
    fun `create a user position field`() {
        val position = Position("Site Reliability Engineer")

        assertThat(position, isA(AwesomeCVUserInfoField::class.java))
        assertThat(position.fieldName, equalTo("position"))
        assertThat(position.value, equalTo("Site Reliability Engineer"))
        assertThat(position.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user mail address field`() {
        val mailAddress = MailAddress("Address, Street, 00000 Country")

        assertThat(mailAddress, isA(AwesomeCVUserInfoField::class.java))
        assertThat(mailAddress.fieldName, equalTo("address"))
        assertThat(mailAddress.value, equalTo("Address, Street, 00000 Country"))
        assertThat(mailAddress.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user phone field`() {
        val phone = Phone("000-00-0000")

        assertThat(phone, isA(AwesomeCVUserInfoField::class.java))
        assertThat(phone.fieldName, equalTo("mobile"))
        assertThat(phone.value, equalTo("000-00-0000"))
        assertThat(phone.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user email field`() {
        val email = Email("your_name@email.com")

        assertThat(email, isA(AwesomeCVUserInfoField::class.java))
        assertThat(email.fieldName, equalTo("email"))
        assertThat(email.value, equalTo("your_name@email.com"))
        assertThat(email.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user homepage field`() {
        val homepage = HomePage("www.homepage.com")

        assertThat(homepage, isA(AwesomeCVUserInfoField::class.java))
        assertThat(homepage.fieldName, equalTo("homepage"))
        assertThat(homepage.value, equalTo("www.homepage.com"))
        assertThat(homepage.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user github field`() {
        val github = Github("your_id")

        assertThat(github, isA(AwesomeCVUserInfoField::class.java))
        assertThat(github.fieldName, equalTo("github"))
        assertThat(github.value, equalTo("your_id"))
        assertThat(github.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user linkedin field`() {
        val linkedIn = LinkedIn("your_id")

        assertThat(linkedIn, isA(AwesomeCVUserInfoField::class.java))
        assertThat(linkedIn.fieldName, equalTo("linkedin"))
        assertThat(linkedIn.value, equalTo("your_id"))
        assertThat(linkedIn.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user gitlab field`() {
        val gitlab = Gitlab("your_id")

        assertThat(gitlab, isA(AwesomeCVUserInfoField::class.java))
        assertThat(gitlab.fieldName, equalTo("gitlab"))
        assertThat(gitlab.value, equalTo("your_id"))
    }

    @Test
    fun `create a user twitter field`() {
        val twitter = Twitter("@twitterhandle")

        assertThat(twitter, isA(AwesomeCVUserInfoField::class.java))
        assertThat(twitter.fieldName, equalTo("twitter"))
        assertThat(twitter.value, equalTo("@twitterhandle"))
        assertThat(twitter.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user skype field`() {
        val skype = Skype("skype-id")

        assertThat(skype, isA(AwesomeCVUserInfoField::class.java))
        assertThat(skype.fieldName, equalTo("skype"))
        assertThat(skype.value, equalTo("skype-id"))
        assertThat(skype.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user reddit field`() {
        val reddit = Reddit("reddit-id")

        assertThat(reddit, isA(AwesomeCVUserInfoField::class.java))
        assertThat(reddit.fieldName, equalTo("reddit"))
        assertThat(reddit.value, equalTo("reddit-id"))
        assertThat(reddit.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user medium field`() {
        val medium = Medium("medium-id")

        assertThat(medium, isA(AwesomeCVUserInfoField::class.java))
        assertThat(medium.fieldName, equalTo("medium"))
        assertThat(medium.value, equalTo("medium-id"))
        assertThat(medium.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user extra info field`() {
        val extraInfo = ExtraInfo("extra information")

        assertThat(extraInfo, isA(AwesomeCVUserInfoField::class.java))
        assertThat(extraInfo.fieldName, equalTo("extrainfo"))
        assertThat(extraInfo.value, equalTo("extra information"))
        assertThat(extraInfo.valueId, `is`(nullValue()))
    }

    @Test
    fun `create a user stackoverflow field`() {
        val stackOverFlow1 = StackOverFlow("SO-id", "SO-name")
        val stackOverFlow2 = StackOverFlow("SO-id")

        assertThat(stackOverFlow1, isA(AwesomeCVUserInfoField::class.java))
        assertThat(stackOverFlow1.fieldName, equalTo("stackoverflow"))
        assertThat(stackOverFlow1.value, equalTo("SO-name"))
        assertThat(stackOverFlow1.valueId, equalTo("SO-id"))

        assertThat(stackOverFlow2, isA(AwesomeCVUserInfoField::class.java))
        assertThat(stackOverFlow2.fieldName, equalTo("stackoverflow"))
        assertThat(stackOverFlow2.value, `is`(emptyString()))
        assertThat(stackOverFlow2.valueId, equalTo("SO-id"))
    }

    @Test
    fun `create a user google scholar field`() {
        val googleScholar1 = GoogleScholar("googlescholar-id", "name-to-display")
        val googleScholar2 = GoogleScholar("googlescholar-id")

        assertThat(googleScholar1, isA(AwesomeCVUserInfoField::class.java))
        assertThat(googleScholar1.fieldName, equalTo("googlescholar"))
        assertThat(googleScholar1.value, equalTo("name-to-display"))
        assertThat(googleScholar1.valueId, equalTo("googlescholar-id"))

        assertThat(googleScholar2, isA(AwesomeCVUserInfoField::class.java))
        assertThat(googleScholar2.fieldName, equalTo("googlescholar"))
        assertThat(googleScholar2.value, `is`(emptyString()))
        assertThat(googleScholar2.valueId, equalTo("googlescholar-id"))
    }

    @Test
    fun `create a user object`() {
        val personalInfo = linkedSetOf(
            Email("your_name@email.com"),
            Phone("000-00-0000"),
            MailAddress("Address, Street, 00000 Country")
        )

        val user1 = AwesomeCVUserInfo()
        val user2 = AwesomeCVUserInfo("John")
        val user3 = AwesomeCVUserInfo("John", "Dupont")
        val user4 = AwesomeCVUserInfo("John",  "Dupont", personalInfo)

        assertThat(user1, notNullValue())
        assertThat(user1.firstName, equalTo(""))
        assertThat(user1.lastName, equalTo(""))
        assertThat(user1.personalInfo, hasSize(0))

        assertThat(user2, notNullValue())
        assertThat(user2.firstName, equalTo("John"))
        assertThat(user2.lastName, equalTo(""))
        assertThat(user2.personalInfo, hasSize(0))

        assertThat(user3, notNullValue())
        assertThat(user3.firstName, equalTo("John"))
        assertThat(user3.lastName, equalTo("Dupont"))
        assertThat(user3.personalInfo, hasSize(0))

        assertThat(user4, notNullValue())
        assertThat(user4.firstName, equalTo("John"))
        assertThat(user4.lastName, equalTo("Dupont"))
        assertThat(user4.personalInfo, everyItem(isA(AwesomeCVUserInfoField::class.java)))
        assertThat(user4.personalInfo, hasSize(3))
        assertThat(user4.personalInfo, equalTo(personalInfo))
    }
}
