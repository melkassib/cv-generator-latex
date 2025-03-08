@file:JvmName("Resume")

package com.melkassib.cvgenerator.common.domain

import com.melkassib.cvgenerator.altacv.domain.AltaCVUserInfo
import com.melkassib.cvgenerator.altacv.domain.PhotoShape
import com.melkassib.cvgenerator.altacv.utils.ColorPalette
import com.melkassib.cvgenerator.altacv.utils.PredefinedColorPalette
import com.melkassib.cvgenerator.awesomecv.domain.AwesomeCVUserInfo
import com.melkassib.cvgenerator.awesomecv.domain.ColorTheme
import com.melkassib.cvgenerator.awesomecv.domain.HeaderAlignment
import com.melkassib.cvgenerator.awesomecv.domain.Photo
import com.melkassib.cvgenerator.common.serialization.JSON_MAPPER
import com.melkassib.cvgenerator.common.serialization.YAML_MAPPER

/**
 * Defines the positioning options for the photo in the resume.
 * Can be either LEFT or RIGHT aligned.
 */
enum class PhotoDirection {
    LEFT,
    RIGHT
}

/**
 * Marker interface representing the configuration options for a resume.
 */
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

/**
 * Configuration options for the resume layout and styling.
 *
 * @property colorTheme TThe color palette theme to use (defaults to RED)
 * @property isSectionHighlighted To highlight sections with awesome color
 * @property headerSocialSeparator The social information separator (defaults to pipe |)
 */
data class AwesomeCVConfig @JvmOverloads constructor(
    @JvmField var colorTheme: ColorTheme = ColorTheme.RED,
    @JvmField var isSectionHighlighted: Boolean = true,
    @JvmField var headerSocialSeparator: String = "\\textbar"
) : ResumeConfig

/**
 * Marker interface representing a resume header.
 */
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
    @JvmField var userInfo: AltaCVUserInfo? = null,
    @JvmField var photo: com.melkassib.cvgenerator.altacv.domain.Photo? = null
) : ResumeHeader

/**
 * Represents the header section of the AwesomeCV resume.
 *
 * @property alignment The alignment of the header (defaults to CENTER).
 * @property userInfo Basic information about the resume owner.
 * @property photo The photo configuration.
 * @property quote A quote to be displayed in the header.
 */
data class AwesomeCVHeader @JvmOverloads constructor(
    @JvmField var alignment: HeaderAlignment = HeaderAlignment.CENTER,
    @JvmField var userInfo: AwesomeCVUserInfo? = null,
    @JvmField var photo: Photo? = null,
    @JvmField var quote: String = ""
) : ResumeHeader {
    fun user(init: AwesomeCVUserInfo.() -> Unit) {
        userInfo = AwesomeCVUserInfo().apply(init)
    }
}

/**
 * Marker interface representing a resume footer.
 */
sealed interface ResumeFooter

/**
 * Represents the footer section of the resume.
 *
 * @property left The left-aligned footer text
 * @property center The center-aligned footer text
 * @property right The right-aligned footer text
 */
data class AwesomeCVFooter @JvmOverloads constructor(
    @JvmField var left: String = "",
    @JvmField var center: String = "",
    @JvmField var right: String = ""
) : ResumeFooter

/**
 * Represents a resume footer with no content.
 */
data object NoFooter : ResumeFooter

/**
 * Abstract class representing a generic resume.
 *
 * @param C The type of the resume configuration.
 * @param H The type of the resume header.
 * @param F The type of the resume footer.
 * @property config The configuration options for the resume layout and styling.
 * @property header The header section containing tagline, user info, and photo.
 * @property footer The footer section containing info displayed at the bottom of the resume.
 * @property sections The list of sections containing resume content.
 */
abstract class Resume<C : ResumeConfig, H : ResumeHeader, F : ResumeFooter>(
    open val config: C,
    open val header: H,
    val footer: F,
    open val sections: List<Section>
) {
    /**
     * Converts the resume to a LaTeX string.
     *
     * @return The LaTeX string representation of the resume.
     */
    abstract fun toLaTeX(): String

    /**
     * Converts the resume object to a JSON string.
     *
     * @return A JSON string representation of the resume
     */
    fun toJson(): String = JSON_MAPPER.writeValueAsString(this)

    /**
     * Converts the resume object to a YAML string.
     *
     * @return A YAML string representation of the resume
     */
    fun toYaml(): String = YAML_MAPPER.writeValueAsString(this)
}
