package com.example.studymate.board

import android.util.Log
import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.StudyRecord.StudyRetrofitAPI
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Response

class CommentRetrofitWork(private val userToken: String, private val postId: String, private val postInfo: PostCommentModel) {
    fun work() {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }
        val service = PostRetrofitAPI.emgMedService

        service.postCommentsByEnqueue("Bearer $userToken", postId, postInfo)
            .enqueue(object : retrofit2.Callback<SignUpResponseBody> {
                override fun onResponse(
                    call: Call<SignUpResponseBody>,
                    response: Response<SignUpResponseBody>

                ) {
                    Log.d("저장 통신 성공", response.toString())
                    Log.d("저장 통신 성공", response.body().toString())
                    if (response.isSuccessful) {
                        val result = response.body()

                    }
                }

                override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                    Log.d("데이터 저장 실패", t.message.toString())
                }
            })
    }
}