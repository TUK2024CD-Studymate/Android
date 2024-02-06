package com.example.studymate.StudyRecord

import com.example.studymate.User
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RecordService {
    @POST("/api/calender")
    fun addRecordByEnqueue(
        @Header("Authorization") authorization: String,
        @Body recordInfo: StudyModel
    ): Call<SignUpResponseBody>

    @GET("/api/calender/{calender_id}")
    fun getRecordByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("calender_id") calenderId: String // 또는 필요에 따라 다른 데이터 타입을 사용
    ): Call<StudyModel>

    @DELETE("/api/calender/{calender_id}")
    fun deleteRecordByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("calender_id") calenderId: String
    ): Call<SignUpResponseBody>
}
