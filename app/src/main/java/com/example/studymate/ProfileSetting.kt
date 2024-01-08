package com.example.studymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studymate.databinding.ActivityMainBinding
import com.example.studymate.databinding.ActivityProfileSettingBinding

class ProfileSetting : AppCompatActivity() {
    private lateinit var binding: ActivityProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}