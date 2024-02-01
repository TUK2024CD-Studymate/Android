package com.example.studymate

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studymate.HomeActivity
import com.example.studymate.Login.LoginApi
import com.example.studymate.Login.LoginBackendResponse
import com.example.studymate.Login.UserModel
import com.example.studymate.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = Intent(this, HomeActivity::class.java)

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.btn.setOnClickListener {
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPass.text.toString().trim()

            val api = LoginApi.create()
            val data = UserModel(email, password)

            api.userLogin(data).enqueue(object : retrofit2.Callback<LoginBackendResponse> {
                override fun onResponse(
                    call: Call<LoginBackendResponse>,
                    response: Response<LoginBackendResponse>
                ) {
                    Log.d("로그인 통신 성공", response.toString())
                    Log.d("로그인 통신 성공", response.body().toString())

                    when (response.code()) {
                        200 -> {
                            // 로그인 성공 시 토큰을 SharedPreferences에 저장
                            val editor = sharedPreferences.edit()
                            editor.putString("userToken", response.body()?.token)
                            editor.apply()

                            startActivity(intent)
                        }
                        401 -> Toast.makeText(
                            this@LoginActivity,
                            "로그인 실패 : 아이디나 비번이 올바르지 않습니다",
                            Toast.LENGTH_LONG
                        ).show()
                        500 -> Toast.makeText(
                            this@LoginActivity,
                            "로그인 실패 : 서버 오류",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
                    // 실패
                    Log.d("로그인 통신 실패", t.message.toString())
                    Log.d("로그인 통신 실패", "fail")
                }
            })
        }

        // 뒤로가기 버튼
        binding.backImg.setOnClickListener {
            finish()
        }
    }
}
