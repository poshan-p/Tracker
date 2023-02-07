package com.mystegy.tracker.feature_tracker.presentation.main.graphs.graphs_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.feature_tracker.domain.models.GraphCustom
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GraphsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GraphsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getGraphs()
    }

    fun onEvent(event: GraphsUIEvent) {
        when(event) {
            is GraphsUIEvent.DeleteGraph -> deleteGraph(event.id)
        }
    }

    private fun getGraphs() =
        repository.getTrackerAndGraph().onEach { trackerAndGraphs ->
            _uiState.value = _uiState.value.copy(
                graphs = trackerAndGraphs.map { trackerAndGraph ->
                    trackerAndGraph.graphs.map {
                        GraphCustom(
                            id = it.id,
                            graphID = it.graphID,
                            tracker = trackerAndGraph.tracker,
                            title = it.title
                        )
                    }
                }.flatten().sortedBy { it.graphID }.groupBy { it.graphID }.map { (graphID, items) -> items.map { it.tracker to (graphID to it.title) } }
            )
        }.launchIn(viewModelScope)

    private fun deleteGraph(id: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteGraphs(id)
    }
}