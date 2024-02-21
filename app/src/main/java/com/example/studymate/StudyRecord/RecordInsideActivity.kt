package com.example.studymate.StudyRecord

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.studymate.HomeActivity
import com.example.studymate.HomeFragment.RecordFragment
import com.example.studymate.MainActivity
import com.example.studymate.board.BoardInsideActivity
import com.example.studymate.databinding.ActivityMainBinding
import com.example.studymate.databinding.ActivityRecordInsideBinding
import java.time.Instant
import java.time.ZoneId

class RecordInsideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordInsideBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordInsideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.chronometer.start()

        binding.stopBtn.setOnClickListener {
            val stopTime = Instant.now()
            Log.d("stopTime", stopTime.toString())
            val studyMemo = binding.editMemo.text.toString()

            val intent = Intent(this,HomeActivity::class.java)
            intent.putExtra("content",studyMemo)
            intent.putExtra("endTime",stopTime)
            startActivityForResult(intent,REQUEST_CODE)
            finish()
        }


    }
    companion object {
        const val REQUEST_CODE = 1
    }
}