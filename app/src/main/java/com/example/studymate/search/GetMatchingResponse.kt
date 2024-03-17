package com.example.studymate.search

import com.google.gson.annotations.SerializedName

data class GetMatchingResponse(
    @SerializedName("memberList")
    val memberList: List<GetMatchingModel>
)
