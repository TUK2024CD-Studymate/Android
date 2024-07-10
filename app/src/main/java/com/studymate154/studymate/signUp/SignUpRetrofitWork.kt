package com.studymate154.studymate.signUp

import android.util.Log
import com.studymate154.studymate.Model.UserInfoModel
import com.studymate154.studymate.board.PostRetrofitAPI
import retrofit2.Call
import retrofit2.Response

class RetrofitWork(private val userInfo: UserInfoModel) {
    fun work() {
        val service = PostRetrofitAPI.emgMedService

        service.addUserByEnqueue(userInfo)
            .enqueue(object : retrofit2.Callback<SignUpResponseBody> {
                override fun onResponse(
                    call: Call<SignUpResponseBody>,
                    response: Response<SignUpResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("회원가입 성공", "$result")
                    }
                }

                override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                    Log.d("회원가입 실패", t.message.toString())
                }
            })
    }
}