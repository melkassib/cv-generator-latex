@file:JvmName("User")

package com.melkassib.cvgenerator.awesomecv.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.awesomecv.serialization.UserInfoSerializers
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import java.util.*

/**
 * Represents a collection of personal information of a user in the AwesomeCV resume.
 */
typealias AwesomeCVUserPersonalInfo = LinkedHashSet<UserInfoField>

/**
 * Represents the user information in the AwesomeCV resume.
 *
 * @property firstName The first name of the user.
 * @property lastName The last name of the user.
 * @property personalInfo The personal information fields of the user.
 */
data class AwesomeCVUserInfo @JvmOverloads constructor(
    var firstName: String = "",
    var lastName: String = "",
    var personalInfo: AwesomeCVUserPersonalInfo = linkedSetOf()
)

/**
 * Represents a user information field in the AwesomeCV resume.
 *
 * @property fieldName The name of the field.
 * @property value The value of the field.
 * @property valueId The value ID of the field (if exists).
 */
@JsonSerialize(using = UserInfoSerializers.Serializer::class)
@JsonDeserialize(using = UserInfoSerializers.Deserializer::class)
open class UserInfoField(val fieldName: String, open val value: String = "", open val valueId: String? = null) {
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
        if (value != other.value) return false
        if (valueId != other.valueId) return false

        return true
    }

    override fun hashCode() = Objects.hash(fieldName, value, valueId)

    override fun toString(): String {
        return "UserInfoField(fieldName='$fieldName', value='$value', valueId=$valueId)"
    }
}

/**
 * Represents the position field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The position of the user.
 */
data class Position(override val value: String) : UserInfoField("position")

/**
 * Represents the address field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The address of the user.
 */
data class MailAddress(override val value: String) : UserInfoField("address")

/**
 * Represents the phone number field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The phone number of the user.
 */
data class Phone(override val value: String) : UserInfoField("mobile")

/**
 * Represents the email field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The email address of the user.
 */
data class Email(override val value: String) : UserInfoField("email")

/**
 * Represents the homepage field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The homepage URL of the user.
 */
data class HomePage(override val value: String) : UserInfoField("homepage")

/**
 * Represents the GitHub field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The GitHub of the user.
 */
data class Github(override val value: String) : UserInfoField("github")

/**
 * Represents the GitHub field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The GitHub of the user.
 */
data class LinkedIn(override val value: String) : UserInfoField("linkedin")

/**
 * Represents the GitLab field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The GitLab of the user.
 */
data class Gitlab(override val value: String) : UserInfoField("gitlab")

/**
 * Represents the twitter field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The twitter handle of the user.
 */
data class Twitter(override val value: String) : UserInfoField("twitter")

/**
 * Represents the Skype field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The Skype of the user.
 */
data class Skype(override val value: String) : UserInfoField("skype")

/**
 * Represents the Reddit field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The Reddit of the user.
 */
data class Reddit(override val value: String) : UserInfoField("reddit")

/**
 * Represents the Medium field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The Medium of the user.
 */
data class Medium(override val value: String) : UserInfoField("medium")

/**
 * Represents a field to add extra information about a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The extra information.
 */
data class ExtraInfo(override val value: String) : UserInfoField("extrainfo")

/**
 * Represents the StackOverFlow field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The StackOverFlow of the user.
 */
data class StackOverFlow(override val valueId: String, override val value: String = "") : UserInfoField("stackoverflow", value, valueId)

/**
 * Represents the GoogleScholar field of a user.
 * Used in the AwesomeCV resume.
 *
 * @property value The GoogleScholar of the user.
 */
data class GoogleScholar(override val valueId: String, override val value: String = "") : UserInfoField("googlescholar", value, valueId)
