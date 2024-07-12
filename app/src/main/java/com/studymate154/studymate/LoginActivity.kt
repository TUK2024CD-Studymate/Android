package com.studymate154.studymate

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.studymate154.studymate.Model.LoginModel
import com.studymate154.studymate.databinding.ActivityLoginBinding
import com.studymate154.studymate.signUp.LoginRetrofitWork
import com.studymate154.studymate.signUp.RetrofitWork


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        private val TAG = "FCMActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = Intent(this, HomeActivity::class.java)

        binding.Btn.setOnClickListener {
            startActivity(intent)
        }

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        binding.loginBtn.setOnClickListener {
            val data = LoginModel(binding.editEmail.text.toString().trim(), binding.editPass.text.toString().trim())
            val retrofitWork = LoginRetrofitWork(data,this@LoginActivity)
            retrofitWork.work()

            binding.editEmail.text = null
            binding.editPass.text = null
        }



        // 뒤로가기 버튼
        binding.backImg.setOnClickListener {
            finish()
        }
    }
}
