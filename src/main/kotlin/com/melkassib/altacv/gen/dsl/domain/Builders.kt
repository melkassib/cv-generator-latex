@file:JvmName("ResumeBuilders")

package com.melkassib.altacv.gen.dsl.domain

import com.melkassib.altacv.gen.dsl.utils.separateWith

fun resume(init: ResumeBuilder.() -> Unit) = ResumeBuilder().apply(init).build()

class ResumeBuilder {
    private var _config = ResumeConfig()
    private var _header = ResumeHeader()
    private var _sections = emptyList<Section>()

    fun config(init: ResumeConfig.() -> Unit) {
        _config = ResumeConfig().apply(init)
    }

    fun header(init: ResumeHeader.() -> Unit) {
        _header = ResumeHeader().apply(init)
    }

    fun sections(init: SectionListBuilder.() -> Unit) {
        _sections = SectionListBuilder().apply(init).build()
    }

    fun build() = Resume(_config, _header, _sections)
}

class SectionListBuilder {
    private val _sections = mutableListOf<Section>()

    @JvmOverloads
    fun section(
        title: String,
        position: SectionPosition,
        separator: SectionContent = NoContent,
        ignored: Boolean = false,
        init: SectionBuilder.() -> Unit
    ) {
        val contents = SectionBuilder().apply(init).build().separateWith(separator)
        _sections += Section(title, position, contents, ignored)
    }

    fun build() = _sections.toList()
}

class SectionBuilder {
    private val _contents = mutableListOf<SectionContent>()

    fun contents(init: SectionContentBuilder.() -> Unit) {
        _contents += SectionContentBuilder().apply(init).build()
    }

    fun build() = _contents.toList()
}

class SectionContentBuilder {
    private val _contents = mutableListOf<SectionContent>()

    fun content(item: SectionContent) {
        _contents += item
    }

    fun content(value: String) {
        _contents += LatexContent(value)
    }

    fun event(title: String, init: Event.() -> Unit) {
        _contents += Event.create(title, init)
    }

    fun tag(title: String) {
        _contents += Tag(title)
    }

    fun quote(title: String) {
        _contents += Quote(title)
    }

    fun skill(title: String, fluency: String) {
        _contents += SkillStr(title, fluency)
    }

    fun skill(title: String, rating: Double) {
        _contents += Skill(title, rating)
    }

    fun achievement(iconName: String, achievement: String, detail: String) {
        _contents += Achievement(iconName, achievement, detail)
    }

    fun wheelchart(innerRadius: Double, outerRadius: Double, init: WheelChartBuilder.() -> Unit) {
        val wheelChartItems = WheelChartBuilder().apply(init).build()
        _contents += WheelChart(innerRadius, outerRadius, wheelChartItems)
    }

    fun build() = _contents.toList()
}

class WheelChartBuilder {
    private val _items = mutableListOf<WheelChartItem>()

    fun item(value: Int, textWidth: Int, color: String, detail: String) {
        _items += WheelChartItem(value, textWidth, color, detail)
    }

    fun build() = _items.toList()
}
