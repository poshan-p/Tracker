package com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.core.utils.readFromCSV
import com.mystegy.tracker.core.utils.writeToCSV
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUIState())
    val uiState = _uiState.asStateFlow()

    private val _isSnackBarShown = MutableSharedFlow<Boolean>()
    val isSnackBarShown = _isSnackBarShown.asSharedFlow()

    fun showSnackBar(show: Boolean, errorMessage: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = errorMessage ?: ERROR_MESSAGE)
        viewModelScope.launch {
            _isSnackBarShown.emit(show)
        }
    }

    init {
        getTrackers()
    }

    fun onEvent(event: MainUIEvent) {
        when (event) {
            is MainUIEvent.ExportDialogVisibility -> {
                _uiState.value =
                    _uiState.value.copy(exportDialogVisible = !_uiState.value.exportDialogVisible)
            }

            is MainUIEvent.Tracker -> {
                _uiState.value = _uiState.value.copy(
                    selectedTrackers = _uiState.value.selectedTrackers.toMutableList().apply {
                        if (_uiState.value.selectedTrackers.contains(event.tracker)) {
                            remove(event.tracker)
                        } else {
                            add(event.tracker)
                        }
                    }
                )

            }

            MainUIEvent.SelectAllTracker -> {
                _uiState.value = _uiState.value.copy(
                    selectedTrackers = _uiState.value.trackers
                )
            }

            MainUIEvent.ClearAllSelectedTracker -> {
                _uiState.value = _uiState.value.copy(
                    selectedTrackers = emptyList()
                )
            }
        }
    }

    private fun getTrackers() =
        repository.getTrackers().onEach { trackers ->
            _uiState.value = _uiState.value.copy(trackers = trackers)
        }.launchIn(viewModelScope)

    fun readFromCSV(inputStream: InputStream) = viewModelScope.launch(Dispatchers.Default) {
        val result = inputStream.readFromCSV()
        var group = ""
        result.forEach { value ->
            insertTracker(
                Tracker(
                    id = 0,
                    title = value.key,
                    description = "",
                    items = value.value.map {
                        group = it.group
                        TrackerItem(
                            id = it.id,
                            reps = it.reps,
                            weight = it.weight,
                            volume = it.volume,
                            note = it.note,
                            localDateTime = it.localDateTime
                        )
                    },
                    hasDefaultValues = false,
                    defaultRep = 1,
                    defaultWeight = 1.0,
                    group = group
                )
            )
        }
    }

    fun writeToCSV(outputStream: OutputStream) = viewModelScope.launch(Dispatchers.Default) {
        outputStream.writeToCSV(_uiState.value.selectedTrackers)
    }

    private suspend fun insertTracker(tracker: Tracker) {
        repository.insertTracker(tracker)
    }
}