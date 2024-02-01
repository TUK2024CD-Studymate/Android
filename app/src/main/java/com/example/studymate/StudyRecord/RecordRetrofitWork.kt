package com.example.studymate.StudyRecord

import android.util.Log
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Response

class RecordRetrofitWork(private val userToken: String, private val recordInfo: StudyModel) {

    fun work() {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }
        val service = StudyRetrofitAPI.emgMedService

        service.addRecordByEnqueue("Bearer $userToken", recordInfo)
            .enqueue(object : retrofit2.Callback<SignUpResponseBody> {
                override fun onResponse(
                    call: Call<SignUpResponseBody>,
                    response: Response<SignUpResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("데이터 저장 성공", "$result")
                    }
                }

                override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                    Log.d("데이터 저장 실패", t.message.toString())
                }
            })
    }
}
