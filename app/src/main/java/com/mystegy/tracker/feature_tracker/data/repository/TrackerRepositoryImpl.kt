package com.mystegy.tracker.feature_tracker.data.repository

import com.mystegy.tracker.feature_tracker.data.local.TrackerDatabase
import com.mystegy.tracker.feature_tracker.data.mappers.toGraphEntity
import com.mystegy.tracker.feature_tracker.data.mappers.toTracker
import com.mystegy.tracker.feature_tracker.data.mappers.toTrackerAndGraph
import com.mystegy.tracker.feature_tracker.data.mappers.toTrackerEntity
import com.mystegy.tracker.feature_tracker.domain.models.Graph
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.models.TrackerAndGraph
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRepositoryImpl @Inject constructor(
    db: TrackerDatabase
): TrackerRepository {

    private val dao = db.trackerDao()

    override suspend fun insertTracker(tracker: Tracker) {
        dao.insertTrackerEntity(tracker.toTrackerEntity())
    }

    override suspend fun insertGraphs(graphs: List<Graph>) {
        dao.insertGraphEntities(graphEntities = graphs.map { it.toGraphEntity() })
    }

    override fun getTrackers(): Flow<List<Tracker>> = flow {
        dao.getTrackers().collect { trackers ->
            emit(trackers.map { it.toTracker() })
        }
    }

    override fun getTracker(id: Long): Flow<Tracker> = flow {
        dao.getTracker(id).collect { tracker ->
            emit(tracker.toTracker())
        }
    }

    override suspend fun deleteTracker(id: Long) {
        dao.deleteTracker(id)
    }

    override fun getTrackerAndGraph(): Flow<List<TrackerAndGraph>> = flow {
        dao.getTrackerAndGraphs().collect { trackerAndGraph ->
            emit(trackerAndGraph.map { it.toTrackerAndGraph() })
        }
    }

    override suspend fun deleteGraphs(graphID: String) {
        dao.deleteGraph(graphID)
    }


}