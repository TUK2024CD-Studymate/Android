package com.example.studymate.board

import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    @POST("/api/posts")
    fun addPostByEnqueue(
        @Header("Authorization") authorization: String,
        @Body recordInfo: BoardModel
    ): Call<SignUpResponseBody>

    @GET("/api/posts")
    fun getPostByEnqueue(
        @Header("Authorization") authorization: String,
        @Query("category") category: String // 카테고리를 추가한 부분
    ): Call<List<GetBoardModel>>


    @GET("/api/posts/{id}")
    fun getPostIdByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("id") id: String // 또는 필요에 따라 다른 데이터 타입을 사용
    ):  Call<GetBoardModel>

    @POST("/api/posts/{post_id}/comments")
    fun postCommentsByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("post_id") postId: String, // post_id를 직접 전달
        @Body postCommentModel: PostCommentModel
    ): Call<SignUpResponseBody>

    @GET("/api/posts/{post_id}/comments")
    fun getCommentByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("post_id") postId: String, // post_id를 직접 전달
    ): Call<List<GetCommentModel>>
}