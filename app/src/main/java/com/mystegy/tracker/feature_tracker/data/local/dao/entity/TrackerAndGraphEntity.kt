package com.mystegy.tracker.feature_tracker.data.local.dao.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TrackerAndGraphEntity(
    @Embedded val trackerEntity: TrackerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trackerID"
    )
    val graphEntities: List<GraphEntity>
)
