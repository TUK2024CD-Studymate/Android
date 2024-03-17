package com.example.studymate.search

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.studymate.R
import com.example.studymate.databinding.ActivityMentoInfoBinding

class MentoInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentoInfoBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentoInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")

        binding.nameText.text = intent.getStringExtra("name").toString()
        binding.idText.text = intent.getStringExtra("nickname").toString()
        binding.interestsText.text = intent.getStringExtra("interests").toString()
        binding.emailText.text = intent.getStringExtra("email").toString()
        binding.urlText.text = intent.getStringExtra("url").toString()
        binding.jobText.text = intent.getStringExtra("job").toString()
        binding.mentorInfoText.text = intent.getStringExtra("info").toString()




    }
}