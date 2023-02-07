package com.mystegy.tracker.feature_tracker.presentation.main.graphs.add_edit_graph_screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.core.utils.Dialog
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgsParcelable
import com.mystegy.tracker.feature_tracker.presentation.components.InputTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalComposeUiApi::class)
@Destination(navArgsDelegate = AddEditGraphScreenArgsParcelable::class, style = Dialog::class)
@Composable
fun AddEditGraphScreen(
    navigator: DestinationsNavigator,
    viewModel: AddEditGraphScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { navigator.popBackStack() },
        confirmButton = {
            TextButton(onClick = {
                if (uiState.value.selectedTrackers.isEmpty()) {
                    Toast.makeText(
                        context,
                        "At least one tracker must be selected",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.onEvent(AddEditGraphUIEvent.Done)
                    navigator.popBackStack()
                }
            }) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                navigator.popBackStack()
            }) {
                Text("Cancel")
            }
        },
        icon = {
            Icon(
                imageVector = if (uiState.value.arg.edit) Icons.Default.Edit else Icons.Default.Add,
                contentDescription = null
            )
        },
        title = {
            Text(text = if (uiState.value.arg.edit) "Edit Graph" else "Add Graph")
        },
        text = {
            LazyColumn {
                item {
                    if (uiState.value.trackers.isEmpty()) {
                        Text(text = "No trackers available")
                    } else {
                        InputTextField(
                            label = "Title(Optional)",
                            value = uiState.value.title,
                            onTextChange = { viewModel.onEvent(AddEditGraphUIEvent.Title(it)) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            imeAction = ImeAction.Done,
                            singleLine = true,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            keyboardActions = KeyboardActions(),
                        )
                    }
                }
                items(uiState.value.trackers) { tracker ->
                    val checked = uiState.value.selectedTrackers.contains(tracker)
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clickable {
                                viewModel.onEvent(AddEditGraphUIEvent.AddRemoveTracker(tracker))
                            }
                            .fillMaxWidth()) {
                        Checkbox(checked = checked, onCheckedChange = {
                            viewModel.onEvent(AddEditGraphUIEvent.AddRemoveTracker(tracker))
                        })
                        Text(text = tracker.title)
                    }
                }
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    )

}