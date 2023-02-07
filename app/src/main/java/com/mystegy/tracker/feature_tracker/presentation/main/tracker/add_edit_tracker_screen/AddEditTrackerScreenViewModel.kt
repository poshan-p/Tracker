package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_tracker_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import com.mystegy.tracker.feature_tracker.presentation.main.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditTrackerScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTrackerUIState(arg = savedStateHandle.navArgs()))
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AddEditTrackerUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSnackBarShown = MutableSharedFlow<Boolean>()
    val isSnackBarShown = _isSnackBarShown.asSharedFlow()

    fun showSnackBar(show: Boolean, errorMessage: String?){
        _uiState.value = _uiState.value.copy(errorMessage = errorMessage ?: ERROR_MESSAGE)
        viewModelScope.launch {
            _isSnackBarShown.emit(show)
        }
    }

    init {
        if (uiState.value.arg.edit) {
            _uiState.value = _uiState.value.copy(
                title = _uiState.value.arg.tracker.title,
                description = _uiState.value.arg.tracker.description,
                hasDefaultValues = _uiState.value.arg.tracker.hasDefaultValues,
                defaultRep = _uiState.value.arg.tracker.defaultRep.toString(),
                defaultWeight = _uiState.value.arg.tracker.defaultWeight.toString()
            )
        }
    }

    fun onEvent(event: AddEditTrackerUIEvent) {
        when (event) {
            is AddEditTrackerUIEvent.DefaultRep -> {
                _uiState.value = _uiState.value.copy(defaultRep = event.defaultRep)
            }

            is AddEditTrackerUIEvent.DefaultWeight -> {
                _uiState.value = _uiState.value.copy(defaultWeight = event.defaultWeight)
            }

            is AddEditTrackerUIEvent.Description -> {
                _uiState.value = _uiState.value.copy(description = event.description)
            }

            AddEditTrackerUIEvent.Done -> {
                insertTracker()
            }
            AddEditTrackerUIEvent.HasDefaultValues -> {
                _uiState.value =
                    _uiState.value.copy(hasDefaultValues = !uiState.value.hasDefaultValues)
            }

            is AddEditTrackerUIEvent.Title -> {
                _uiState.value = _uiState.value.copy(title = event.title)
            }
        }
    }

    private fun insertTracker() = viewModelScope.launch(Dispatchers.IO) {
        if (_uiState.value.title.isBlank()) {
            showSnackBar(true, "Please enter a title for the tracker")
        } else {
            var defaultReps = _uiState.value.defaultRep.toIntOrNull() ?: 1
            val defaultWeight = _uiState.value.defaultWeight.toDoubleOrNull() ?: 0.0
            if (defaultReps <= 0) {
                defaultReps = 1
            }
            repository.insertTracker(
                Tracker(
                    id = if (_uiState.value.arg.edit) _uiState.value.arg.tracker.id else 0,
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                    items = if (_uiState.value.arg.edit) _uiState.value.arg.tracker.items else listOf(),
                    hasDefaultValues = _uiState.value.hasDefaultValues,
                    defaultRep = defaultReps,
                    defaultWeight = defaultWeight,
                    group = if (_uiState.value.arg.edit) _uiState.value.arg.tracker.group else ""
                )
            )
            _uiEvent.emit(AddEditTrackerUIEvent.Done)
        }

    }
}