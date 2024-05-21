package com.studymate154.studymate.signUp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpService {
    //회원 가입
    @Headers("Content-Type: application/json")
    @POST("/api/signIn")
    fun addUserByEnqueue(
        @Body userInfo: User
    ): Call<SignUpResponseBody>
}