@file:JvmName("User")

package com.melkassib.altacv.gen.dsl.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.altacv.gen.dsl.serialization.JSON_MAPPER
import com.melkassib.altacv.gen.dsl.serialization.UserInfoSerializers
import java.util.*

typealias UserPersonalInfo = Set<UserInfoField>

@JvmRecord
data class RUser @JvmOverloads constructor(val name: String = "", val personalInfo: UserPersonalInfo = setOf())

@JsonSerialize(using = UserInfoSerializers.Serializer::class)
@JsonDeserialize(using = UserInfoSerializers.Deserializer::class)
open class UserInfoField(
    val fieldName: String,
    val symbol: String,
    val prefix: String = "",
    open val value: String = ""
) {
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

class EmailField(override val value: String) : UserInfoField("email", "\\faAt", "mailto:")
class PhoneField(override val value: String) : UserInfoField("phone", "\\faPhone", "tel:")
class MailAddressField(override val value: String) : UserInfoField("mailaddress", "\\faEnvelope")
class LocationField(override val value: String) : UserInfoField("location", "\\cvLocationMarker")
class HomePageField(override val value: String) : UserInfoField("homepage", "\\faGlobe", "https://")
class TwitterField(override val value: String) : UserInfoField("twitter", "\\faTwitter", "https://twitter.com/")
class GithubField(override val value: String) : UserInfoField("github", "\\faGithub", "https://github.com/")

class LinkedinField(override val value: String) :
    UserInfoField(
        "linkedin",
        "\\faLinkedin",
        "https://linkedin.com/in/"
    )

class OrcidField(override val value: String) : UserInfoField("orcid", "\\faOrcid", "https://orcid.org/") {
    init {
        require(value.matches(Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))) {
            "Invalid ORCID: $value. Expected format: dddd-dddd-dddd-dddd"
        }
    }
}
