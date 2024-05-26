package com.studymate154.studymate.board

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.studymate154.studymate.R
import com.studymate154.studymate.StudyRecord.StudyRetrofitAPI
import com.studymate154.studymate.databinding.ActivityBoardPutBinding
import com.studymate154.studymate.databinding.ActivityChattingRoomBinding

class BoardPutActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityBoardPutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardPutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val boardId = intent.getStringExtra("boardId").toString()

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "") ?: ""

        val boardModel = BoardWriteModel(null,null,null,null)

        // 뒤로가기
        binding.backImg.setOnClickListener {
            finish()
        }

        //과목 선택, 카테고리 선택
        val interests = resources.getStringArray(R.array.interests_array)
        val category = resources.getStringArray(R.array.category_array)
        val interestsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, interests)
        val categoryAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, category)
        binding.spinner1.adapter = interestsAdapter
        binding.spinner2.adapter = categoryAdapter

        // 상세분야 선택
        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {

                    }
                    1 -> {
                        boardModel.interests = "WEBAPP"
                    }
                    2 -> {
                        boardModel.interests = "SERVER"
                    }
                    3 -> {
                        boardModel.interests = "AI"
                    }
                    4 -> {
                        boardModel.interests = "DATA"
                    }
                    5 -> {
                        boardModel.interests = "SECURITY"
                    }
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //관심 게세핀 선택
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->{

                    }
                    1->{
                        boardModel.category = "QUESTION"
                    }
                    2->{
                        boardModel.category = "STUDY"
                    }
                    3->{
                        boardModel.category = "FREE"
                    }
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        //게시글 수정
        binding.postBtn.setOnClickListener {
            boardModel.title = binding.editTitle.text.toString()
            boardModel.content = binding.editContent.text.toString()
            updateBoardData(boardModel)

            val retrofitWork = BoardPutRetrofitWork( this ,userToken, boardId, boardModel)
            retrofitWork.work()
            finish()
        }




    }

    private fun updateBoardData(boardData: BoardWriteModel) {
        val json = StudyRetrofitAPI.gson.toJson(boardData)
        Log.d("studymodel", json)
    }


}