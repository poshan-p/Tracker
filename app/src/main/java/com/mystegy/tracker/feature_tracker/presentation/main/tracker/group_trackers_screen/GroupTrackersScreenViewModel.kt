package com.mystegy.tracker.feature_tracker.presentation.main.tracker.group_trackers_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
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
class GroupTrackersScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GroupTrackersUIState(arg = savedStateHandle.navArgs()))
    val uiState = _uiState.asStateFlow()

    init {
        getTrackers()
    }

    fun onEvent(event: GroupTrackersUIEvent) {
        when(event) {
            GroupTrackersUIEvent.DeleteTrackers -> {
                _uiState.value.trackers.forEach { tracker ->
                    deleteTrackers(tracker)
                }
            }
            GroupTrackersUIEvent.GroupDeleteDialogVisibility -> {
                _uiState.value = _uiState.value.copy(groupDeleteDialogVisible = !_uiState.value.groupDeleteDialogVisible)
            }
            is GroupTrackersUIEvent.RemoveTracker -> {
                removeTrackers(event.tracker)
            }
            GroupTrackersUIEvent.RemoveTrackers -> {
                _uiState.value.trackers.forEach { tracker ->
                    removeTrackers(tracker)
                }
            }
            is GroupTrackersUIEvent.DeleteTracker -> {
                deleteTrackers(event.tracker)
            }
            is GroupTrackersUIEvent.GroupName -> {
                _uiState.value = _uiState.value.copy(arg = _uiState.value.arg.copy(group = event.groupName))
            }
        }
    }
    fun setEdit(edit: Boolean) {
        _uiState.value = _uiState.value.copy(edit = edit)
    }

    private fun getTrackers() =
        repository.getTrackers().onEach { trackers ->
            _uiState.value = _uiState.value.copy(trackers = trackers.filter { it.group.getOrNull(_uiState.value.arg.index) == _uiState.value.arg.group }.sortedBy { it.sort })
        }.launchIn(viewModelScope)

    private fun deleteTrackers(tracker: Tracker) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTracker(tracker.id)
        }

    private fun removeTrackers(tracker: Tracker) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTracker(tracker = tracker.copy(group = tracker.group.toMutableList().apply { removeLast() }))
        }
}