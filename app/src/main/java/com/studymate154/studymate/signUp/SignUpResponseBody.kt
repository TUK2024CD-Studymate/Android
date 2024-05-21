package com.studymate154.studymate.signUp

import com.google.gson.annotations.SerializedName

data class SignUpResponseBody(
    @SerializedName("result")
    val result: String,
    @SerializedName("status")
    val status: String?,
    val calender_id : String?,
    val id : String?,
    val roomId : String?
)