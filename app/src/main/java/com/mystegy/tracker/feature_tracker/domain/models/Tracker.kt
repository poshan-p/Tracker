package com.mystegy.tracker.feature_tracker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tracker(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val items: List<TrackerItem> = listOf(),
    val hasDefaultValues: Boolean = false,
    val defaultRep: Int = 1,
    val defaultWeight: Double = 1.0,
    val group: String = ""
): Parcelable
