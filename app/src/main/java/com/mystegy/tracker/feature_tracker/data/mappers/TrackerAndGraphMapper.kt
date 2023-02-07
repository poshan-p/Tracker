package com.mystegy.tracker.feature_tracker.data.mappers

import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TrackerAndGraphEntity
import com.mystegy.tracker.feature_tracker.domain.models.TrackerAndGraph

fun TrackerAndGraphEntity.toTrackerAndGraph() : TrackerAndGraph {
    return TrackerAndGraph(
        tracker = this.trackerEntity.toTracker(),
        graphs = this.graphEntities.map { it.toGraph() }
    )
}

fun TrackerAndGraph.toTrackerAndGraphEntity() : TrackerAndGraphEntity {
    return TrackerAndGraphEntity(
        trackerEntity = this.tracker.toTrackerEntity(),
        graphEntities = this.graphs.map { it.toGraphEntity() }
    )
}