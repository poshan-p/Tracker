package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen

import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class TrackerUIEvent {
    data class DeleteTracker(val id: Long): TrackerUIEvent()
    data class SelectTrackerDisplayType(val trackerDisplayType: TrackerDisplayType): TrackerUIEvent()
}
data class TrackerUIState(
    val trackers: List<Tracker> = listOf(),
    val errorMessage: String = ERROR_MESSAGE,
    val trackerDisplayType: TrackerDisplayType = TrackerDisplayType.All
)

enum class TrackerDisplayType {
    All,
    Trackers,
    Groups
}
