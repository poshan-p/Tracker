package com.mystegy.tracker.feature_tracker.presentation.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    label: String,
    modifier: Modifier = Modifier,
    value: String,
    onTextChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    singleLine: Boolean = true,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    placeholder: String = ""
) {
    TextField(
        value = value,
        onValueChange = {
            onTextChange.invoke(it)
        }, modifier = modifier,
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        label = {
            Text(text = label)
        },
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        placeholder = { Text(text = placeholder) }
    )

}