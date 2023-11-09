@file:JvmName("User")

package com.melkassib.altacv.gen.dsl.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.altacv.gen.dsl.serialization.UserInfoSerializers

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
    override fun toString(): String {
        return "UserInfoField(fieldName='$fieldName', symbol='$symbol', prefix='$prefix', value='$value')"
    }
}

data class EmailField(override val value: String) : UserInfoField("email", "\\faAt", "mailto:")
data class PhoneField(override val value: String) : UserInfoField("phone", "\\faPhone", "tel:")
data class MailAddressField(override val value: String) : UserInfoField("mailaddress", "\\faEnvelope")
data class LocationField(override val value: String) : UserInfoField("location", "\\cvLocationMarker")
data class HomePageField(override val value: String) : UserInfoField("homepage", "\\faGlobe", "https://")
data class TwitterField(override val value: String) : UserInfoField("twitter", "\\faTwitter", "https://twitter.com/")
data class GithubField(override val value: String) : UserInfoField("github", "\\faGithub", "https://github.com/")

data class LinkedinField(override val value: String) :
    UserInfoField(
        "linkedin",
        "\\faLinkedin",
        "https://linkedin.com/in/"
    )

data class OrcidField(override val value: String) : UserInfoField("orcid", "\\faOrcid", "https://orcid.org/") {
    init {
        require(value.matches(Regex("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))) {
            "Invalid ORCID: $value. Expected format: dddd-dddd-dddd-dddd"
        }
    }
}
