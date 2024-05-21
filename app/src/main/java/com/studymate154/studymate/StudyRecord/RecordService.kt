package com.studymate154.studymate.StudyRecord

import com.studymate154.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RecordService {
    //스터디 기록 생성
    @POST("/api/calender/{subject-id}")
    fun addRecordByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("subject-id") subjectId: String,
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

    //스터디기록 리스트 불러오기
    @GET("/api/calender")
    fun getRecordListByEnqueue(
        @Header("Authorization") authorization: String,
        @Query("startTime") startTime: String // 카테고리를 추가한 부분
    ): Call<GetRecordResponse>

    // 전체 과목 조회
    @GET("/api/subject")
    fun getAllSubjectName(
        @Header("Authorization") authorization: String,
    ): Call<SubjectListResponse>
}
