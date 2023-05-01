package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenWrapperArg
import com.mystegy.tracker.feature_tracker.domain.models.GroupTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.ReorderTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditGroupScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditItemScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditTrackerScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.GroupTrackersScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.ReorderScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.TrackerDetailScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen.components.GroupCard
import com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen.components.TrackerCard
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(176.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TrackerDisplayType.values().forEach { displayType ->
                            FilterChip(
                                selected = uiState.value.trackerDisplayType == displayType,
                                onClick = {
                                    viewModel.onEvent(
                                        TrackerUIEvent.SelectTrackerDisplayType(displayType)
                                    )
                                },
                                label = {
                                    Text(text = displayType.name)
                                },
                                leadingIcon = {
                                    if (uiState.value.trackerDisplayType == displayType)
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = null
                                        )
                                }
                            )
                        }
                        FilterChip(
                            selected = true,
                            onClick = {
                                navigator.navigate(ReorderScreenDestination(
                                    ReorderTrackersScreenArgs(
                                        0,
                                        ""
                                    )
                                ))
                            },
                            label = {
                                Text(text = "Reorder")
                            }
                        )
                    }
                }
                when (uiState.value.trackerDisplayType) {
                    TrackerDisplayType.All -> {
                        uiState.value.trackers.sortedBy { it.sort }.groupBy {
                            if (it.group.isEmpty()) {
                                it.id
                            } else {
                                it.group.first()
                            }
                        }.forEach { (key, trackers) ->
                            if (key is Long) {
                                item {
                                    TrackerCard(
                                        modifier = Modifier
                                            .width(176.dp),
                                        tracker = trackers.first(),
                                        onEdit = {
                                            navigator.navigate(
                                                AddEditTrackerScreenDestination(
                                                    AddEditTrackerScreenArgs(
                                                        AddEditTrackerScreenWrapperArg(
                                                            tracker = trackers.first(),
                                                            group = "",
                                                            insertFromGroup = listOf(),
                                                            edit = true
                                                        )
                                                    )
                                                )
                                            )
                                        },
                                        onDelete = {
                                            viewModel.onEvent(TrackerUIEvent.DeleteTracker(trackers.first().id))
                                        },
                                        onClick = {
                                            navigator.navigate(
                                                TrackerDetailScreenDestination(
                                                    trackers.first()
                                                )
                                            )
                                        },
                                        onAdd = {
                                            navigator.navigate(
                                                AddEditItemScreenDestination(
                                                    tracker = trackers.first(),
                                                    edit = false,
                                                    item = TrackerItem()
                                                )
                                            )
                                        },
                                        onMoveToGroup = {
                                            navigator.navigate(
                                                AddEditGroupScreenDestination(
                                                    "",
                                                    false,
                                                    trackers.first(),
                                                    0
                                                )
                                            )
                                        },
                                        onRemoveFromGroup = {

                                        }
                                    )
                                }

                            } else {
                                item {
                                    GroupCard(
                                        modifier = Modifier
                                            .width(176.dp),
                                        trackers = trackers,
                                        group = key as String,
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
                                            navigator.navigate(GroupTrackersScreenDestination(
                                                GroupTrackersScreenArgs(
                                                    0,
                                                    it
                                                )
                                            ))

                                        }
                                    )
                                }
                            }
                        }
                    }

                    TrackerDisplayType.Trackers -> {
                        items(uiState.value.trackers.filter { it.group.isEmpty() }
                            .sortedBy { it.sort }, key = { it.id }) { tracker ->
                            TrackerCard(
                                modifier = Modifier
                                    .width(176.dp),
                                tracker = tracker,
                                onEdit = {
                                    navigator.navigate(
                                        AddEditTrackerScreenDestination(
                                            AddEditTrackerScreenArgs(
                                                AddEditTrackerScreenWrapperArg(
                                                    tracker = tracker,
                                                    group = "",
                                                    insertFromGroup = listOf(),
                                                    edit = true
                                                )
                                            )
                                        )
                                    )
                                },
                                onDelete = {
                                    viewModel.onEvent(
                                        TrackerUIEvent.DeleteTracker(
                                            tracker.id
                                        )
                                    )
                                },
                                onClick = {
                                    navigator.navigate(
                                        TrackerDetailScreenDestination(
                                            tracker
                                        )
                                    )
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
                                    navigator.navigate(
                                        AddEditGroupScreenDestination(
                                            "",
                                            false,
                                            tracker,
                                            0
                                        )
                                    )
                                },
                                onRemoveFromGroup = {

                                }
                            )
                        }
                    }

                    TrackerDisplayType.Groups -> {
                        uiState.value.trackers.filter { it.group.isNotEmpty() }.sortedBy { it.sort }
                            .groupBy { it.group.first() }
                            .forEach { (key, trackers) ->
                                if (key.isNotBlank()) {
                                    item {
                                        GroupCard(
                                            modifier = Modifier
                                                .width(176.dp),
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
                                                navigator.navigate(GroupTrackersScreenDestination(
                                                    GroupTrackersScreenArgs(
                                                        0,
                                                        it
                                                    )
                                                ))
                                            }
                                        )
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

}