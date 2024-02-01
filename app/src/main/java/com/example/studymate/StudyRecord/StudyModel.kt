package com.example.studymate.StudyRecord

import com.google.gson.Gson
import java.time.ZonedDateTime

data class StudyModel(
    var content: String? = null,
    var studyClass: String? = null,
    var startTime: String? = null,
    var endTime: String? = null
)

fun StudyModel.toJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String.toStudyModel(): StudyModel {
    val gson = Gson()
    return gson.fromJson(this, StudyModel::class.java)
}