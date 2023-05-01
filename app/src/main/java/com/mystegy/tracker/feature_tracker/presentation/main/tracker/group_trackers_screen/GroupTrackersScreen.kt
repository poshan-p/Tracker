package com.mystegy.tracker.feature_tracker.presentation.main.tracker.group_trackers_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.core.utils.TAG
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenWrapperArg
import com.mystegy.tracker.feature_tracker.domain.models.GroupTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.ReorderTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination(navArgsDelegate = GroupTrackersScreenArgs::class)
@Composable
fun GroupTrackersScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<AddEditGroupScreenDestination, String>,
    viewModel: GroupTrackersScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    resultRecipient.onNavResult { navResult ->
        when (navResult) {
            NavResult.Canceled -> {

            }
            is NavResult.Value -> {
                if (uiState.value.edit) {
                    viewModel.onEvent(GroupTrackersUIEvent.GroupName(navResult.value))
                    viewModel.setEdit(false)
                }
            }
        }
    }

    if (uiState.value.groupDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility)
            },
            confirmButton = {

            },
            text = {
                Surface(
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextButton(
                            onClick = {
                                viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility)
                                viewModel.onEvent(GroupTrackersUIEvent.DeleteTrackers)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Delete Group With Trackers")
                        }
                        TextButton(
                            onClick = {
                                viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility)
                                viewModel.onEvent(GroupTrackersUIEvent.RemoveTrackers)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Delete Group Only")
                        }
                        TextButton(
                            onClick = {
                                viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility)
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.value.arg.group)
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navigator.navigate(
                            ReorderScreenDestination(
                                ReorderTrackersScreenArgs(
                                    index = uiState.value.arg.index,
                                    group = uiState.value.arg.group
                                )
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Recycling, contentDescription = null)
                    }
                    IconButton(onClick = { viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                    IconButton(onClick = {
                        viewModel.setEdit(true)
                        navigator.navigate(
                            AddEditGroupScreenDestination(
                                uiState.value.arg.group,
                                true,
                                Tracker(),
                                uiState.value.arg.index
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigator.navigate(
                    AddEditTrackerScreenDestination(
                        AddEditTrackerScreenArgs(
                            AddEditTrackerScreenWrapperArg(
                                group = "",
                                insertFromGroup = uiState.value.trackers.first().group.take(uiState.value.arg.index + 1)
                            )
                        )
                    )
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        if (uiState.value.trackers.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = "No trackers available for this group")
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(176.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 48.dp
                ),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.value.trackers.sortedBy { it.sort }.groupBy {
                    if (it.group.getOrNull(uiState.value.arg.index + 1) == null) {
                        it.id
                    } else {
                        it.group[uiState.value.arg.index + 1]
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
                                    viewModel.onEvent(GroupTrackersUIEvent.DeleteTracker(trackers.first()))
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
                                            uiState.value.arg.index + 1
                                        )
                                    )
                                },
                                onRemoveFromGroup = {
                                    viewModel.onEvent(GroupTrackersUIEvent.RemoveTracker(trackers.first()))
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
                                    navigator.navigate(
                                        GroupTrackersScreenDestination(
                                            GroupTrackersScreenArgs(
                                                uiState.value.arg.index + 1,
                                                it
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}