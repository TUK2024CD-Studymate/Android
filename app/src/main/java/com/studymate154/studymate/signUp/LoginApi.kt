package com.studymate154.studymate.signUp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/api/login")
    fun userLogin(
        @Body jsonParams : UserModel,
    ): Call<LoginBackendResponse>

    companion object {
        // 기존 서버 주소 "https://studymate154.kro.kr"
        private const val BASE_URL = "http://10.0.2.2:8080"
        val gson : Gson =   GsonBuilder().setLenient().create();

        fun create() : LoginApi {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(LoginApi::class.java)
        }
    }


}