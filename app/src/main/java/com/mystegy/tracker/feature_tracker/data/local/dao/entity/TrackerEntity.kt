package com.mystegy.tracker.feature_tracker.data.local.dao.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem

@Entity(
    indices = [Index("id")]
)
data class TrackerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val description: String,
    val items: List<TrackerItem>,
    val hasDefaultValues: Boolean,
    val defaultRep: Int,
    val defaultWeight: Double,
    val group: String
)