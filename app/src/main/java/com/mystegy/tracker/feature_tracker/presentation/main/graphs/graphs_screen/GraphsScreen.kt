package com.mystegy.tracker.feature_tracker.presentation.main.graphs.graphs_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgsParcelable
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditGraphScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.graphs.graphs_screen.components.GraphCard
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun GraphsScreen(
    navigator: DestinationsNavigator,
    viewModel: GraphsScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        if (uiState.value.graphs.isEmpty()) {
            Box(
                modifier = androidx.compose.ui.Modifier
                    .padding(paddingValues)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "No graph added yet")
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.padding(paddingValues), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(uiState.value.graphs) { graph ->
                    GraphCard(
                        trackers = graph.map { it.first },
                        title = if (graph.isNotEmpty()) graph.map { it.second.second }
                            .first() else "",
                        onEdit = {
                            navigator.navigate(
                                AddEditGraphScreenDestination(
                                    AddEditGraphScreenArgsParcelable(
                                        AddEditGraphScreenArgs(
                                            graphID = if (graph.isNotEmpty()) graph.map { it.second.first }
                                                .first() else "",
                                            trackers = graph.map { it.first },
                                            edit = true,
                                            title = if (graph.isNotEmpty()) graph.map { it.second.second }
                                                .first() else ""
                                        )
                                    )
                                )
                            )
                        },
                        onDelete = {
                            viewModel.onEvent(GraphsUIEvent.DeleteGraph(if (graph.isNotEmpty()) graph.map { it.second.first }
                                .first() else ""))
                        }
                    )
                }
            }
        }
    }
}