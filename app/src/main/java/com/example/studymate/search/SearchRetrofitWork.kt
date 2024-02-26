package com.example.studymate.search

import android.util.Log
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Response

class SearchRetrofitWork(private val userToken: String, private val quesInfo: QuesModel) {

    interface Callback {
        fun onQuestionPosted(questionId: String?)
        fun onFailure(message: String)
    }
    fun work(callback: Callback) {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }
        val service = PostRetrofitAPI.emgMedService

        service.addQuesByEnqueue("Bearer $userToken", quesInfo)
            .enqueue(object : retrofit2.Callback<SignUpResponseBody> {
                override fun onResponse(
                    call: Call<SignUpResponseBody>,
                    response: Response<SignUpResponseBody>

                ) {
                    Log.d("저장 통신 성공", response.toString())
                    Log.d("저장 통신 성공", response.body().toString())
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            val questionId = response.body()?.id
                            Log.d("questionId", questionId.toString())

                            // 성공 시에 콜백 호출
                            if (questionId != null) {
                                callback.onQuestionPosted(questionId)
                            } else {
                                callback.onFailure("Question ID is null")
                            }
                        } else {
                            callback.onFailure("Failed to post question. Response code: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                    Log.d("데이터 저장 실패", t.message.toString())
                }
            })
    }

}