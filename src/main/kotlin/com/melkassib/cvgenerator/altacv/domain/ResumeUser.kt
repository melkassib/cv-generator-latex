@file:JvmName("User")

package com.melkassib.cvgenerator.altacv.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.altacv.serialization.UserInfoSerializers
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import java.util.*

/**
 * Represents the personal information of a user.
 * This class is used to represent the personal information of a user in the resume.
 */
typealias AltaCVUserPersonalInfo = Set<AltaCVUserInfoField>

/**
 * Represents the user information of a user.
 * This class is used to represent the user information of a user in the resume.
 */
@JvmRecord
data class AltaCVUserInfo @JvmOverloads constructor(val name: String = "", val personalInfo: AltaCVUserPersonalInfo = setOf())

/**
 * Represents the user information field of a user.
 * This class is used to represent the user information field of a user in the resume.
 */
@JsonSerialize(using = UserInfoSerializers.Serializer::class)
@JsonDeserialize(using = UserInfoSerializers.Deserializer::class)
open class AltaCVUserInfoField(
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

        other as AltaCVUserInfoField

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
class Email(override val value: String) : AltaCVUserInfoField("email", "\\faAt", "mailto:")

/**
 * Represents the phone field of a user.
 * This class is used to represent the phone field of a user in the resume.
 *
 * @property value The phone number of the user.
 */
class Phone(override val value: String) : AltaCVUserInfoField("phone", "\\faPhone", "tel:")

/**
 * Represents the mail address field of a user.
 * This class is used to represent the mail address field of a user in the resume.
 *
 * @property value The mail address of the user.
 */
class MailAddress(override val value: String) : AltaCVUserInfoField("mailaddress", "\\faEnvelope")

/**
 * Represents the location field of a user.
 * This class is used to represent the location field of a user in the resume.
 *
 * @property value The location of the user.
 */
class Location(override val value: String) : AltaCVUserInfoField("location", "\\cvLocationMarker")

/**
 * Represents the home page field of a user.
 * This class is used to represent the home page field of a user in the resume.
 *
 * @property value The home page URL of the user.
 */
class HomePage(override val value: String) : AltaCVUserInfoField("homepage", "\\faGlobe", "https://")

/**
 * Represents the twitter field of a user.
 * This class is used to represent the twitter field of a user in the resume.
 *
 * @property value The twitter handle of the user.
 */
class Twitter(override val value: String) : AltaCVUserInfoField("twitter", "\\faTwitter", "https://twitter.com/")

/**
 * Represents the GitHub field of a user.
 * This class is used to represent the GitHub field of a user in the resume.
 *
 * @property value The GitHub username of the user.
 */
class Github(override val value: String) : AltaCVUserInfoField("github", "\\faGithub", "https://github.com/")

/**
 * Represents the LinkedIn field of a user.
 * This class is used to represent the LinkedIn field of a user in the resume.
 *
 * @property value The LinkedIn profile URL of the user.
 */
class LinkedIn(override val value: String) :
    AltaCVUserInfoField(
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
class Orcid(override val value: String) : AltaCVUserInfoField("orcid", "\\faOrcid", "https://orcid.org/") {
    init {
        require(value.matches(Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))) {
            "Invalid ORCID: $value. Expected format: dddd-dddd-dddd-dddd"
        }
    }
}
