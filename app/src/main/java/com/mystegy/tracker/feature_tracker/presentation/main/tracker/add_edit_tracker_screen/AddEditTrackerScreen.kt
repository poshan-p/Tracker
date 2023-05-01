package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_tracker_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenArgs
import com.mystegy.tracker.feature_tracker.presentation.components.InputTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = AddEditTrackerScreenArgs::class)
@Composable
fun AddEditTrackerScreen(
    navigator: DestinationsNavigator,
    viewModel: AddEditTrackerScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (uiState.value.addTagDialogVisible) {
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(text = "Add ${uiState.value.tagType.name.lowercase()} tag")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(
                        AddEditTrackerUIEvent.AddTag(
                            uiState.value.tagType,
                            uiState.value.tag
                        )
                    )
                    viewModel.onEvent(
                        AddEditTrackerUIEvent.AddTagDialogVisibility(
                            uiState.value.tagType
                        )
                    )
                }) {
                    Text(text = "Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(
                        AddEditTrackerUIEvent.AddTagDialogVisibility(
                            uiState.value.tagType
                        )
                    )
                }) {
                    Text(text = "Cancel")
                }
            },
            text = {
                Column {
                    InputTextField(
                        label = "Tag",
                        value = uiState.value.tag,
                        onTextChange = { viewModel.onEvent(AddEditTrackerUIEvent.Tag(it)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        imeAction = ImeAction.Done,
                        singleLine = true,
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        keyboardActions = KeyboardActions(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.value.tags) {
                            InputChip(
                                selected = false,
                                onClick = {
                                    viewModel.onEvent(AddEditTrackerUIEvent.TapTag(it.tag))
                                },
                                label = {
                                    Text(text = it.tag)
                                },
                                trailingIcon = {
                                    CompositionLocalProvider(
                                        LocalMinimumTouchTargetEnforcement provides false
                                    ) {
                                        IconButton(onClick = {
                                            viewModel.onEvent(
                                                AddEditTrackerUIEvent.RemoveTag(
                                                    it
                                                )
                                            )
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Cancel,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                }
                            )
                        }
                    }
                }
            }
        )
    }

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
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { result ->
            when (result) {
                AddEditTrackerUIEvent.Done -> {
                    navigator.popBackStack()

                }

                else -> {}
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${if (uiState.value.arg.arg.edit) "Edit" else "Add"} Tracker") },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            InputTextField(
                label = "Title",
                value = uiState.value.title,
                onTextChange = { viewModel.onEvent(AddEditTrackerUIEvent.Title(it)) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                imeAction = ImeAction.Next,
                singleLine = true,
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions()
            )
            Spacer(modifier = Modifier.height(16.dp))
            InputTextField(
                label = "Description(Optional)",
                value = uiState.value.description,
                onTextChange = { viewModel.onEvent(AddEditTrackerUIEvent.Description(it)) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                imeAction = if (uiState.value.hasDefaultValues) ImeAction.Next else ImeAction.Done,
                singleLine = true,
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Set default value for weight")
                Switch(
                    checked = uiState.value.hasDefaultValues,
                    onCheckedChange = { viewModel.onEvent(AddEditTrackerUIEvent.HasDefaultValues) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            InputTextField(
                label = "Default reps",
                value = uiState.value.defaultRep,
                onTextChange = { viewModel.onEvent(AddEditTrackerUIEvent.DefaultRep(it)) },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                imeAction = ImeAction.Next,
                singleLine = true,
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(),
                placeholder = "1"
            )
            if (uiState.value.hasDefaultValues) {
                Spacer(modifier = Modifier.height(16.dp))
                InputTextField(
                    label = "Default weight",
                    value = uiState.value.defaultWeight,
                    onTextChange = { viewModel.onEvent(AddEditTrackerUIEvent.DefaultWeight(it)) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    imeAction = ImeAction.Done,
                    singleLine = true,
                    keyboardType = KeyboardType.Decimal,
                    keyboardActions = KeyboardActions(),
                    placeholder = "0.0"
                )
            }
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    viewModel.onEvent(
                        AddEditTrackerUIEvent.AddTagDialogVisibility(
                            TagType.Primary
                        )
                    )
                }) {
                    Text(text = "Primary Tag")
                }
                Button(onClick = {
                    viewModel.onEvent(
                        AddEditTrackerUIEvent.AddTagDialogVisibility(
                            TagType.Secondary
                        )
                    )
                }) {
                    Text(text = "Secondary Tag")
                }
            }
            if (uiState.value.primaryTags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ElevatedCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Primary Tags", modifier = Modifier.padding(16.dp))
                        LazyRow(
                            contentPadding = PaddingValues(start = 16.dp, bottom = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.value.primaryTags) {
                                InputChip(
                                    selected = false,
                                    onClick = {},
                                    label = {
                                        Text(text = it)
                                    },
                                    trailingIcon = {
                                        CompositionLocalProvider(
                                            LocalMinimumTouchTargetEnforcement provides false
                                        ) {
                                            IconButton(onClick = { viewModel.onEvent(AddEditTrackerUIEvent.RemoveTagLocal(it, TagType.Primary)) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Cancel,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            if (uiState.value.secondaryTags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ElevatedCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Secondary Tags", modifier = Modifier.padding(16.dp))
                        LazyRow(
                            contentPadding = PaddingValues(start = 16.dp, bottom = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.value.secondaryTags) {
                                InputChip(
                                    selected = false,
                                    onClick = {},
                                    label = {
                                        Text(text = it)
                                    },
                                    trailingIcon = {
                                        CompositionLocalProvider(
                                            LocalMinimumTouchTargetEnforcement provides false
                                        ) {
                                            IconButton(onClick = { viewModel.onEvent(AddEditTrackerUIEvent.RemoveTagLocal(it, TagType.Secondary)) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Cancel,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.onEvent(AddEditTrackerUIEvent.Done) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = uiState.value.title.isNotBlank()
            ) {
                Text(text = "Done")
            }

        }
    }

}