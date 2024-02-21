package com.example.studymate.board

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
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
    var commentList = listOf<GetCommentModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardInsideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")

        val postCommentModel = PostCommentModel(null)

        val listAdapter = CommentListAdapter()

        //보드아이디
        val boardId = intent.getStringExtra("boardId").toString()
        Log.d("boardId", boardId)

        //뒤로가기
        binding.backImg.setOnClickListener {
            finish()
        }
        getBoardItem(boardId)

        // 댓글 post
        binding.postBtn.setOnClickListener {
            postCommentModel.content = binding.editComment.text.toString()
            val retrofitWork = CommentRetrofitWork(userToken.toString(), boardId ,postCommentModel)
            retrofitWork.work()
            binding.editComment.text = null
            binding.recyclerView.apply { listAdapter.notifyDataSetChanged() }
        }
        //댓글 get
        getCommentList(boardId)

        //adapter적용
        binding.recyclerView.apply {
            listAdapter.setList(commentList)
            listAdapter.notifyDataSetChanged()
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@BoardInsideActivity)
            setHasFixedSize(true)
        }


    }

    private fun getBoardItem(id: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getPostIdByEnqueue("Bearer $userToken", id)

        call.enqueue(object : Callback<GetBoardModel> {
            override fun onResponse(call: Call<GetBoardModel>, response: Response<GetBoardModel>) {
                if (response.isSuccessful) {
                    val boardModel: GetBoardModel? = response.body()

                    if(boardModel != null){
                        val korCategory = BoardCategory.fromEngName(boardModel.category ?: "")?.korName
                        binding.category.text =  korCategory
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

    enum class BoardCategory(val korName: String) {
        FREE("자유게시판"),
        QUESTION("질문게시판"),
        STUDY("스터디게시판");

        companion object {
            fun fromEngName(engName: String): BoardCategory? {
                return values().find { it.name == engName }
            }
        }
    }

    private fun getCommentList(postId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getCommentByEnqueue("Bearer $userToken", postId)
        val listAdapter = CommentListAdapter()

        call.enqueue(object : Callback<List<GetCommentModel>> {
            override fun onResponse(
                call: Call<List<GetCommentModel>>,
                response: Response<List<GetCommentModel>>
            ) {
                if (response.isSuccessful) {
                    val boardModelList: List<GetCommentModel>? = response.body()

                    if (boardModelList != null) {
                        commentList = boardModelList
                        listAdapter.setList(commentList)

                        binding.recyclerView.adapter = listAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<GetCommentModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}