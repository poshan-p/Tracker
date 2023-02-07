package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_tracker_screen

import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenArgs

sealed class AddEditTrackerUIEvent {
    data class Title(val title: String): AddEditTrackerUIEvent()
    data class Description(val description: String): AddEditTrackerUIEvent()
    data class DefaultRep(val defaultRep: String): AddEditTrackerUIEvent()
    data class DefaultWeight(val defaultWeight: String): AddEditTrackerUIEvent()
    object HasDefaultValues: AddEditTrackerUIEvent()
    object Done: AddEditTrackerUIEvent()
}
data class AddEditTrackerUIState(
    val arg: AddEditTrackerScreenArgs,
    val title: String = "",
    val description: String = "",
    val hasDefaultValues: Boolean = false,
    val defaultRep: String = "",
    val defaultWeight: String = "",
    val errorMessage: String = ERROR_MESSAGE
)