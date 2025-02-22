@file:JvmName("JsonSerializers")

package com.melkassib.cvgenerator.awesomecv.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.readValue
import com.melkassib.cvgenerator.awesomecv.domain.*
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.common.utils.JsonFieldNames

/**
 * Contains custom serializers and deserializers for UserInfoField (AwesomeCV).
 */
object UserInfoSerializers {

    object Serializer : JsonSerializer<UserInfoField>() {
        override fun serialize(userInfoField: UserInfoField, gen: JsonGenerator, serializers: SerializerProvider?) {
            with(gen) {
                writeStartObject()
                writeStringField(JsonFieldNames.FIELD_NAME, userInfoField.fieldName)
                writeStringField(JsonFieldNames.VALUE, userInfoField.value)
                userInfoField.valueId?.let {
                    writeStringField(JsonFieldNames.VALUE_ID, userInfoField.valueId)
                }
                writeEndObject()
            }
        }
    }

    object Deserializer : JsonDeserializer<UserInfoField>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): UserInfoField {
            val node = parser.readValueAsTree<JsonNode>()
            return when (node[JsonFieldNames.FIELD_NAME].asText()) {
                "position" -> Position(node[JsonFieldNames.VALUE].asText())
                "address" -> MailAddress(node[JsonFieldNames.VALUE].asText())
                "mobile" -> Phone(node[JsonFieldNames.VALUE].asText())
                "email" -> Email(node[JsonFieldNames.VALUE].asText())
                "homepage" -> HomePage(node[JsonFieldNames.VALUE].asText())
                "github" -> Github(node[JsonFieldNames.VALUE].asText())
                "linkedin" -> LinkedIn(node[JsonFieldNames.VALUE].asText())
                "gitlab" -> Gitlab(node[JsonFieldNames.VALUE].asText())
                "twitter" -> Twitter(node[JsonFieldNames.VALUE].asText())
                "skype" -> Skype(node[JsonFieldNames.VALUE].asText())
                "reddit" -> Reddit(node[JsonFieldNames.VALUE].asText())
                "medium" -> Medium(node[JsonFieldNames.VALUE].asText())
                "stackoverflow" -> StackOverFlow(node[JsonFieldNames.VALUE_ID].asText(), node[JsonFieldNames.VALUE].asText())
                "googlescholar" -> GoogleScholar(node[JsonFieldNames.VALUE_ID].asText(), node[JsonFieldNames.VALUE].asText())
                "extrainfo" -> ExtraInfo(node[JsonFieldNames.VALUE].asText())
                else -> UserInfoField("null")
            }
        }
    }
}

/**
 * Builds an AwesomeCV Resume object from a JSON string.
 *
 * @param json The JSON string representing the resume.
 * @return The [AwesomeCVResume] object.
 */
fun buildAwesomeCVResumeFromJson(json: String) = JSON_MAPPER.readValue<AwesomeCVResume>(json)
