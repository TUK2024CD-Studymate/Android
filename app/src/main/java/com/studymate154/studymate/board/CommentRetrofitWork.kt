package com.studymate154.studymate.board

import android.util.Log
import com.studymate154.studymate.Model.PostCommentModel
import com.studymate154.studymate.signUp.SignUpResponseBody
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class CommentRetrofitWork(private val userToken: String, private val postId: String, private val postInfo: PostCommentModel) {
    fun work() {

        if (userToken.isEmpty()) {
            Log.d("userToken", "userToken이 없습니다.")
            return
        }
        val service = PostRetrofitAPI.emgMedService

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.postCommentsByEnqueue("Bearer $userToken", postId, postInfo)

            if (response.isSuccessful){
                Log.d("commentPost","댓글 post 성공")
            }
        }
    }
}