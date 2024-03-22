package com.example.studymate.board

import com.example.studymate.loginFragment.VerifyModel
import com.example.studymate.search.GetMatchingResponse
import com.example.studymate.search.QuesModel
import com.example.studymate.signUp.SignUpResponseBody
import com.example.studymate.signUp.User
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    @POST("/api/posts")
    fun addPostByEnqueue(
        @Header("Authorization") authorization: String,
        @Body recordInfo: BoardWriteModel
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
        @Path("post_id") postId: String // post_id를 직접 전달
    ): Call<List<GetCommentModel>>

    @POST("/api/question")
    fun addQuesByEnqueue(
        @Header("Authorization") authorization: String,
        @Body quesInfo: QuesModel
    ): Call<SignUpResponseBody>

    @GET("/api/matching/{questionId}")
    fun getMatchingList(
        @Header("Authorization") authorization: String,
        @Path("questionId") questionId : String
    ): Call<GetMatchingResponse>

    @DELETE("/api/posts/{post_id}")
    fun deletePostByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("post_id") postId: String // 또는 필요에 따라 다른 데이터 타입을 사용
    ):  Call<SignUpResponseBody>

    @PUT("/api/posts/{post_id}")
    fun putPostByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("post_id") postId: String // 또는 필요에 따라 다른 데이터 타입을 사용
    ):  Call<SignUpResponseBody>

    //인즌번호 전송
    @POST("/api/signIn/message")
    fun postTelByEnqueue(
        @Body userTel : User
    ) : Call<SignUpResponseBody>

    //인즌번호 맞는지 확인
    @POST("/api/signIn/message/verify")
    fun postVerifyByEnqueue(
        @Body verifyModel : VerifyModel
    ) : Call<SignUpResponseBody>

    //댓글 수 가져오기
    @GET("/api/posts/{post_id}/comments/count")
    fun getComment(
        @Header("Authorization") authorization: String
    ): Call<SignUpResponseBody>

    // 좋아요 보내기
    @POST("/api/post/heart/{postId}")
    fun postHeart(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: String
    ) : Call<SignUpResponseBody>

    //방생성
    @POST("/api/chat/room")
    fun postRoom(
        @Header("Authorization") authorization: String,
    ): Call<SignUpResponseBody>



}