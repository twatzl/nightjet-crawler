package eu.twatzl.njcrawler.util

import kotlinx.datetime.*

fun getTimezone() = TimeZone.currentSystemDefault()

fun getCurrentTime() = Clock.System.now()

fun getFormattedTime(i: Instant): String {
    val t = i.toLocalDateTime(getTimezone())
    return "%04d-%02d-%02d_%02d-%02d".format(t.year, t.monthNumber, t.dayOfMonth, t.hour, t.minute)
}

fun getFormattedDate(i: Instant): String {
    val t = i.toLocalDateTime(getTimezone())
    return "%04d-%02d-%02d".format(t.year, t.monthNumber, t.dayOfMonth)
}

fun getFormattedDate(t: LocalDateTime): String {
    return "%04d-%02d-%02d".format(t.year, t.monthNumber, t.dayOfMonth)
}

fun getNextDay(t: LocalDateTime): LocalDateTime {
    return t.toInstant(getTimezone()).plus(1, DateTimeUnit.DAY, getTimezone()).toLocalDateTime(getTimezone())
}
