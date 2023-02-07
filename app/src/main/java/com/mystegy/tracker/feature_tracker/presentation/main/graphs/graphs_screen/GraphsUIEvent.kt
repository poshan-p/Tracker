package com.mystegy.tracker.feature_tracker.presentation.main.graphs.graphs_screen

import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class GraphsUIEvent{
    data class DeleteGraph(val id: String): GraphsUIEvent()

}
data class GraphsUIState(
    val graphs: List<List<Pair<Tracker, Pair<String, String>>>> = listOf()
)
