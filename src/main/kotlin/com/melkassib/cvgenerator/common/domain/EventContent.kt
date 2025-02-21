package com.melkassib.cvgenerator.common.domain

import com.melkassib.cvgenerator.common.utils.SectionEventDuration
import java.time.LocalDate

/**
 * Represents a period of time for an event.
 * Can be either a string representing a date range or a date range object.
 */
sealed interface EventPeriod

/**
 * Represents a duration of an event.
 * The date must be in the format yyyy-MM.
 *
 * @property date The date of the event duration in yyyy-MM format.
 * @throws IllegalArgumentException if the date format is invalid.
 */
@JvmInline
private value class EventDuration(val date: String) {
    init {
        require(date.matches(Regex("\\d{4}-\\d{2}"))) {
            "Invalid date format: $date. Expected format: yyyy-MM"
        }
    }
}

/**
 * Represents a period of time for an event as a string.
 * This class provides a way to represent event durations using string dates.
 *
 * @property start The start date of the event period in yyyy-MM format
 * @property end The end date of the event period in yyyy-MM format, defaults to empty string
 *
 * @see EventPeriod
 * @see EventDuration
 */
class EventPeriodString private constructor(val start: String, val end: String = "") : EventPeriod {
    companion object {
        /**
         * Creates an instance of EventPeriodString.
         *
         * @param from The start date of the event period in yyyy-MM format.
         * @param to The end date of the event period in yyyy-MM format, defaults to empty string.
         * @return An instance of EventPeriodString.
         */
        @JvmStatic
        @JvmOverloads
        fun eventDurationStr(from: String, to: String = "") = EventPeriodString(from, to)
    }
}

/**
 * Represents a period of time for an event as LocalDate objects.
 * The dates must be in the format yyyy-MM.
 *
 * @property start The start date of the event period
 * @property end The end date of the event period, defaults to current date
 */
class EventPeriodDate private constructor(val start: LocalDate, val end: LocalDate = LocalDate.now()) : EventPeriod {
    companion object {
        /**
         * Creates an instance of EventPeriodDate.
         *
         * @param from The start date of the event period in yyyy-MM format.
         * @param to The end date of the event period in yyyy-MM format.
         * @return An instance of EventPeriodDate.
         */
        @JvmStatic
        fun eventDurationDate(from: String, to: String): EventPeriodDate {
            val startDate = LocalDate.parse("${EventDuration(from).date}-01", SectionEventDuration.DATE_PATTERN)
            val endDate = LocalDate.parse("${EventDuration(to).date}-01", SectionEventDuration.DATE_PATTERN)
            return EventPeriodDate(startDate, endDate)
        }
    }
}

/**
 * Represents a period of time for an event as no period.
 */
data object NoEventPeriod : EventPeriod
