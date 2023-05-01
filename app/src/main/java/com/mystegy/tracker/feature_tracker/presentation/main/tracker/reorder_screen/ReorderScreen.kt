package com.mystegy.tracker.feature_tracker.presentation.main.tracker.reorder_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.core.utils.reorder.ReorderableItem
import com.mystegy.tracker.core.utils.reorder.detectReorderAfterLongPress
import com.mystegy.tracker.core.utils.reorder.rememberReorderableLazyGridState
import com.mystegy.tracker.core.utils.reorder.reorderable
import com.mystegy.tracker.feature_tracker.domain.models.ReorderTrackersScreenArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = ReorderTrackersScreenArgs::class)
@Composable
fun ReorderScreen(
    navigator: DestinationsNavigator,
    viewModel: ReorderScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val state = rememberReorderableLazyGridState(
        onMove = { from, to ->
            viewModel.onEvent(ReorderUIEvent.Drag(from.index, to.index))
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Reorder trackers")
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.onEvent(ReorderUIEvent.Save) }) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            state = state.gridState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .reorderable(state)
                .detectReorderAfterLongPress(state),
            columns = GridCells.Adaptive(176.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.value.trackers, { it.id }) { item ->
                ReorderableItem(state, key = item) {
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = if (item.group.isEmpty() || (uiState.value.args.group.isBlank() && uiState.value.args.index != 0) || (uiState.value.args.group.isNotBlank() && item.group.getOrNull(uiState.value.args.index + 1) == null)) item.title else "*" + (item.group.getOrNull(uiState.value.args.index + 1) ?: item.group[uiState.value.args.index]))
                            }
                    }
                }
            }
        }
    }


}