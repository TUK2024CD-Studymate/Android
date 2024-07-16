package com.studymate154.studymate.board

import android.util.Log
import com.studymate154.studymate.Model.BoardWriteModel
import com.studymate154.studymate.signUp.SignUpResponseBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class StudyRetrofitWork(private val userToken: String, private val postInfo: BoardWriteModel) {
    fun work() {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }

        val service = PostRetrofitAPI.emgMedService

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.addPostByEnqueue("Bearer $userToken", postInfo)

            if(response.isSuccessful){
                Log.d("Login","로그인 통신 성공")
            }

        }

    }
}