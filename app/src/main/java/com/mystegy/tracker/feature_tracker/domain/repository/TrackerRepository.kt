package com.mystegy.tracker.feature_tracker.domain.repository

import com.mystegy.tracker.feature_tracker.domain.models.Graph
import com.mystegy.tracker.feature_tracker.domain.models.Tag
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.models.TrackerAndGraph
import kotlinx.coroutines.flow.Flow

interface TrackerRepository {

    suspend fun insertTracker(tracker: Tracker)

    suspend fun insertGraphs(graphs: List<Graph>)

    fun getTrackers(): Flow<List<Tracker>>

    fun getTracker(id: Long): Flow<Tracker>

    suspend fun deleteTracker(id: Long)

    fun getTrackerAndGraph(): Flow<List<TrackerAndGraph>>

    suspend fun deleteGraphs(graphID: String)

    suspend fun reorderTrackers(trackers: List<Tracker>)

    suspend fun insertTag(tag: Tag)

    suspend fun deleteTag(tag: Tag)

    fun getTags(): Flow<List<Tag>>


}