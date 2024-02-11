package com.example.studymate.board

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.studymate.R
import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.StudyRecord.StudyRetrofitAPI.gson
import com.example.studymate.databinding.ActivityBoardWriteBinding
import com.example.studymate.databinding.ActivityHomeBinding
import com.example.studymate.signUp.LoginApi

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardWriteBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")

        val boardModel = BoardModel(null,null,null,null)

        val interests = resources.getStringArray(R.array.interests_array)
        val category = resources.getStringArray(R.array.category_array)
        val interestsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, interests)
        val categoryAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, category)
        binding.spinner1.adapter = interestsAdapter
        binding.spinner2.adapter = categoryAdapter


        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        boardModel.interests = "MATH"
                    }
                    1 -> {
                        boardModel.interests = "PROGRAMMING"
                    }
                    2 -> {
                        boardModel.interests = "KOREAN"
                    }
                    3 -> {
                        boardModel.interests = "ENGLISH"
                    }
                    4 -> {
                        boardModel.interests = "SCIENCE"
                    }
                    5 -> {
                        boardModel.interests = "SOCIETY"
                    }
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->{
                        boardModel.category = "QUESTION"
                    }
                    1->{
                        boardModel.category = "STUDY"
                    }
                    2->{
                        boardModel.category = "FREE"
                    }
            }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        // @post
        binding.postBtn.setOnClickListener {
            boardModel.title = binding.editTitle.text.toString()
            boardModel.content = binding.editContent.text.toString()
            updateBoardData(boardModel)

            val retrofitWork = StudyRetrofitWork(userToken.toString(),boardModel)
            retrofitWork.work()
            finish()
        }


    }

    private fun updateBoardData(boardData: BoardModel) {
        val json = gson.toJson(boardData)
        Log.d("studymodel", json)
    }
}