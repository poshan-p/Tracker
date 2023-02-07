package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_item_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mystegy.tracker.core.utils.Dialog
import com.mystegy.tracker.core.utils.displayFormat
import com.mystegy.tracker.feature_tracker.domain.models.AddEditItemScreenArgs
import com.mystegy.tracker.feature_tracker.presentation.components.InputTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Destination(navArgsDelegate = AddEditItemScreenArgs::class, style = Dialog::class)
@Composable
fun AddEditItemScreen(
    navigator: DestinationsNavigator,
    viewModel: AddEditItemScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val timeDialogState = rememberMaterialDialogState()
    val dateDialogState = rememberMaterialDialogState()



    AlertDialog(
        onDismissRequest = { navigator.popBackStack() },
        confirmButton = {
            TextButton(onClick = {
                viewModel.onEvent(AddEditItemUIEvent.Done)
                navigator.popBackStack()
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
            Text(text = uiState.value.arg.tracker.title)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputChip(
                        selected = false,
                        onClick = {
                            dateDialogState.show()
                        },
                        label = { Text(text = uiState.value.localDate.displayFormat()) })
                    InputChip(
                        selected = false,
                        onClick = { timeDialogState.show() },
                        label = { Text(text = uiState.value.localTime.displayFormat()) })
                }
                Spacer(modifier = Modifier.height(8.dp))
                InputTextField(
                    label = "Note",
                    value = uiState.value.note,
                    onTextChange = { viewModel.onEvent(AddEditItemUIEvent.Note(it)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    imeAction = ImeAction.Next,
                    singleLine = true,
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    keyboardActions = KeyboardActions(),
                )

                Spacer(modifier = Modifier.height(8.dp))
                InputTextField(
                    label = "Reps",
                    value = uiState.value.reps,
                    onTextChange = { viewModel.onEvent(AddEditItemUIEvent.Reps(it)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    imeAction = ImeAction.Next,
                    singleLine = true,
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Number,
                    keyboardActions = KeyboardActions(),
                    placeholder = if (uiState.value.arg.tracker.hasDefaultValues) uiState.value.arg.tracker.defaultRep.toString() else "1"
                )
                Spacer(modifier = Modifier.height(8.dp))
                InputTextField(
                    label = "Weight",
                    value = uiState.value.weight,
                    onTextChange = { viewModel.onEvent(AddEditItemUIEvent.Weight(it)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    imeAction = ImeAction.Done,
                    singleLine = true,
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Decimal,
                    keyboardActions = KeyboardActions(),
                    placeholder = if (uiState.value.arg.tracker.hasDefaultValues) uiState.value.arg.tracker.defaultWeight.toString() else "0.0"
                )


            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    )




    MaterialDialog(
        dialogState = timeDialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.primary)
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        timepicker(
            initialTime = uiState.value.localTime,
            title = "Pick time",
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colorScheme.primary,
                inactiveBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                activeTextColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                inactivePeriodBackground = MaterialTheme.colorScheme.surfaceVariant,
                selectorColor = MaterialTheme.colorScheme.primary,
                selectorTextColor = MaterialTheme.colorScheme.onPrimary,
                headerTextColor = MaterialTheme.colorScheme.onBackground,
                borderColor = MaterialTheme.colorScheme.onBackground
            ),
            is24HourClock = true
        ) { time ->
            viewModel.onEvent(AddEditItemUIEvent.Time(time))
        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.primary)
            )
            negativeButton(
                "Cancel",
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        datepicker(
            initialDate = uiState.value.localDate,
            title = "Pick date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateInactiveBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) { date ->
            viewModel.onEvent(AddEditItemUIEvent.Date(date))
        }
    }
}