package com.mystegy.tracker.feature_tracker.data.mappers

import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TrackerEntity
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

fun TrackerEntity.toTracker(): Tracker {
    return Tracker(
        id = this.id,
        title = this.title,
        description = this.description,
        items = this.items,
        hasDefaultValues = this.hasDefaultValues,
        defaultRep = this.defaultRep,
        defaultWeight = this.defaultWeight,
        group = this.nestedGroups,
        sort = this.sort,
        primaryTags = this.primaryTags,
        secondaryTags = this.secondaryTags
    )
}
fun Tracker.toTrackerEntity(): TrackerEntity {
    return TrackerEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        items = this.items,
        hasDefaultValues = this.hasDefaultValues,
        defaultRep = this.defaultRep,
        defaultWeight = this.defaultWeight,
        nestedGroups = this.group,
        sort = this.sort,
        primaryTags = this.primaryTags,
        secondaryTags = this.secondaryTags
    )
}