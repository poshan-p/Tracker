package com.mystegy.tracker.feature_tracker.presentation.main.tracker.group_trackers_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.GroupTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditGroupScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditItemScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditTrackerScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.TrackerDetailScreenDestination
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
        when(navResult) {
            NavResult.Canceled -> {}
            is NavResult.Value -> {
                viewModel.onEvent(GroupTrackersUIEvent.GroupName(navResult.value))
            }
        }
    }

    if (uiState.value.groupDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility)
            }
        ) {
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
                    IconButton(onClick = { viewModel.onEvent(GroupTrackersUIEvent.GroupDeleteDialogVisibility) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                    IconButton(onClick = { navigator.navigate(AddEditGroupScreenDestination(uiState.value.arg.group, true, Tracker())) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                }
            )
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
            LazyVerticalGrid(
                columns = GridCells.Adaptive(176.dp),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = uiState.value.trackers, key = { it.id }) { tracker ->
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
                            viewModel.onEvent(GroupTrackersUIEvent.DeleteTracker(tracker))
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
                        },
                        onRemoveFromGroup = {
                            viewModel.onEvent(GroupTrackersUIEvent.RemoveTracker(tracker))
                        }
                    )

                }
            }
        }
    }
}