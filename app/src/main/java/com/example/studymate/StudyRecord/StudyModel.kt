package com.example.studymate.StudyRecord

import com.google.gson.Gson
import java.time.ZonedDateTime

data class StudyModel(
    var id : String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var entireTime: String? = null,
    var subjectName : String? = null
)
