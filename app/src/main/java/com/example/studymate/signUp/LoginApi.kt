package com.example.studymate.signUp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    //@Headers("app/json")
    @POST("/api/login")
    fun userLogin(
        @Body jsonParams : UserModel,
    ): Call<LoginBackendResponse>

    companion object {
        private const val BASE_URL = "http://study-mate.kro.kr:8080"
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