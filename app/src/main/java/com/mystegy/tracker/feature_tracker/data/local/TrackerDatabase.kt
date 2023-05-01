package com.mystegy.tracker.feature_tracker.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.mystegy.tracker.feature_tracker.data.local.dao.TrackerDao
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.GraphEntity
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TagEntity
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TrackerEntity
import com.mystegy.tracker.feature_tracker.data.util.Converters

@Database(
    entities = [
        TrackerEntity::class,
        GraphEntity::class,
        TagEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            DeleteGroupColumnInTrackerEntity::class
        )
    ]
)
@TypeConverters(Converters::class)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun trackerDao(): TrackerDao
}

@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "TrackerEntity",
        columnName = "group"
    )
)
class DeleteGroupColumnInTrackerEntity : AutoMigrationSpec