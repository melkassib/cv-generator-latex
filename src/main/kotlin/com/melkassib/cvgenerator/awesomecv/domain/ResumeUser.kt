package com.melkassib.cvgenerator.awesomecv.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.awesomecv.serialization.UserInfoSerializers
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import java.util.*

typealias AwesomeCVUserPersonalInfo = LinkedHashSet<AwesomeCVUserInfoField>

data class AwesomeCVUserInfo @JvmOverloads constructor(
    var firstName: String = "",
    var lastName: String = "",
    var personalInfo: AwesomeCVUserPersonalInfo = linkedSetOf()
)

@JsonSerialize(using = UserInfoSerializers.Serializer::class)
@JsonDeserialize(using = UserInfoSerializers.Deserializer::class)
open class AwesomeCVUserInfoField(val fieldName: String, open val value: String = "", open val valueId: String? = null) {
    /**
     * Converts the user information field to a JSON string.
     *
     * @return The JSON string representation of the user information field.
     */
    fun toJson(): String = JSON_MAPPER.writeValueAsString(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AwesomeCVUserInfoField

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

data class Position(override val value: String) : AwesomeCVUserInfoField("position")
data class MailAddress(override val value: String) : AwesomeCVUserInfoField("address")
data class Phone(override val value: String) : AwesomeCVUserInfoField("mobile")
data class Email(override val value: String) : AwesomeCVUserInfoField("email")
data class HomePage(override val value: String) : AwesomeCVUserInfoField("homepage")
data class Github(override val value: String) : AwesomeCVUserInfoField("github")
data class LinkedIn(override val value: String) : AwesomeCVUserInfoField("linkedin")
data class Gitlab(override val value: String) : AwesomeCVUserInfoField("gitlab")
data class Twitter(override val value: String) : AwesomeCVUserInfoField("twitter")
data class Skype(override val value: String) : AwesomeCVUserInfoField("skype")
data class Reddit(override val value: String) : AwesomeCVUserInfoField("reddit")
data class Medium(override val value: String) : AwesomeCVUserInfoField("medium")
data class ExtraInfo(override val value: String) : AwesomeCVUserInfoField("extrainfo")

data class StackOverFlow(override val valueId: String, override val value: String = "") : AwesomeCVUserInfoField("stackoverflow", value, valueId)
data class GoogleScholar(override val valueId: String, override val value: String = "") : AwesomeCVUserInfoField("googlescholar", value, valueId)
