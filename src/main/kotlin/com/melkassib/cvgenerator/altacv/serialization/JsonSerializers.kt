@file:JvmName("JsonSerializers")

package com.melkassib.cvgenerator.altacv.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.readValue
import com.melkassib.cvgenerator.altacv.domain.*
import com.melkassib.cvgenerator.altacv.utils.USER_CONTACT_FIELDS
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.common.utils.JsonFieldNames

/**
 * Contains custom serializers and deserializers for UserInfoField (AltaCV).
 */
object UserInfoSerializers {

    object Serializer : JsonSerializer<AltaCVUserInfoField>() {
        override fun serialize(userInfoField: AltaCVUserInfoField, gen: JsonGenerator, serializers: SerializerProvider?) {
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

    object Deserializer : JsonDeserializer<AltaCVUserInfoField>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): AltaCVUserInfoField {
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
                    return AltaCVUserInfoField(fieldName, symbol, prefix, value)
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
