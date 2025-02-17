@file:JvmName("User")

package com.melkassib.cvgenerator.altacv.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.altacv.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.altacv.serialization.UserInfoSerializers
import java.util.*

/**
 * Represents the personal information of a user.
 * This class is used to represent the personal information of a user in the resume.
 */
typealias UserPersonalInfo = Set<UserInfoField>

/**
 * Represents the user information of a user.
 * This class is used to represent the user information of a user in the resume.
 */
@JvmRecord
data class UserInfo @JvmOverloads constructor(val name: String = "", val personalInfo: UserPersonalInfo = setOf())

/**
 * Represents the user information field of a user.
 * This class is used to represent the user information field of a user in the resume.
 */
@JsonSerialize(using = UserInfoSerializers.Serializer::class)
@JsonDeserialize(using = UserInfoSerializers.Deserializer::class)
open class UserInfoField(
    val fieldName: String,
    val symbol: String,
    val prefix: String = "",
    open val value: String = ""
) {
    /**
     * Converts the user information field to a JSON string.
     *
     * @return The JSON string representation of the user information field.
     */
    fun toJson(): String = JSON_MAPPER.writeValueAsString(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInfoField

        if (fieldName != other.fieldName) return false
        if (symbol != other.symbol) return false
        if (prefix != other.prefix) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode() = Objects.hash(fieldName, symbol, prefix, value)

    override fun toString(): String {
        return "UserInfoField(fieldName='$fieldName', symbol='$symbol', prefix='$prefix', value='$value')"
    }
}

/**
 * Represents the email field of a user.
 * This class is used to represent the email field of a user in the resume.
 *
 * @property value The email address of the user.
 */
class EmailField(override val value: String) : UserInfoField("email", "\\faAt", "mailto:")

/**
 * Represents the phone field of a user.
 * This class is used to represent the phone field of a user in the resume.
 *
 * @property value The phone number of the user.
 */
class PhoneField(override val value: String) : UserInfoField("phone", "\\faPhone", "tel:")

/**
 * Represents the mail address field of a user.
 * This class is used to represent the mail address field of a user in the resume.
 *
 * @property value The mail address of the user.
 */
class MailAddressField(override val value: String) : UserInfoField("mailaddress", "\\faEnvelope")

/**
 * Represents the location field of a user.
 * This class is used to represent the location field of a user in the resume.
 *
 * @property value The location of the user.
 */
class LocationField(override val value: String) : UserInfoField("location", "\\cvLocationMarker")

/**
 * Represents the home page field of a user.
 * This class is used to represent the home page field of a user in the resume.
 *
 * @property value The home page URL of the user.
 */
class HomePageField(override val value: String) : UserInfoField("homepage", "\\faGlobe", "https://")

/**
 * Represents the twitter field of a user.
 * This class is used to represent the twitter field of a user in the resume.
 *
 * @property value The twitter handle of the user.
 */
class TwitterField(override val value: String) : UserInfoField("twitter", "\\faTwitter", "https://twitter.com/")

/**
 * Represents the GitHub field of a user.
 * This class is used to represent the GitHub field of a user in the resume.
 *
 * @property value The GitHub username of the user.
 */
class GithubField(override val value: String) : UserInfoField("github", "\\faGithub", "https://github.com/")

/**
 * Represents the LinkedIn field of a user.
 * This class is used to represent the LinkedIn field of a user in the resume.
 *
 * @property value The LinkedIn profile URL of the user.
 */
class LinkedinField(override val value: String) :
    UserInfoField(
        "linkedin",
        "\\faLinkedin",
        "https://linkedin.com/in/"
    )

/**
 * Represents the ORCID field of a user.
 * This class is used to represent the ORCID field of a user in the resume.
 *
 * @property value The ORCID identifier of the user.
 * @throws IllegalArgumentException if the ORCID format is invalid.
 */
class OrcidField(override val value: String) : UserInfoField("orcid", "\\faOrcid", "https://orcid.org/") {
    init {
        require(value.matches(Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))) {
            "Invalid ORCID: $value. Expected format: dddd-dddd-dddd-dddd"
        }
    }
}
