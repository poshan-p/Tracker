package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_group_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGroupScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.GroupAddOrSelect
import com.mystegy.tracker.feature_tracker.presentation.components.InputTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Destination(navArgsDelegate = AddEditGroupScreenArgs::class)
@Composable
fun AddEditGroupScreen(
    navigator: ResultBackNavigator<String>,
    viewModel: AddEditGroupScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Group"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack("") }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (!uiState.value.arg.edit && uiState.value.groups.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.value.groupAddOrSelect == GroupAddOrSelect.AddNewGroup,
                        onClick = {
                            viewModel.onEvent(
                                AddEditGroupUIEvent.GroupAddOrSelectEvent(
                                    GroupAddOrSelect.AddNewGroup
                                )
                            )
                        },
                        label = {
                            Text(text = GroupAddOrSelect.AddNewGroup.label)
                        },
                        leadingIcon = {
                            if (uiState.value.groupAddOrSelect == GroupAddOrSelect.AddNewGroup)
                                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                        }
                    )
                    FilterChip(
                        selected = uiState.value.groupAddOrSelect == GroupAddOrSelect.SelectExistingGroup,
                        onClick = {
                            viewModel.onEvent(
                                AddEditGroupUIEvent.GroupAddOrSelectEvent(
                                    GroupAddOrSelect.SelectExistingGroup
                                )
                            )
                        },
                        label = {
                            Text(text = GroupAddOrSelect.SelectExistingGroup.label)
                        },
                        leadingIcon = {
                            if (uiState.value.groupAddOrSelect == GroupAddOrSelect.SelectExistingGroup)
                                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                        }
                    )
                }
            }
            if (uiState.value.groupAddOrSelect == GroupAddOrSelect.SelectExistingGroup) {
                FlowRow {
                    uiState.value.groups.forEach { group ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEvent(
                                        AddEditGroupUIEvent.SelectGroup(group)
                                    )
                                }
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = uiState.value.selectedGroup == group,
                                onCheckedChange = {
                                    viewModel.onEvent(
                                        AddEditGroupUIEvent.SelectGroup(group)
                                    )
                                })
                            Text(text = group)
                        }
                    }
                }
            } else {
                InputTextField(
                    label = "Group Name",
                    value = uiState.value.name,
                    onTextChange = {
                        viewModel.onEvent(AddEditGroupUIEvent.Name(it))
                    },
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.onEvent(AddEditGroupUIEvent.Done)
                    navigator.navigateBack(if (uiState.value.arg.edit) uiState.value.name else "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = (uiState.value.doneButtonEnabled && uiState.value.name.isNotBlank()) || (uiState.value.groupAddOrSelect == GroupAddOrSelect.SelectExistingGroup && uiState.value.selectedGroup.isNotBlank())
            ) {
                Text(text = "Done")
            }
        }
    }
}