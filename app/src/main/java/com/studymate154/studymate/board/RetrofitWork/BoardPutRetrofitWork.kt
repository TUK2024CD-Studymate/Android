package com.studymate154.studymate.board.RetrofitWork

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.studymate154.studymate.Model.BoardWriteModel
import com.studymate154.studymate.board.PostRetrofitAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardPutRetrofitWork(private val context: Context, private val userToken: String, private val postId : String, private val postInfo: BoardWriteModel) {
    fun work() {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }
        val service = PostRetrofitAPI.emgMedService

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.putPostByEnqueue("Bearer $userToken", postId, postInfo)

            if (response.isSuccessful) {
                Log.d("Login", "로그인 통신 성공")
                Toast.makeText(context, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}