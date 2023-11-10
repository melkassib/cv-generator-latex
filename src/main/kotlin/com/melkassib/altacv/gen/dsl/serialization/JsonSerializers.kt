@file:JvmName("JsonSerializers")

package com.melkassib.altacv.gen.dsl.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.melkassib.altacv.gen.dsl.domain.*
import com.melkassib.altacv.gen.dsl.domain.EventPeriodDate.Companion.eventDurationDate
import com.melkassib.altacv.gen.dsl.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.altacv.gen.dsl.utils.JsonFieldNames
import com.melkassib.altacv.gen.dsl.utils.USER_CONTACT_FIELDS
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object SectionContentSerializers {
    object ContentListSerializer : JsonSerializer<List<SectionContent>>() {
        override fun serialize(value: List<SectionContent>, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartArray()
            value.forEach { gen.writeObject(it.wrapped()) }
            gen.writeEndArray()
        }
    }

    object ContentListDeserializer : JsonDeserializer<List<SectionContent>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): List<SectionContent> {
            val node = parser.readValueAsTree<JsonNode>()
            return node.map { parser.codec.treeToValue(it, ContentWrapper::class.java).content }
        }
    }

    object ContentWrapperSerializer : JsonSerializer<ContentWrapper>() {
        override fun serialize(value: ContentWrapper, gen: JsonGenerator, serializers: SerializerProvider) {
            val type = value.content.type

            if (type === ContentType.EMPTY) return

            val withContent = type !in listOf(ContentType.DIVIDER, ContentType.NEWLINE, ContentType.NEWPAGE)

            with(gen) {
                writeStartObject()
                writeObjectField(JsonFieldNames.TYPE, type)

                if (withContent) {
                    serializeWrapperWithContent(gen, value)
                }

                writeEndObject()
            }
        }
    }

    object ContentWrapperDeserializer : JsonDeserializer<ContentWrapper>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ContentWrapper {
            val node = parser.readValueAsTree<JsonNode>()
            val type = parser.codec.treeToValue(node[JsonFieldNames.TYPE], ContentType::class.java)
                ?: throw IllegalArgumentException("Missing type")
            val content = node[JsonFieldNames.CONTENT]

            return when (type) {
                ContentType.DIVIDER -> ContentWrapper(Divider)
                ContentType.NEWLINE -> ContentWrapper(NewLine)
                ContentType.NEWPAGE -> ContentWrapper(NewPage)
                ContentType.QUOTE -> ContentWrapper(Quote(content.asText()))
                ContentType.GENERIC -> ContentWrapper(LatexContent(content.asText()))
                ContentType.TAG -> ContentWrapper(Tag(content.asText()))
                ContentType.EVENT -> ContentWrapper(parser.codec.treeToValue(content, Event::class.java))
                ContentType.ACHIEVEMENT -> ContentWrapper(parser.codec.treeToValue(content, Achievement::class.java))
                ContentType.SKILL -> ContentWrapper(parser.codec.treeToValue(content, Skill::class.java))
                ContentType.WHEELCHART -> ContentWrapper(parser.codec.treeToValue(content, WheelChart::class.java))
                ContentType.ITEM -> ContentWrapper(parser.codec.treeToValue(content, Item::class.java))
                ContentType.EMPTY -> ContentWrapper(NoContent)
            }
        }
    }
}

object DateSerializers {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    object Serializer : JsonSerializer<LocalDate>() {
        override fun serialize(
            localDate: LocalDate,
            jsonGenerator: JsonGenerator,
            serializerProvider: SerializerProvider
        ) {
            jsonGenerator.writeString(localDate.format(formatter).substring(0, 7))
        }
    }

    object Deserializer : JsonDeserializer<LocalDate>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): LocalDate {
            return LocalDate.parse(parser.valueAsString, formatter)
        }
    }
}

object EventPeriodDeserializer : JsonDeserializer<EventPeriod>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): EventPeriod {
        val node = parser.readValueAsTree<JsonNode>()
        val startDate = node[JsonFieldNames.START].asText()
        val endDate = node[JsonFieldNames.END].asText()

        return if (startDate.matches(Regex("\\d{4}-\\d{2}"))) {
            eventDurationDate(startDate, endDate)
        } else {
            eventDurationStr(startDate, endDate)
        }
    }
}

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
                "email" -> EmailField(node[JsonFieldNames.VALUE].asText())
                "phone" -> PhoneField(node[JsonFieldNames.VALUE].asText())
                "mailaddress" -> MailAddressField(node[JsonFieldNames.VALUE].asText())
                "location" -> LocationField(node[JsonFieldNames.VALUE].asText())
                "homepage" -> HomePageField(node[JsonFieldNames.VALUE].asText())
                "twitter" -> TwitterField(node[JsonFieldNames.VALUE].asText())
                "linkedin" -> LinkedinField(node[JsonFieldNames.VALUE].asText())
                "github" -> GithubField(node[JsonFieldNames.VALUE].asText())
                "orcid" -> OrcidField(node[JsonFieldNames.VALUE].asText())
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

private val JSON_MAPPER = run {
    val dateModule = SimpleModule()
    dateModule.addDeserializer(LocalDate::class.java, DateSerializers.Deserializer)
    dateModule.addSerializer(LocalDate::class.java, DateSerializers.Serializer)

    val kotlinModule = KotlinModule.Builder()
        .withReflectionCacheSize(512)
        .configure(KotlinFeature.NullToEmptyCollection, false)
        .configure(KotlinFeature.NullToEmptyMap, false)
        .configure(KotlinFeature.NullIsSameAsDefault, false)
        .configure(KotlinFeature.SingletonSupport, false)
        .configure(KotlinFeature.StrictNullChecks, false)
        .build()

    ObjectMapper().registerModule(kotlinModule).registerModule(dateModule)
}

private fun serializeWrapperWithContent(gen: JsonGenerator, wrapper: ContentWrapper) {
    val type = wrapper.content.type

    with(gen) {
        when (type) {
            ContentType.TAG, ContentType.QUOTE, ContentType.GENERIC -> {
                val simpleContent = wrapper.content as HasSimpleContent
                writeStringField(JsonFieldNames.CONTENT, simpleContent.content)
            }

            ContentType.SKILL -> {
                when (val skill = wrapper.content) {
                    is Skill -> {
                        writeObjectFieldStart(JsonFieldNames.CONTENT)
                        writeStringField(JsonFieldNames.SKILL, skill.skill)
                        writeNumberField(JsonFieldNames.RATING, skill.rating)
                        writeEndObject()
                    }

                    is SkillStr -> {
                        writeObjectFieldStart(JsonFieldNames.CONTENT)
                        writeStringField(JsonFieldNames.SKILL, skill.skill)
                        writeStringField(JsonFieldNames.FLUENCY, skill.fluency)
                        writeEndObject()
                    }

                    else -> throw IllegalArgumentException("Unsupported skill type: $type")
                }
            }

            else -> writePOJOField(JsonFieldNames.CONTENT, wrapper.content)
        }
    }
}

fun Resume.toJson(): String = JSON_MAPPER.writeValueAsString(this)
fun buildResumeFromJson(json: String): Resume = JSON_MAPPER.readValue<Resume>(json)
