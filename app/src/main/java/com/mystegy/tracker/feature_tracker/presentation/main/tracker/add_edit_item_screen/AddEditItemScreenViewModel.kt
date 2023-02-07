package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_item_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.core.utils.toLocalDateTime
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import com.mystegy.tracker.feature_tracker.presentation.main.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddEditItemScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AddEditItemUIState(arg = savedStateHandle.navArgs()))
    val uiState = _uiState.asStateFlow()

    init {
        if (uiState.value.arg.edit) {
            _uiState.value = _uiState.value.copy(
                localDate = _uiState.value.arg.item.localDateTime.toLocalDateTime().toLocalDate(),
                localTime = _uiState.value.arg.item.localDateTime.toLocalDateTime().toLocalTime(),
                note = _uiState.value.arg.item.note,
                reps = _uiState.value.arg.item.reps.toString(),
                weight = _uiState.value.arg.item.weight.toString()
            )
        }
    }
    fun onEvent(event: AddEditItemUIEvent) {
        when(event) {
            is AddEditItemUIEvent.Date -> {
                _uiState.value = _uiState.value.copy(localDate = event.localDate)
            }
            AddEditItemUIEvent.Done -> {
                insertTrackerItem()
            }
            is AddEditItemUIEvent.Note -> {
                _uiState.value = _uiState.value.copy(note = event.note)
            }
            is AddEditItemUIEvent.Reps -> {
                _uiState.value = _uiState.value.copy(reps = event.reps)
            }
            is AddEditItemUIEvent.Time -> {
                _uiState.value = _uiState.value.copy(localTime = event.localTime)
            }
            is AddEditItemUIEvent.Weight -> {
                _uiState.value = _uiState.value.copy(weight = event.weight)
            }
        }
    }

    private fun insertTrackerItem() = viewModelScope.launch(Dispatchers.IO) {
        var reps = _uiState.value.reps.toIntOrNull() ?: if (_uiState.value.arg.tracker.hasDefaultValues || _uiState.value.arg.tracker.defaultRep != 1) _uiState.value.arg.tracker.defaultRep else 1
        var weight = _uiState.value.weight.toDoubleOrNull() ?: if (_uiState.value.arg.tracker.hasDefaultValues) _uiState.value.arg.tracker.defaultWeight else 0.0

        reps = if (reps<=0) 1 else reps

        val item = TrackerItem(
            id = if (_uiState.value.arg.edit) _uiState.value.arg.item.id else UUID.randomUUID().toString(),
            reps = reps,
            weight = weight,
            volume = reps.times(weight),
            note = _uiState.value.note,
            localDateTime = LocalDateTime.of(_uiState.value.localDate, _uiState.value.localTime).toString()
        )
        val trackerItems = _uiState.value.arg.tracker.items.toMutableList().apply {
            if (_uiState.value.arg.edit) {
                set(this.indexOf(_uiState.value.arg.item), item)
            } else {
                add(item)
            }
        }.sortedBy { it.localDateTime }.reversed()

        repository.insertTracker(_uiState.value.arg.tracker.copy(items = trackerItems))

    }
}