package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_detail_screen

import com.mystegy.tracker.feature_tracker.domain.models.TrackerDetailScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem

sealed class TrackerDetailUIEvent {
    data class DeleteItem(val trackerItem: TrackerItem, val delete: Boolean): TrackerDetailUIEvent()
}
data class TrackerDetailUIState(
    val arg: TrackerDetailScreenArgs,
    val tracker: Tracker = Tracker(),
    val deleteDialogVisible: Boolean = false,
    val trackerItemToBeDeleted: TrackerItem = TrackerItem()
)