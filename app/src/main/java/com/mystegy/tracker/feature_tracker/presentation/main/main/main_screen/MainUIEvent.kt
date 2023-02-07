package com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen

import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class MainUIEvent{
    object ExportDialogVisibility: MainUIEvent()
    data class Tracker(val tracker: com.mystegy.tracker.feature_tracker.domain.models.Tracker): MainUIEvent()
}
data class MainUIState(
    val trackers: List<Tracker> = listOf(),
    val selectedTrackers: List<Tracker> = listOf(),
    val exportDialogVisible: Boolean = false,
    val errorMessage: String = ERROR_MESSAGE
)
