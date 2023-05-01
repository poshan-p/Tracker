package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mystegy.tracker.core.utils.displayFormat
import com.mystegy.tracker.core.utils.toLocalDateTime
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerCard(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.width(176.dp),
    tracker: Tracker,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onAdd: () -> Unit,
    onMoveToGroup: () -> Unit,
    onRemoveFromGroup: () -> Unit
) {
    var dropdownMenuVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var descriptionDialogVisible by remember { mutableStateOf(false) }

    if (deleteDialogVisible) {
        AlertDialog(
            onDismissRequest = { deleteDialogVisible = false },
            confirmButton = {
                TextButton(onClick = {
                    deleteDialogVisible = false
                    onDelete.invoke()
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    deleteDialogVisible = false
                }) {
                    Text(text = "Cancel")
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            },
            title = { Text(text = "Delete tracker") },
            text = { Text(text = "Are you sure you want to delete the tracker?") }
        )
    }
    if (descriptionDialogVisible) {
        AlertDialog(
            onDismissRequest = { descriptionDialogVisible = false },
            icon = {
                Icon(imageVector = Icons.Default.Description, contentDescription = null)
            },
            title = { Text(text = "Description") },
            text = {
                Column {
                    Text(text = tracker.description.ifBlank { "No description" })
                    Spacer(modifier = Modifier.height(8.dp))
                    if (tracker.primaryTags.isNotEmpty()) {
                        Text(text = "Primary Tags: ${tracker.primaryTags.joinToString(", ")}")
                    }
                    if (tracker.secondaryTags.isNotEmpty()) {
                        Text(text = "Secondary Tags: ${tracker.secondaryTags.joinToString(", ")}")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    descriptionDialogVisible = false
                }) {
                    Text(text = "OK")
                }
            }
        )
    }


    ElevatedCard(
        modifier = modifier,
        onClick = { onClick.invoke() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = { dropdownMenuVisible = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = dropdownMenuVisible,
                onDismissRequest = { dropdownMenuVisible = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        dropdownMenuVisible = false
                        onEdit.invoke()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    })
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        dropdownMenuVisible = false
                        deleteDialogVisible = true
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null
                        )
                    })
                DropdownMenuItem(
                    text = { Text("Description") },
                    onClick = {
                        dropdownMenuVisible = false
                        descriptionDialogVisible = true
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Description,
                            contentDescription = null
                        )
                    }
                )
                if (tracker.group.isNotEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Remove from group") },
                        onClick = {
                            dropdownMenuVisible = false
                            onRemoveFromGroup.invoke()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.PlaylistRemove,
                                contentDescription = null
                            )
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Move to group") },
                    onClick = {
                        dropdownMenuVisible = false
                        onMoveToGroup.invoke()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.PlaylistAdd,
                            contentDescription = null
                        )
                    }
                )
            }
        }

        Text(
            text = tracker.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.History, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (tracker.items.isEmpty()) "No Data" else tracker.items.first().localDateTime.toLocalDateTime()
                    .displayFormat(),
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = MaterialTheme.colorScheme.primary)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = if (tracker.items.isEmpty()) "No Data"
                else "${tracker.items.size} Tracked",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = { onAdd.invoke() }) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
            }
        }
    }
}