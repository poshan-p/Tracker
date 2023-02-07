package com.mystegy.tracker.feature_tracker.presentation.main.tracker.group_trackers_screen

import com.mystegy.tracker.feature_tracker.domain.models.GroupTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class GroupTrackersUIEvent {
    object DeleteTrackers: GroupTrackersUIEvent()
    object RemoveTrackers: GroupTrackersUIEvent()
    object GroupDeleteDialogVisibility: GroupTrackersUIEvent()
    data class RemoveTracker(val tracker: Tracker): GroupTrackersUIEvent()
    data class DeleteTracker(val tracker: Tracker): GroupTrackersUIEvent()
    data class GroupName(val groupName: String): GroupTrackersUIEvent()
}

data class GroupTrackersUIState(
    val arg: GroupTrackersScreenArgs,
    val trackers: List<Tracker> = listOf(),
    val groupDeleteDialogVisible: Boolean = false
)