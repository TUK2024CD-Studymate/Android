package com.example.studymate

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpService {
    @Headers("Content-Type: application/json")
    @POST("/api/sign-in/")
    fun addUserByEnqueue(@Body userInfo: MutableMap<String, List<String>>): Call<SignUpResponseBody>
}