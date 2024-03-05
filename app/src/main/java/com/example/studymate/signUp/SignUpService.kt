package com.example.studymate.signUp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpService {
    @Headers("Content-Type: application/json")
    @POST("/api/signIn")
    fun addUserByEnqueue(@Body userInfo: User): Call<SignUpResponseBody>
}