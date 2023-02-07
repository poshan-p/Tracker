package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.feature_tracker.domain.models.TrackerDetailScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import com.mystegy.tracker.feature_tracker.presentation.main.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrackerDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TrackerDetailUIState(arg = savedStateHandle.navArgs(), tracker = (savedStateHandle.navArgs() as TrackerDetailScreenArgs).tracker))
    val uiState = _uiState.asStateFlow()

    init {
        getTracker(_uiState.value.arg.tracker.id)
    }

    fun onEvent(event: TrackerDetailUIEvent) {
        when(event) {
            is TrackerDetailUIEvent.DeleteItem -> {
                if (event.delete) {
                    deleteItem(_uiState.value.trackerItemToBeDeleted)
                }
                _uiState.value = _uiState.value.copy(deleteDialogVisible = !_uiState.value.deleteDialogVisible, trackerItemToBeDeleted = event.trackerItem)
            }
        }
    }

    private fun getTracker(id: Long) =
        repository.getTracker(id).onEach { tracker ->
            _uiState.value = _uiState.value.copy(tracker = tracker)
        }.launchIn(viewModelScope)

    private fun deleteItem(item: TrackerItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTracker(
            _uiState.value.tracker.copy(
                items = _uiState.value.tracker.items.toMutableList().apply {
                    remove(item)
                }
            )
        )
    }
}