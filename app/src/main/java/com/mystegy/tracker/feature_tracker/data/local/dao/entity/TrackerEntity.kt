package com.mystegy.tracker.feature_tracker.data.local.dao.entity

import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "nestedGroups", defaultValue = "[]")
    val nestedGroups: List<String>,
    @ColumnInfo(name = "sort", defaultValue = "0")
    val sort: Long,
    @ColumnInfo(name = "primaryTags", defaultValue = "[]")
    val primaryTags: List<String>,
    @ColumnInfo(name = "secondaryTags", defaultValue = "[]")
    val secondaryTags: List<String>
)