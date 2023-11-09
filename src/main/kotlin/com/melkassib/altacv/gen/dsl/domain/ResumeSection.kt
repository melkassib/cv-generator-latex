@file:JvmName("Section")

package com.melkassib.altacv.gen.dsl.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.altacv.gen.dsl.serialization.SectionContentSerializers

data class SectionPosition(val column: Int, val order: Int) {
    init {
        require(column in 1..2) {
            "column position: should be 1 or 2"
        }
    }
}

class Section @JvmOverloads constructor(
    val title: String,
    val position: SectionPosition,
    @JsonSerialize(using = SectionContentSerializers.ContentListSerializer::class)
    @JsonDeserialize(using = SectionContentSerializers.ContentListDeserializer::class)
    val contents: List<SectionContent> = listOf(),
    @JsonIgnore val ignored: Boolean = false
)
