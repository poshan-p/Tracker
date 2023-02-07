package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.*
import com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen.components.GroupCard
import com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen.components.TrackerCard
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun TrackerScreen(
    navigator: DestinationsNavigator,
    viewModel: TrackerScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.isSnackBarShown.collectLatest {
            if (it) {
                val result = snackbarHostState.showSnackbar(
                    message = uiState.value.errorMessage
                )
                when (result) {
                    SnackbarResult.Dismissed -> {
                        viewModel.showSnackBar(false, null)
                    }

                    SnackbarResult.ActionPerformed -> {
                        viewModel.showSnackBar(false, null)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        if (uiState.value.trackers.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "No tracker added yet")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(176.dp),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.value.trackers.groupBy { it.group }.entries.forEach { (key, trackers) ->
                    if (key.isBlank()) {
                        items(items = trackers, key = { it.id }) { tracker ->
                            TrackerCard(
                                modifier = Modifier
                                    .width(176.dp)
                                    .animateItemPlacement(),
                                tracker = tracker,
                                onEdit = {
                                    navigator.navigate(
                                        AddEditTrackerScreenDestination(
                                            tracker = tracker,
                                            group = "",
                                            edit = true
                                        )
                                    )
                                },
                                onDelete = {
                                    viewModel.onEvent(TrackerUIEvent.DeleteTracker(tracker.id))
                                },
                                onClick = {
                                    navigator.navigate(TrackerDetailScreenDestination(tracker))
                                },
                                onAdd = {
                                    navigator.navigate(
                                        AddEditItemScreenDestination(
                                            tracker = tracker,
                                            edit = false,
                                            item = TrackerItem()
                                        )
                                    )
                                },
                                onMoveToGroup = {
                                    navigator.navigate(AddEditGroupScreenDestination("", false, tracker))
                                },
                                onRemoveFromGroup = {

                                }
                            )

                        }

                    } else {
                        item {
                            GroupCard(
                                modifier = Modifier
                                    .width(176.dp)
                                    .height(211.dp)
                                    .animateItemPlacement(),
                                trackers = trackers,
                                group = key,
                                track = { index ->
                                    navigator.navigate(
                                        AddEditItemScreenDestination(
                                            tracker = trackers[index],
                                            edit = false,
                                            item = TrackerItem()
                                        )
                                    )
                                },
                                onClick = {
                                    navigator.navigate(GroupTrackersScreenDestination(it))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}