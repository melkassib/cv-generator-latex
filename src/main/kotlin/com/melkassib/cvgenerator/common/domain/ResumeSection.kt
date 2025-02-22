@file:JvmName("Section")

package com.melkassib.cvgenerator.common.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.melkassib.cvgenerator.common.serialization.SectionContentSerializers

/**
 * Represents the position of a section in the resume.
 *
 * @property column The column number (1 or 2) where this section should be placed
 * @property order The vertical ordering position within the column
 * @throws IllegalArgumentException if column number is different from 1 or 2
 */
data class SectionPosition(val column: Int, val order: Int) {
    init {
        require(column in 1..2) {
            "column position: should be 1 or 2"
        }
    }
}

/**
 * Represents a section in the resume with a title, position, and contents.
 *
 * @property title The title of the section
 * @property position The [SectionPosition] indicating where this section should be placed
 * @property contents The list of [SectionContent] elements in this section
 * @property ignored Whether this section should be ignored when generating the resume
 */
data class Section @JvmOverloads constructor(
    val title: String,
    val position: SectionPosition,
    @JsonSerialize(using = SectionContentSerializers.ContentListSerializer::class)
    @JsonDeserialize(using = SectionContentSerializers.ContentListDeserializer::class)
    val contents: List<SectionContent> = listOf(),
    @JsonIgnore val ignored: Boolean = false
)
