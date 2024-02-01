package com.example.studymate.StudyRecord

import com.example.studymate.User
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface RecordService {
    @POST("/api/calender")
    fun addRecordByEnqueue(
        @Header("Authorization") authorization: String,
        @Body recordInfo: StudyModel
    ): Call<SignUpResponseBody>
}
