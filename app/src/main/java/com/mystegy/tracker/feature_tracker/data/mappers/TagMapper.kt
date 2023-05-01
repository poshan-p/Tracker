package com.mystegy.tracker.feature_tracker.data.mappers

import com.mystegy.tracker.feature_tracker.data.local.dao.entity.TagEntity
import com.mystegy.tracker.feature_tracker.domain.models.Tag

fun TagEntity.toTag(): Tag {
    return Tag(
        tag = this.tag
    )
}
fun Tag.toTagEntity(): TagEntity {
    return TagEntity(
        tag = this.tag
    )
}