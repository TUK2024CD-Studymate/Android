package com.studymate154.studymate.signUp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.studymate154.studymate.HomeActivity
import com.studymate154.studymate.Model.LoginModel
import com.studymate154.studymate.board.PostRetrofitAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class LoginRetrofitWork(private val user: LoginModel, private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun work() {
        val service = PostRetrofitAPI.emgMedService
        val intent = Intent(context, HomeActivity::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.userLogin(user)

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    when (response.code()) {
                        200 -> {
                            // 로그인 성공 시 토큰을 SharedPreferences에 저장
                            val editor = sharedPreferences.edit()
                            editor.putString("userToken", response.body()?.accessToken)
                            editor.putString("refreshToken", response.body()?.refreshToken)
                            editor.apply()

                            context.startActivity(intent)
                        }
                        401 -> Toast.makeText(context, "로그인 실패 : 아이디나 비번이 올바르지 않습니다", Toast.LENGTH_LONG).show()
                        500 -> Toast.makeText(
                            context,
                            "로그인 실패 : 서버 오류",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }else {
                    Log.d("로그인 실패", response.code().toString())
                }
            }
        }

    }
}
