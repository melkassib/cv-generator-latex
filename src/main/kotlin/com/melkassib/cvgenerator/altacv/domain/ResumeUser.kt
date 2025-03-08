@file:JvmName("User")

package com.melkassib.cvgenerator.altacv.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.altacv.serialization.UserInfoSerializers
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import java.util.*
import kotlin.collections.LinkedHashSet

/**
 * Represents a collection of personal information of a user in the AltaCV resume.
 */
typealias AltaCVUserPersonalInfo = LinkedHashSet<UserInfoField>

/**
 * Represents the user information of a user in the AltaCV resume.
 *
 * @property name The name of the user.
 * @property personalInfo The personal information of the user.
 */
@JvmRecord
data class AltaCVUserInfo @JvmOverloads constructor(
    val name: String = "",
    val personalInfo: AltaCVUserPersonalInfo = linkedSetOf()
)

/**
 * Represents a user information field in the AltaCV resume.
 *
 * @property fieldName The name of the field.
 * @property symbol The LaTeX symbol to represent the field.
 * @property prefix The prefix to be added to the field value.
 * @property value The value of the field.
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
        return "AltaCVUserInfoField(fieldName='$fieldName', symbol='$symbol', prefix='$prefix', value='$value')"
    }
}

/**
 * Represents the email field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The email address of the user.
 */
data class Email(override val value: String) : UserInfoField("email", "\\faAt", "mailto:")

/**
 * Represents the phone field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The phone number of the user.
 */
data class Phone(override val value: String) : UserInfoField("phone", "\\faPhone", "tel:")

/**
 * Represents the mail address field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The mail address of the user.
 */
data class MailAddress(override val value: String) : UserInfoField("mailaddress", "\\faEnvelope")

/**
 * Represents the location field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The location of the user.
 */
data class Location(override val value: String) : UserInfoField("location", "\\cvLocationMarker")

/**
 * Represents the homepage website field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The homepage URL of the user.
 */
data class HomePage(override val value: String) : UserInfoField("homepage", "\\faGlobe", "https://")

/**
 * Represents the twitter field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The twitter handle of the user.
 */
data class Twitter(override val value: String) : UserInfoField("twitter", "\\raisebox{-0.2ex}{\\scalebox{0.95}{\\simpleicon{x}}}", "https://x.com/")

/**
 * Represents the GitHub field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The GitHub username of the user.
 */
data class Github(override val value: String) : UserInfoField("github", "\\faGithub", "https://github.com/")

/**
 * Represents the LinkedIn field of a user.
 * Used in the AltaCV resume.
 *
 * @property value The LinkedIn profile URL of the user.
 */
data class LinkedIn(override val value: String) :
    UserInfoField(
        "linkedin",
        "\\faLinkedin",
        "https://linkedin.com/in/"
    )

/**
 * Represents the ORCID field of a user, format: 0000-0000-0000-0000.
 * Used in the AltaCV resume.
 *
 * @property value The ORCID identifier of the user.
 * @throws IllegalArgumentException if the ORCID format is invalid.
 */
data class Orcid(override val value: String) : UserInfoField("orcid", "\\faOrcid", "https://orcid.org/") {
    init {
        require(value.matches(Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))) {
            "Invalid ORCID: $value. Expected format: dddd-dddd-dddd-dddd"
        }
    }
}
