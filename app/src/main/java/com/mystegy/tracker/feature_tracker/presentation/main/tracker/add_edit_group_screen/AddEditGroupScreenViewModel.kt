package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_group_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.feature_tracker.domain.models.GroupAddOrSelect
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
class AddEditGroupScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AddEditGroupUIState(arg = savedStateHandle.navArgs()))
    val uiState = _uiState.asStateFlow()

    init {
        if (_uiState.value.arg.edit) {
            _uiState.value = _uiState.value.copy(name = _uiState.value.arg.groupName)
        }
        getTrackers()
    }

    fun onEvent(event: AddEditGroupUIEvent) {
        when(event) {
            AddEditGroupUIEvent.Done -> {
                insertOrUpdateTracker()
            }
            is AddEditGroupUIEvent.Name -> {
                _uiState.value = _uiState.value.copy(
                    name = event.name,
                    doneButtonEnabled = !_uiState.value.groups.contains(event.name)
                )
            }
            is AddEditGroupUIEvent.GroupAddOrSelectEvent -> {
                if (_uiState.value.groups.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(groupAddOrSelect = event.groupAddOrSelect)
                }
            }
            is AddEditGroupUIEvent.SelectGroup -> {
                _uiState.value = _uiState.value.copy(selectedGroup = event.group)
            }
        }
    }

    private fun insertOrUpdateTracker() {
        if (!uiState.value.arg.edit) {
            if (_uiState.value.groupAddOrSelect == GroupAddOrSelect.SelectExistingGroup) {
                insertTracker(_uiState.value.arg.tracker.copy(group = _uiState.value.selectedGroup))
            } else {
                insertTracker(_uiState.value.arg.tracker.copy(group = _uiState.value.name))
            }
        } else {
            _uiState.value.trackers.forEach { tracker ->
                if (tracker.group == _uiState.value.arg.groupName) {
                    insertTracker(tracker.copy(group = _uiState.value.name))
                }
            }
        }
    }

    private fun getTrackers() =
        repository.getTrackers().onEach { trackers ->
            val groups = trackers.map { it.group }.distinct().toMutableList().apply {
                remove("")
            }
            _uiState.value = _uiState.value.copy(
                trackers = trackers,
                groups = groups,
                groupAddOrSelect = if (groups.isNotEmpty() && !_uiState.value.arg.edit) GroupAddOrSelect.SelectExistingGroup else GroupAddOrSelect.AddNewGroup
            )
        }.launchIn(viewModelScope)

    private fun insertTracker(tracker: Tracker) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTracker(tracker)
        }


}