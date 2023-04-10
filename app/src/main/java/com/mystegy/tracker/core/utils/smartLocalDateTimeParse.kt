package com.mystegy.tracker.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.smartLocalDateTimeParse(): LocalDateTime {
    val dtmStr = replace("/", "-")

    val (dt,tm) = dtmStr.split(" ")

    return dt.smartLocalDateParse().atTime(tm.smartLocalTimeParse())
}

fun String.smartLocalDateParse(): LocalDate {
    val dtStr = replace("/", "-")

    return when {
        dtStr.matches(Regex("[0-9]-[0-9]-[0-9]{2}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("d-M-yy"))

        dtStr.matches(Regex("[0-9]{2}-[0-9]-[0-9]{2}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("dd-M-yy"))

        dtStr.matches(Regex("[0-9]-[0-9]-[0-9]{4}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("d-M-yyyy"))

        dtStr.matches(Regex("[0-9]{2}-[0-9]-[0-9]{4}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("dd-M-yyyy"))



        dtStr.matches(Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        dtStr.matches(Regex("[0-9]{2}-[0-9]{2}-[0-9]{4}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        dtStr.matches(Regex("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-M-d"))

        dtStr.matches(Regex("[0-9]{1,2}-[0-9]{1,2}-[0-9]{4}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("d-M-yyyy"))

        dtStr.matches(Regex("[0-9]{2}-[0-9]{2}-[0-9]{2}")) ->
            LocalDate.parse(dtStr, DateTimeFormatter.ofPattern("dd-MM-yy"))

        else -> throw Exception("Unrecognized date format: $this")
    }
}

fun String.smartLocalTimeParse() = when {

    matches(Regex("[0-9]{2}:[0-9]{2}")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))

    matches(Regex("[0-9]:[0-9]{2}")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("H:mm"))

    matches(Regex("[0-9]{4}")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("HHmm"))


    matches(Regex("[0-9]{2}:[0-9]{2} (AM|PM)")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm aa"))

    matches(Regex("[0-9]:[0-9]{2} (AM|PM)")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("H:mm aa"))

    matches(Regex("[0-9]{4} (AM|PM)")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("HHmm aa"))

    matches(Regex("[0-9]{3}")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("Hmm"))

    matches(Regex("[0-9]{2}")) ->
        LocalTime.parse("00$this", DateTimeFormatter.ofPattern("HHmm"))

    matches(Regex("[0-9]")) ->
        LocalTime.parse("000$this", DateTimeFormatter.ofPattern("HHmm"))

    matches(Regex("[0-9]{2}:[0-9]{2}:[0-9]{2}")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm:ss"))

    matches(Regex("[0-9]:[0-9]{2}:[0-9]{2}")) ->
        LocalTime.parse(this, DateTimeFormatter.ofPattern("H:mm:ss"))

    else -> throw Exception("Unrecognized time format: $this")
}