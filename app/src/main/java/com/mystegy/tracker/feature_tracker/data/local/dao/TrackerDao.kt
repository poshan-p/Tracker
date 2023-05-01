package com.mystegy.tracker.feature_tracker.data.local.dao

import androidx.room.*
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.GraphEntity
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TagEntity
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TrackerAndGraphEntity
import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TrackerEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
@Singleton
interface TrackerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracker(model: TrackerEntity): Long
    @Update
    suspend fun updateTracker(model: TrackerEntity): Int
    @Transaction
    suspend fun insertTrackerEntity(model: TrackerEntity) {
        val id = insertTracker(model)
        if (id==-1L) {
            updateTracker(model)
        }
    }

    @Insert(entity = GraphEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGraphEntities(graphEntities: List<GraphEntity>)

    @Query("SELECT * FROM trackerentity")
    fun getTrackers(): Flow<List<TrackerEntity>>

    @Query("SELECT * FROM trackerentity WHERE id =:id LIMIT 1")
    fun getTracker(id: Long): Flow<TrackerEntity>

    @Query("DELETE FROM trackerentity WHERE id = :id")
    suspend fun deleteTracker(id: Long)

    @Transaction
    @Query("SELECT * FROM trackerentity")
    fun getTrackerAndGraphs(): Flow<List<TrackerAndGraphEntity>>

    @Query("DELETE FROM graphentity WHERE graphID = :graphID")
    suspend fun deleteGraph(graphID: String)

    @Update
    suspend fun updateTrackers(trackers: List<TrackerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagEntity: TagEntity)

    @Delete
    suspend fun deleteTag(tagEntity: TagEntity)

    @Query("SELECT * FROM tagentity")
    fun getTags(): Flow<List<TagEntity>>

}