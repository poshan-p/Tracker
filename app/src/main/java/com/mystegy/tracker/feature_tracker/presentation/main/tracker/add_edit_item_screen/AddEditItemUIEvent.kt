package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_item_screen

import com.mystegy.tracker.feature_tracker.domain.models.AddEditItemScreenArgs
import java.time.LocalDate
import java.time.LocalTime

sealed class AddEditItemUIEvent{
    data class Date(val localDate: LocalDate): AddEditItemUIEvent()
    data class Time(val localTime: LocalTime): AddEditItemUIEvent()
    data class Note(val note: String): AddEditItemUIEvent()
    data class Reps(val reps: String): AddEditItemUIEvent()
    data class Weight(val weight: String): AddEditItemUIEvent()
    object Done: AddEditItemUIEvent()
}
data class AddEditItemUIState(
    val arg: AddEditItemScreenArgs,
    val localDate: LocalDate = LocalDate.now(),
    val localTime: LocalTime = LocalTime.now(),
    val note: String = "",
    val reps: String = "",
    val weight: String = ""
)
