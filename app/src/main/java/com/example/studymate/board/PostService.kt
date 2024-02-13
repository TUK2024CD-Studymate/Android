package com.example.studymate.board

import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {

    @POST("/api/posts")
    fun addPostByEnqueue(
        @Header("Authorization") authorization: String,
        @Body recordInfo: BoardModel
    ): Call<SignUpResponseBody>

    @GET("/api/posts")
    fun getPostByEnqueue(
        @Header("Authorization") authorization: String
    ): Call<List<GetBoardModel>>

    @GET("/api/posts/{id}")
    fun getPostIdByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("id") id: String // 또는 필요에 따라 다른 데이터 타입을 사용
    ):  Call<GetBoardModel>

}