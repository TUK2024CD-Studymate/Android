package com.studymate154.studymate.StudyRecord

import com.google.gson.annotations.SerializedName

data class GetRecordResponse (
    @SerializedName("calenderList")
    val calenderList : List<StudyModel>
        )