package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_detail_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mystegy.tracker.core.utils.displayFormat
import com.mystegy.tracker.core.utils.displayFormatDayShort
import com.mystegy.tracker.core.utils.toLocalDateTime
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem

@Composable
fun TrackerItemCard(
    modifier: Modifier = Modifier,
    trackerItem: TrackerItem,
    onEdit:() -> Unit,
    onDelete:() -> Unit
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column {
            Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = trackerItem.localDateTime.toLocalDateTime().displayFormat(), style = MaterialTheme.typography.bodySmall)
                    Text(text = trackerItem.localDateTime.toLocalDateTime().displayFormatDayShort(), style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Card {
                        Text(text = "Reps: ${trackerItem.reps}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(4.dp))
                    }
                    if (trackerItem.weight > 0) {
                        Card {
                            Text(
                                text = "Weight: ${trackerItem.weight}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp))

                        }

                        Card {
                            Text(
                                text = "Volume: ${trackerItem.volume}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp))

                        }

                    }
                }
                Spacer(modifier = Modifier
                    .width(16.dp)
                    .weight(1f))
                Column {
                    IconButton(onClick = { onEdit.invoke() }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = { onDelete.invoke() }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
            if (trackerItem.note.isNotBlank()) {
                Text(
                    text = "Note: ${trackerItem.note}",
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}