package com.melkassib.cvgenerator.common.domain

import com.melkassib.cvgenerator.altacv.domain.PhotoShape
import com.melkassib.cvgenerator.altacv.domain.Section
import com.melkassib.cvgenerator.altacv.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.altacv.utils.ColorPalette
import com.melkassib.cvgenerator.altacv.utils.PredefinedColorPalette
import com.melkassib.cvgenerator.awesomecv.domain.*

sealed interface ResumeConfig

/**
 * Configuration options for the resume layout and styling.
 *
 * @property columnRatio The ratio between left and right columns (defaults to 0.6)
 * @property photoShape The shape style for photos (defaults to NORMAL)
 * @property theme The color palette theme to use (defaults to THEME1)
 */
data class AltaCVConfig @JvmOverloads constructor(
    @JvmField var columnRatio: Double = 0.6,
    @JvmField var photoShape: PhotoShape = PhotoShape.NORMAL,
    @JvmField var theme: ColorPalette = PredefinedColorPalette.THEME1
) : ResumeConfig

data class AwesomeCVConfig @JvmOverloads constructor(
    @JvmField var colorTheme: ColorTheme = ColorTheme.RED,
    @JvmField var isSectionHighlighted: Boolean = true,
    @JvmField var headerSocialSeparator: String = "\\textbar"
) : ResumeConfig

sealed interface ResumeHeader

/**
 * Represents the header section of the resume.
 *
 * @property tagline A brief description
 * @property userInfo Basic information about the resume owner
 * @property photo The photo configuration
 */
data class AltaCVHeader @JvmOverloads constructor(
    @JvmField var tagline: String = "",
    @JvmField var userInfo: com.melkassib.cvgenerator.altacv.domain.UserInfo? = null,
    @JvmField var photo: com.melkassib.cvgenerator.altacv.domain.Photo? = null
) : ResumeHeader

data class AwesomeCVHeader @JvmOverloads constructor(
    @JvmField var alignment: HeaderAlignment = HeaderAlignment.CENTER,
    @JvmField var userInfo: UserInfo? = null,
    @JvmField var photo: Photo? = null,
    @JvmField var quote: String = ""
) : ResumeHeader {
    fun user(init: UserInfo.() -> Unit) {
        userInfo = UserInfo().apply(init)
    }
}

sealed interface ResumeFooter

data class AwesomeCVFooter @JvmOverloads constructor(
    @JvmField var left: String = "",
    @JvmField var center: String = "",
    @JvmField var right: String = ""
) : ResumeFooter

data object NoFooter : ResumeFooter

abstract class Resume<C : ResumeConfig, H : ResumeHeader, F : ResumeFooter>(
    open val config: C,
    open val header: H,
    val footer: F,
    open val sections: List<Section>
) {
    abstract fun toLaTeX(): String

    /**
     * Converts the resume object to a JSON string.
     *
     * @return A JSON string representation of the resume
     */
    fun toJson(): String = JSON_MAPPER.writeValueAsString(this)
}
