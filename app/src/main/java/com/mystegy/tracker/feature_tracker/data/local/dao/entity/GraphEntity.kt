package com.mystegy.tracker.feature_tracker.data.local.dao.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TrackerEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("trackerID"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("trackerID")]
)
data class GraphEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val graphID: String,
    val trackerID: Long,
    val title: String
)
