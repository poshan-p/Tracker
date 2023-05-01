package com.mystegy.tracker.feature_tracker.data.local.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = false)
    val tag: String
)
