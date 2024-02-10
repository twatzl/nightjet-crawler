package eu.twatzl.njcrawler.util

import kotlinx.datetime.*

fun getTimezone() = TimeZone.currentSystemDefault()

fun getCurrentTime() = Clock.System.now()

fun getCurrentDay() = getCurrentTime().toLocalDateTime(getTimezone()).date

fun getFormattedTime(i: Instant): String {
    val t = i.toLocalDateTime(getTimezone())
    return "%04d-%02d-%02d_%02d-%02d".format(t.year, t.monthNumber, t.dayOfMonth, t.hour, t.minute)
}

fun getFormattedDate(i: Instant): String {
    val t = i.toLocalDateTime(getTimezone())
    return "%04d-%02d-%02d".format(t.year, t.monthNumber, t.dayOfMonth)
}
