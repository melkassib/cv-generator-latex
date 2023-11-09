package com.melkassib.altacv.gen.dsl.domain

import com.melkassib.altacv.gen.dsl.utils.SectionEventDuration
import java.time.LocalDate

sealed interface EventPeriod

@JvmInline
private value class EventDuration(val date: String) {
    init {
        require(date.matches(Regex("\\d{4}-\\d{2}"))) {
            "Invalid date format: $date. Expected format: yyyy-MM"
        }
    }
}

class EventPeriodString private constructor(val start: String, val end: String = "") : EventPeriod {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun eventDurationStr(from: String, to: String = "") = EventPeriodString(from, to)
    }
}

class EventPeriodDate private constructor(val start: LocalDate, val end: LocalDate = LocalDate.now()) : EventPeriod {
    companion object {
        @JvmStatic
        fun eventDurationDate(from: String, to: String): EventPeriodDate {
            val startDate = LocalDate.parse("${EventDuration(from).date}-01", SectionEventDuration.DATE_PATTERN)
            val endDate = LocalDate.parse("${EventDuration(to).date}-01", SectionEventDuration.DATE_PATTERN)
            return EventPeriodDate(startDate, endDate)
        }
    }
}

data object NoEventPeriod : EventPeriod
