package com.studymate154.studymate.board

import com.studymate154.studymate.MyPage.LogoutModel
import com.studymate154.studymate.StudyRecord.SubjectModel
import com.studymate154.studymate.chatting.ChatRoom
import com.studymate154.studymate.chatting.ReviewVO
import com.studymate154.studymate.chatting.ZoomLinkModel
import com.studymate154.studymate.loginFragment.MessageVerifyModel
import com.studymate154.studymate.search.GetMatchingModel
import com.studymate154.studymate.search.QuesModel
import com.studymate154.studymate.search.ReviewModel
import com.studymate154.studymate.signUp.SignUpResponseBody
import com.studymate154.studymate.signUp.User
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    //본인 회원정보 조회
    @GET("/api/user")
    fun getUserByEnqueue(
        @Header("Authorization") authorization: String,
    ): Call<User>

    @POST("/api/posts")
    fun addPostByEnqueue(
        @Header("Authorization") authorization: String,
        @Body recordInfo: BoardWriteModel
    ): Call<SignUpResponseBody>

    //게시물 가져오기
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

    //kmp 적용한 메칭
    @GET("/api/matching/keyword/{question-id}")
    fun getMatchingList(
        @Header("Authorization") authorization: String,
        @Path("question-id") questionId : String
    ): Call<List<GetMatchingModel>>

    //게시글 삭제
    @DELETE("/api/posts/{post_id}")
    fun deletePostByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("post_id") postId: String // 또는 필요에 따라 다른 데이터 타입을 사용
    ):  Call<SignUpResponseBody>

    //게시글 수정
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
        @Body verifyModel : MessageVerifyModel
    ) : Call<SignUpResponseBody>


    // 좋아요 보내기
    @POST("/api/post/heart/{postId}")
    fun postHeart(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: String
    ) : Call<SignUpResponseBody>



    //줌링크 생성
    @GET("/api/meeting/create")
    fun getZoomLink(
    ): Call<ZoomLinkModel>

    //회원탈퇴
    @DELETE("/api/user")
    fun deleteUser(
        @Header("Authorization") authorization: String
    ): Call<SignUpResponseBody>

    //내 게시물
    @GET("/api/user/post")
    fun getMyPostEnqueue(
        @Header("Authorization") authorization: String
    ): Call<List<GetBoardModel>>

    //로그아웃
    @POST("/api/logout")
    fun postLogout(
        @Header("Authorization") authorization: String,
        @Body logoutModel : LogoutModel
    ): Call<SignUpResponseBody>

    //게시글 검색
    @GET("/api/posts/search")
    fun getPostSearchEnqueue(
        @Header("Authorization") authorization: String,
        @Query("keyword") keyword: String
    ): Call<List<GetBoardModel>>

    //멘토 리뷰 가져오기
    @GET("/api/matching/review/{mentorId}")
    fun getMentorReview(
        @Header("Authorization") authorization: String,
        @Path("mentorId") mentorId : String
    ): Call<List<ReviewModel>>

    //리뷰 포스트
    @POST("/api/review/{mentorId}")
    fun addReviewByEnqueue(
        @Header("Authorization") authorization: String,
        @Path("mentorId") mentorId : String,
        @Body reviewModel: ReviewVO
    ): Call<SignUpResponseBody>
    
    //내 방 목록 가져오기
    @GET("/api/chat/rooms/list")
    fun getMyRoom(
        @Header("Authorization") authorization: String,
    ): Call<List<ChatRoom>>

    //멘토에게 매칭 알림 보내기
    @GET("/api/matching/kakao/{questionId}/{mentorId}")
    fun sendMatchingAlert(
        @Header("Authorization") authorization: String,
        @Path("questionId") questionID : String,
        @Path("mentorId") mentorId : String,
    ): Call<SignUpResponseBody>

    // Ai 멘토 검색
    @GET("/api/matching/keyword/ai/{question-id}")
    fun getMatchingListAi(
        @Header("Authorization") authorization: String,
        @Path("question-id") questionId : String
    ): Call<List<GetMatchingModel>>

    @POST("/api/subject")
    fun postRecordSub(
        @Header("Authorization") authorization: String,
        @Body subjectModel: SubjectModel
    ): Call<SignUpResponseBody>

    //내가 좋아요 누른  게시물
    @GET("/api/user/post/heart")
    fun getMyHeartPostEnqueue(
        @Header("Authorization") authorization: String
    ): Call<List<GetBoardModel>>




}