@file:JvmName("JacksonSerializers")

package com.melkassib.cvgenerator.altacv.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.readValue
import com.melkassib.cvgenerator.altacv.domain.*
import com.melkassib.cvgenerator.altacv.utils.USER_CONTACT_FIELDS
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.common.serialization.YAML_MAPPER
import com.melkassib.cvgenerator.common.utils.JsonFieldNames

/**
 * Contains custom serializers and deserializers for UserInfoField (AltaCV).
 */
object UserInfoSerializers {

    object Serializer : JsonSerializer<UserInfoField>() {
        override fun serialize(userInfoField: UserInfoField, gen: JsonGenerator, serializers: SerializerProvider?) {
            with(gen) {
                writeStartObject()
                writeStringField(JsonFieldNames.FIELD_NAME, userInfoField.fieldName)
                if (userInfoField.fieldName !in USER_CONTACT_FIELDS) {
                    writeStringField(JsonFieldNames.SYMBOL, userInfoField.symbol)
                    writeStringField(JsonFieldNames.PREFIX, userInfoField.prefix)
                }
                writeStringField(JsonFieldNames.VALUE, userInfoField.value)
                writeEndObject()
            }
        }
    }

    object Deserializer : JsonDeserializer<UserInfoField>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): UserInfoField {
            val node = parser.readValueAsTree<JsonNode>()
            return when (val fieldName = node[JsonFieldNames.FIELD_NAME].asText()) {
                "email" -> Email(node[JsonFieldNames.VALUE].asText())
                "phone" -> Phone(node[JsonFieldNames.VALUE].asText())
                "mailaddress" -> MailAddress(node[JsonFieldNames.VALUE].asText())
                "location" -> Location(node[JsonFieldNames.VALUE].asText())
                "homepage" -> HomePage(node[JsonFieldNames.VALUE].asText())
                "twitter" -> Twitter(node[JsonFieldNames.VALUE].asText())
                "linkedin" -> LinkedIn(node[JsonFieldNames.VALUE].asText())
                "github" -> Github(node[JsonFieldNames.VALUE].asText())
                "orcid" -> Orcid(node[JsonFieldNames.VALUE].asText())
                else -> {
                    val symbol = node[JsonFieldNames.SYMBOL].asText()
                    val prefix = node[JsonFieldNames.PREFIX].asText()
                    val value = node[JsonFieldNames.VALUE].asText()
                    return UserInfoField(fieldName, symbol, prefix, value)
                }
            }
        }
    }
}

/**
 * Builds an AltaCV Resume object from a JSON string.
 *
 * @param json The JSON string representing the resume.
 * @return The [AltaCVResume] object.
 */
fun buildAltaCVResumeFromJson(json: String) = JSON_MAPPER.readValue<AltaCVResume>(json)

/**
 * Builds an AltaCV Resume object from a YAML string.
 *
 * @param yaml The YAML string representing the resume.
 * @return The [AltaCVResume] object.
 */
fun buildAltaCVResumeFromYaml(yaml: String) = YAML_MAPPER.readValue<AltaCVResume>(yaml)
