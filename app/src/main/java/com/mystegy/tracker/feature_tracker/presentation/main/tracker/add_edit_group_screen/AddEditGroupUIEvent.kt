package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_group_screen

import com.mystegy.tracker.feature_tracker.domain.models.AddEditGroupScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.GroupAddOrSelect
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

sealed class AddEditGroupUIEvent {
    data class Name(val name: String): AddEditGroupUIEvent()
    data class GroupAddOrSelectEvent(val groupAddOrSelect: GroupAddOrSelect): AddEditGroupUIEvent()
    data class SelectGroup(val group: String): AddEditGroupUIEvent()
    object Done: AddEditGroupUIEvent()
}

data class AddEditGroupUIState(
    val arg: AddEditGroupScreenArgs,
    val name: String = "",
    val doneButtonEnabled: Boolean = false,
    val trackers: List<Tracker> = listOf(),
    val groups: List<String> = listOf(),
    val groupAddOrSelect: GroupAddOrSelect = GroupAddOrSelect.AddNewGroup,
    val selectedGroup: String = ""
)