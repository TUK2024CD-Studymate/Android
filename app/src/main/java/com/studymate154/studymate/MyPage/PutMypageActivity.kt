package com.studymate154.studymate.MyPage


import android.content.SharedPreferences

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.studymate154.studymate.databinding.ActivityPutMypageBinding


class PutMypageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPutMypageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPutMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)




    }




}
