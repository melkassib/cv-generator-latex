@file:JvmName("JsonSerializers")

package com.melkassib.cvgenerator.common.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.melkassib.cvgenerator.common.domain.*
import com.melkassib.cvgenerator.common.domain.EventPeriodDate.Companion.eventDurationDate
import com.melkassib.cvgenerator.common.domain.EventPeriodString.Companion.eventDurationStr
import com.melkassib.cvgenerator.common.utils.JsonFieldNames
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Contains custom serializers and deserializers for SectionContent and related classes.
 */
object SectionContentSerializers {
    /**
     * Serializer for a list of SectionContent.
     */
    object ContentListSerializer : JsonSerializer<List<SectionContent>>() {
        override fun serialize(value: List<SectionContent>, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeStartArray()
            value.forEach { gen.writeObject(it.wrapped()) }
            gen.writeEndArray()
        }
    }

    /**
     * Deserializer for a list of SectionContent.
     */
    object ContentListDeserializer : JsonDeserializer<List<SectionContent>>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): List<SectionContent> {
            val node = parser.readValueAsTree<JsonNode>()
            return node.map { parser.codec.treeToValue(it, ContentWrapper::class.java).content }
        }
    }

    /**
     * Serializer for ContentWrapper.
     */
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

    /**
     * Deserializer for ContentWrapper.
     */
    object ContentWrapperDeserializer : JsonDeserializer<ContentWrapper>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ContentWrapper {
            val node = parser.readValueAsTree<JsonNode>()
            val type = parser.codec.treeToValue(node[JsonFieldNames.TYPE], ContentType::class.java)
            val content = node[JsonFieldNames.CONTENT]

            return when (type!!) {
                ContentType.DIVIDER -> ContentWrapper(Divider)
                ContentType.NEWLINE -> ContentWrapper(NewLine)
                ContentType.NEWPAGE -> ContentWrapper(NewPage)
                ContentType.QUOTE -> ContentWrapper(Quote(content.asText()))
                ContentType.GENERIC -> ContentWrapper(LatexContent(content.asText()))
                ContentType.TAG -> ContentWrapper(Tag(content.asText()))
                ContentType.EVENT -> ContentWrapper(parser.codec.treeToValue(content, Event::class.java))
                ContentType.EVENT_ENTRY -> ContentWrapper(parser.codec.treeToValue(content, Entry::class.java))
                ContentType.ACHIEVEMENT -> ContentWrapper(parser.codec.treeToValue(content, Achievement::class.java))
                ContentType.SKILL -> {
                    if (content.has(JsonFieldNames.FLUENCY)) {
                        ContentWrapper(parser.codec.treeToValue(content, SkillStr::class.java))
                    } else {
                        ContentWrapper(parser.codec.treeToValue(content, Skill::class.java))
                    }
                }
                ContentType.WHEELCHART -> ContentWrapper(parser.codec.treeToValue(content, WheelChart::class.java))
                ContentType.ITEM -> ContentWrapper(parser.codec.treeToValue(content, Item::class.java))
                ContentType.EMPTY -> ContentWrapper(NoContent)
                ContentType.HONOR_LIST -> ContentWrapper(parser.codec.treeToValue(content, HonorList::class.java))
                ContentType.PARAGRAPH -> ContentWrapper(Paragraph(content.asText()))
            }
        }
    }
}

/**
 * Contains custom serializers for LocalDate.
 */
object DateSerializers {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Serializer for LocalDate.
     */
    object Serializer : JsonSerializer<LocalDate>() {
        override fun serialize(
            localDate: LocalDate,
            jsonGenerator: JsonGenerator,
            serializerProvider: SerializerProvider
        ) {
            jsonGenerator.writeString(localDate.format(formatter).substring(0, 7))
        }
    }
}

/**
 * Deserializer for EventPeriod.
 */
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

/**
 * JSON ObjectMapper configured with custom modules for Kotlin and date serialization.
 */
internal val JSON_MAPPER = run {
    val dateModule = SimpleModule()
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

/**
 * Helper function to serialize ContentWrapper with content.
 */
private fun serializeWrapperWithContent(gen: JsonGenerator, wrapper: ContentWrapper) {
    val type = wrapper.content.type

    with(gen) {
        when (type) {
            ContentType.TAG, ContentType.QUOTE, ContentType.GENERIC, ContentType.PARAGRAPH -> {
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

                    else -> {
                        skill as SkillStr
                        writeObjectFieldStart(JsonFieldNames.CONTENT)
                        writeStringField(JsonFieldNames.SKILL, skill.skill)
                        writeStringField(JsonFieldNames.FLUENCY, skill.fluency)
                        writeEndObject()
                    }
                }
            }

            else -> writePOJOField(JsonFieldNames.CONTENT, wrapper.content)
        }
    }
}
