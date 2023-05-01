package com.mystegy.tracker.feature_tracker.presentation.main.tracker.add_edit_tracker_screen

import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Tag

sealed class AddEditTrackerUIEvent {
    data class Title(val title: String): AddEditTrackerUIEvent()
    data class Tag(val tag: String): AddEditTrackerUIEvent()
    data class Description(val description: String): AddEditTrackerUIEvent()
    data class DefaultRep(val defaultRep: String): AddEditTrackerUIEvent()
    data class DefaultWeight(val defaultWeight: String): AddEditTrackerUIEvent()
    object HasDefaultValues: AddEditTrackerUIEvent()
    object Done: AddEditTrackerUIEvent()
    data class AddTagDialogVisibility(val tagType: TagType): AddEditTrackerUIEvent()
    data class AddTag(val tagType: TagType, val tag: String): AddEditTrackerUIEvent()
    data class TapTag(val tag: String): AddEditTrackerUIEvent()
    data class RemoveTagLocal(val tag: String, val tagType: TagType): AddEditTrackerUIEvent()
    data class RemoveTag(val tag: com.mystegy.tracker.feature_tracker.domain.models.Tag): AddEditTrackerUIEvent()
}
data class AddEditTrackerUIState(
    val arg: AddEditTrackerScreenArgs,
    val title: String = "",
    val description: String = "",
    val hasDefaultValues: Boolean = false,
    val defaultRep: String = "",
    val defaultWeight: String = "",
    val errorMessage: String = ERROR_MESSAGE,
    val tag: String = "",
    val addTagDialogVisible: Boolean = false,
    val tagType: TagType = TagType.Primary,
    val tags: List<Tag> = listOf(),
    val primaryTags: List<String> = listOf(),
    val secondaryTags: List<String> = listOf()
)

enum class TagType {
    Primary,
    Secondary
}