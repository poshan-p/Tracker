package com.mystegy.tracker.feature_tracker.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mystegy.tracker.feature_tracker.domain.models.TrackerItem

class Converters {
    private val jsonParser: JsonParser = GsonParser(Gson())

    @TypeConverter
    fun fromTrackerItemsJson(json: String) : List<TrackerItem>{
        return jsonParser.fromJson<ArrayList<TrackerItem>>(
            json,
            object : TypeToken<ArrayList<TrackerItem>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toTrackerItemsJson(trackerItems: List<TrackerItem>) : String{
        return jsonParser.toJson(
            trackerItems,
            object : TypeToken<ArrayList<TrackerItem>>(){}.type
        ) ?: "[]"
    }
}