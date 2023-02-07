package com.mystegy.tracker.feature_tracker.presentation.main.graphs.add_edit_graph_screen

import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class AddEditGraphUIEvent{
    data class AddRemoveTracker(val tracker: Tracker): AddEditGraphUIEvent()
    data class Title(val title: String): AddEditGraphUIEvent()
    object Done: AddEditGraphUIEvent()
}
data class AddEditGraphUIState(
    val arg: AddEditGraphScreenArgs,
    val selectedTrackers: List<Tracker> = listOf(),
    val trackers: List<Tracker> = listOf(),
    val title: String = ""
)
