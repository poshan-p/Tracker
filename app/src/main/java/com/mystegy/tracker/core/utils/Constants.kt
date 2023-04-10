package com.mystegy.tracker.core.utils

import android.R
import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.ramcosta.composedestinations.spec.DestinationStyle
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


const val ERROR_MESSAGE = "Oops, an unexpected error occurred."
const val TAG = "AppTag"

fun LocalDateTime.displayFormat(): String =
    format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))

fun LocalDateTime.displayFormatDayShort(): String =
    format(DateTimeFormatter.ofPattern("E"))

fun LocalTime.displayFormat(): String =
    format(DateTimeFormatter.ofPattern("HH:mm"))

fun LocalDate.displayFormat(): String =
    format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

fun String.toLocalDateTime(): LocalDateTime =
    LocalDateTime.parse(this)


fun String.toLocalDateTime(dateTimeFormatter: DateTimeFormatter): LocalDateTime =
    LocalDateTime.parse(this, dateTimeFormatter)

object Dialog : DestinationStyle.Dialog {
    @OptIn(ExperimentalComposeUiApi::class)
    override val properties = DialogProperties(
        usePlatformDefaultWidth = false
    )
}

fun csvNameGenerator() =
    "Tracker-${LocalDateTime.now().displayFormat()} ${UUID.randomUUID().toString().take(5)}.csv"

fun OutputStream.writeToCSV(trackers: List<Tracker>) {
    val header = "Tracker Title, Timestamp, Reps, Weight, Volume, Note, Group\n"
    write(header.toByteArray())
    var content = ""
    trackers.sortedBy { it.title }.forEach { tracker ->
        tracker.items.forEach { trackerItem ->
            content += "${tracker.title},${
                trackerItem.localDateTime.toLocalDateTime().displayFormat()
            },${trackerItem.reps},${trackerItem.weight},${trackerItem.volume},${trackerItem.note},${tracker.group}\n"
        }
    }
    write(content.toByteArray())
    flush()
    close()
}

fun InputStream.readFromCSV(): Map<String, List<TrackerItemCSV>> {
    val reader = BufferedReader(InputStreamReader(this, Charset.forName("UTF-8")))
    var line = ""
    val trackerItems: ArrayList<TrackerItemCSV> = ArrayList()
    try {
        reader.readLine()
        while (reader.readLine().also { line = it } != null) {
            val tokens = line.split(",".toRegex()).toTypedArray()
            val title = tokens.getOrElse(0) { "" }
            val localDateTime =
                tokens[1].smartLocalDateTimeParse().toString()
            val reps = tokens[2].toIntOrNull() ?: 1
            val weight = tokens[3].toDoubleOrNull() ?: 1.0
            val volume = tokens[4].toDoubleOrNull() ?: 1.0
            val note = tokens.getOrElse(5) { "" }
            val group = tokens.getOrElse(6) { "" }
            trackerItems.add(
                TrackerItemCSV(
                    id = UUID.randomUUID().toString(),
                    reps = reps,
                    weight = weight,
                    volume = volume,
                    note = note,
                    localDateTime = localDateTime,
                    title = title,
                    group = group
                )
            )
        }
    } catch (e: Exception) {
        print(e.stackTrace)
    }
    this.close()
    return trackerItems.groupBy { it.title }
}

data class TrackerItemCSV(
    val id: String = "",
    val reps: Int = 1,
    val weight: Double = 1.0,
    val volume: Double = 1.0,
    val note: String = "",
    val localDateTime: String = "",
    val title: String = "",
    val group: String = ""
)