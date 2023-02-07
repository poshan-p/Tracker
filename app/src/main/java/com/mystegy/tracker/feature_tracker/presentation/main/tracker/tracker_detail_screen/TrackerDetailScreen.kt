package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_detail_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.TrackerDetailScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.AddEditItemScreenDestination
import com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_detail_screen.components.TrackerItemCard
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination(navArgsDelegate = TrackerDetailScreenArgs::class)
@Composable
fun TrackerDetailScreen(
    navigator: DestinationsNavigator,
    viewModel: TrackerDetailScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    if (uiState.value.deleteDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(
                    TrackerDetailUIEvent.DeleteItem(
                        uiState.value.trackerItemToBeDeleted,
                        false
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(
                        TrackerDetailUIEvent.DeleteItem(
                            uiState.value.trackerItemToBeDeleted,
                            true
                        )
                    )
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(
                        TrackerDetailUIEvent.DeleteItem(
                            uiState.value.trackerItemToBeDeleted,
                            false
                        )
                    )
                }) {
                    Text(text = "Cancel")
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            },
            title = {
                Text(text = "Delete")
            },
            text = {
                Text(text = "Are you sure you want to delete it?")
            }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.value.tracker.title) },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigator.navigate(
                    AddEditItemScreenDestination(
                        uiState.value.tracker,
                        TrackerItem(),
                        false
                    )
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        if (uiState.value.tracker.items.isEmpty()) {
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Tracker is empty"
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(paddingValues)
            ) {

                items(uiState.value.tracker.items, key = { it.id }) { trackerItem ->
                    TrackerItemCard(
                        trackerItem = trackerItem,
                        modifier = Modifier.animateItemPlacement(),
                        onEdit = {
                            navigator.navigate(
                                AddEditItemScreenDestination(
                                    uiState.value.tracker,
                                    trackerItem,
                                    true
                                )
                            )
                        },
                        onDelete = {
                            viewModel.onEvent(
                                TrackerDetailUIEvent.DeleteItem(
                                    trackerItem,
                                    false
                                )
                            )
                        })

                }
            }
        }
    }
}
