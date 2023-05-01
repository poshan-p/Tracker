package com.mystegy.tracker.feature_tracker.presentation.main.tracker.reorder_screen

import com.mystegy.tracker.feature_tracker.domain.models.ReorderTrackersScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class ReorderUIEvent {
    data class Drag(val from: Int, val to: Int): ReorderUIEvent()
    object Save: ReorderUIEvent()
}

data class ReorderUIState(
    val args: ReorderTrackersScreenArgs,
    val originalTrackers: List<Tracker> = listOf(),
    val trackers: List<Tracker> = listOf()
)