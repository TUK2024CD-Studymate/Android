package com.studymate154.studymate.board

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.studymate154.studymate.Model.BoardWriteModel
import com.studymate154.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Response

class BoardPutRetrofitWork(private val context: Context, private val userToken: String, private val postId : String, private val postInfo: BoardWriteModel) {
    fun work() {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }
        val service = PostRetrofitAPI.emgMedService

        service.putPostByEnqueue("Bearer $userToken", postId, postInfo)
            .enqueue(object : retrofit2.Callback<SignUpResponseBody> {
                override fun onResponse(
                    call: Call<SignUpResponseBody>,
                    response: Response<SignUpResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Toast.makeText(context, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                    Log.d("데이터 저장 실패", t.message.toString())
                }
            })
    }
}