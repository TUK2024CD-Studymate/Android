package com.example.studymate.signUp

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class SignUpResponseBody(
    @SerializedName("result")
    val result: String,
    @SerializedName("status")
    val status: String?,
    val calender_id : String?,
    val id : String?
)