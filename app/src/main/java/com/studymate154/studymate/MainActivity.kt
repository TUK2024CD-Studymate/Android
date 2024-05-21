package com.studymate154.studymate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.studymate154.studymate.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //gif 이미지 넣기
        Glide.with(this).load(R.drawable.book_animation).into(binding.gifImage)

        binding.LoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupText.setOnClickListener {
            val intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
        }



    }

}