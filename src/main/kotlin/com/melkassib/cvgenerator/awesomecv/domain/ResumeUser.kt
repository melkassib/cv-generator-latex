package com.melkassib.cvgenerator.awesomecv.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.altacv.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.awesomecv.serialization.UserInfoSerializers
import java.util.*
import kotlin.collections.LinkedHashSet

typealias AwesomeCVUserPersonalInfo = LinkedHashSet<UserInfoField>

data class UserInfo @JvmOverloads constructor(
    var firstName: String = "",
    var lastName: String = "",
    var personalInfo: AwesomeCVUserPersonalInfo = linkedSetOf()
)

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

data class Position(override val value: String) : UserInfoField("position")
data class MailAddress(override val value: String) : UserInfoField("address")
data class Phone(override val value: String) : UserInfoField("mobile")
data class Email(override val value: String) : UserInfoField("email")
data class HomePage(override val value: String) : UserInfoField("homepage")
data class Github(override val value: String) : UserInfoField("github")
data class LinkedIn(override val value: String) : UserInfoField("linkedin")
data class Gitlab(override val value: String) : UserInfoField("gitlab")
data class Twitter(override val value: String) : UserInfoField("twitter")
data class Skype(override val value: String) : UserInfoField("skype")
data class Reddit(override val value: String) : UserInfoField("reddit")
data class Medium(override val value: String) : UserInfoField("medium")
data class ExtraInfo(override val value: String) : UserInfoField("extrainfo")

data class StackOverFlow(override val valueId: String, override val value: String = "") : UserInfoField("stackoverflow", value, valueId)
data class GoogleScholar(override val valueId: String, override val value: String = "") : UserInfoField("googlescholar", value, valueId)
