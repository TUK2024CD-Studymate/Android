package com.example.studymate.StudyRecord

import android.util.Log
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Response

class RecordRetrofitWork(private val userToken: String, private val recordInfo: StudyModel) {
    var recordId: String? = null
    fun work(callback: (String)->Unit) {

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
                    Log.d("저장 통신 성공", response.toString())
                    Log.d("저장 통신 성공", response.body().toString())
                    if (response.isSuccessful) {
                        val createdStudyModel = response.body()
                        recordId = createdStudyModel!!.id.toString()
                        Log.d("recordId", recordId.toString())
                        callback(recordId!!)
                    }
                }

                override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                    Log.d("데이터 저장 실패", t.message.toString())
                }
            })
    }
}
