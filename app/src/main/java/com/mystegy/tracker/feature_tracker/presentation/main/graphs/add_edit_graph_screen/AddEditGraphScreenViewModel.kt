package com.mystegy.tracker.feature_tracker.presentation.main.graphs.add_edit_graph_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgsParcelable
import com.mystegy.tracker.feature_tracker.domain.models.Graph
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import com.mystegy.tracker.feature_tracker.presentation.main.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddEditGraphScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditGraphUIState(arg = (savedStateHandle.navArgs() as AddEditGraphScreenArgsParcelable).arg))
    val uiState = _uiState.asStateFlow()

    init {
        getTrackers()
        if (_uiState.value.arg.edit) {
            _uiState.value =
                _uiState.value.copy(selectedTrackers = _uiState.value.selectedTrackers.toMutableList().apply {
                    addAll(_uiState.value.arg.trackers)
                },
                title = _uiState.value.arg.title)
        }
    }

    fun onEvent(event: AddEditGraphUIEvent) {
        when (event) {
            is AddEditGraphUIEvent.AddRemoveTracker -> {
                _uiState.value = _uiState.value.copy(
                    selectedTrackers = _uiState.value.selectedTrackers.toMutableList().apply {
                        if (this.contains(event.tracker)) {
                            remove(event.tracker)
                        } else if (this.size < 5) {
                            add(event.tracker)
                        }
                    }
                )
            }
            AddEditGraphUIEvent.Done -> {
                insertGraph()
            }
            is AddEditGraphUIEvent.Title -> {
                _uiState.value = _uiState.value.copy(title = event.title)
            }
        }
    }

    private fun getTrackers() =
        repository.getTrackers().onEach { trackers ->
            _uiState.value = _uiState.value.copy(trackers = trackers)
        }.launchIn(viewModelScope)

    private fun insertGraph() = viewModelScope.launch(Dispatchers.Default) {
        if (_uiState.value.arg.edit) {
            repository.deleteGraphs(
                _uiState.value.arg.graphID
            )
        }
        val id = if (_uiState.value.arg.edit) _uiState.value.arg.graphID else UUID.randomUUID().toString()
        repository.insertGraphs(_uiState.value.selectedTrackers.map { tracker ->
            Graph(
                id = 0,
                graphID = id,
                trackerID = tracker.id,
                title = _uiState.value.title
            )
        })
    }


}