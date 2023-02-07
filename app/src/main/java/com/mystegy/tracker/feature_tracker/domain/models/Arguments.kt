package com.mystegy.tracker.feature_tracker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditTrackerScreenArgs(
    val tracker: Tracker = Tracker(),
    val group: String,
    val edit: Boolean = false
): Parcelable

@Parcelize
data class AddEditItemScreenArgs(
    val tracker: Tracker = Tracker(),
    val item: TrackerItem,
    val edit: Boolean = false
): Parcelable

@Parcelize
data class TrackerDetailScreenArgs(
    val tracker: Tracker = Tracker()
): Parcelable

@Parcelize
data class AddEditGraphScreenArgsParcelable(
    val arg: AddEditGraphScreenArgs
): Parcelable

@Parcelize
data class AddEditGraphScreenArgs(
    val graphID: String,
    val trackers: List<Tracker>,
    val title: String,
    val edit: Boolean
): Parcelable

@Parcelize
data class GroupTrackersScreenArgs(
    val group: String
): Parcelable

@Parcelize
data class AddEditGroupScreenArgs(
    val groupName: String,
    val edit: Boolean,
    val tracker: Tracker
): Parcelable
enum class Nav(val label: String) {
    Tracker("Tracker"),
    Graph("Graphs"),
    Else("Else")
}
data class GraphCustom(
    val id: Long,
    val graphID: String,
    val tracker: Tracker,
    val title: String
)

enum class GroupAddOrSelect(val label: String){
    AddNewGroup("Add New Group"),
    SelectExistingGroup("Select Existing Group")
}