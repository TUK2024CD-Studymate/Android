package com.example.studymate.board

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.studymate.R
import com.example.studymate.StudyRecord.RecordListAdapter
import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.StudyRecord.StudyRetrofitAPI
import com.example.studymate.databinding.ActivityBoardInsideBinding
import com.example.studymate.databinding.ActivityBoardWriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardInsideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        //보드아이디
        val boardId = intent.getStringExtra("boardId").toString()
        Log.d("boardId", boardId)

        //뒤로가기
        binding.backImg.setOnClickListener {
            finish()
        }
        getBoardItem(boardId)




    }

    private fun getBoardItem(id: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getPostIdByEnqueue("Bearer $userToken", id)

        call.enqueue(object : Callback<GetBoardModel> {
            override fun onResponse(call: Call<GetBoardModel>, response: Response<GetBoardModel>) {
                if (response.isSuccessful) {
                    val boardModel: GetBoardModel? = response.body()

                    if(boardModel != null){
                        binding.category.text =  boardModel.category
                        binding.title.text = boardModel.title
                        binding.nickname.text = boardModel.nickname
                        binding.date.text = boardModel.createdAt
                        binding.content.text = boardModel.content
                    }


                } else {
                    Log.e("getPostById", "게시글 세부 정보 가져오기 실패. 응답 코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetBoardModel>, t: Throwable) {
                // 네트워크 요청 실패 처리
                Log.e("getPostById", "네트워크 요청 실패", t)
            }
        })
    }



}