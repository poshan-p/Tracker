package com.mystegy.tracker.feature_tracker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class TrackerItem(
    val id: String = "",
    val reps: Int = 1,
    val weight: Double = 1.0,
    val volume: Double = 1.0,
    val note: String = "",
    val localDateTime: String = ""
): Parcelable
