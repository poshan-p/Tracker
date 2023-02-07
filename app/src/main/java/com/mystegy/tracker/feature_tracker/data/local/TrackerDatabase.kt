package com.mystegy.tracker.feature_tracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mystegy.tracker.feature_tracker.data.local.dao.TrackerDao
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.GraphEntity
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TrackerEntity
import com.mystegy.tracker.feature_tracker.data.util.Converters

@Database(
    entities = [
        TrackerEntity::class,
        GraphEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun trackerDao(): TrackerDao
}