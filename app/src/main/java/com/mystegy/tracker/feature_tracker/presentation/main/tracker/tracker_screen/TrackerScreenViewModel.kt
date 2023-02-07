package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrackerScreenViewModel @Inject constructor(
    private val repository: TrackerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TrackerUIState())
    val uiState = _uiState.asStateFlow()

    private val _isSnackBarShown = MutableSharedFlow<Boolean>()
    val isSnackBarShown = _isSnackBarShown.asSharedFlow()

    fun showSnackBar(show: Boolean, errorMessage: String?){
        _uiState.value = _uiState.value.copy(errorMessage = errorMessage ?: ERROR_MESSAGE)
        viewModelScope.launch {
            _isSnackBarShown.emit(show)
        }
    }

    init {
        getTrackers()
    }

    fun onEvent(event: TrackerUIEvent) {
        when(event) {
            is TrackerUIEvent.DeleteTracker -> {
                deleteTracker(event.id)
            }
        }
    }

    private fun getTrackers() =
        repository.getTrackers().onEach { trackers ->
            _uiState.value = _uiState.value.copy(trackers = trackers)
        }.launchIn(viewModelScope)

    private fun deleteTracker(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTracker(id)
        showSnackBar(true, "Tracker deleted")
    }

}