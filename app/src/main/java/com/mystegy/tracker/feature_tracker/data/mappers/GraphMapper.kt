package com.mystegy.tracker.feature_tracker.data.mappers

import com.mystegy.tracker.feature_tracker.data.local.dao.entity.GraphEntity
import com.mystegy.tracker.feature_tracker.domain.models.Graph

fun GraphEntity.toGraph(): Graph {
    return Graph(
        id = this.id,
        graphID = this.graphID,
        trackerID = this.trackerID,
        title = this.title
    )
}
fun Graph.toGraphEntity(): GraphEntity {
    return GraphEntity(
        id = this.id,
        graphID = this.graphID,
        trackerID = this.trackerID,
        title = this.title
    )
}