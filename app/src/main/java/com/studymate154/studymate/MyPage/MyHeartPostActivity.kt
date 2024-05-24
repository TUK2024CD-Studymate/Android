package com.studymate154.studymate.MyPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.studymate154.studymate.R
import com.studymate154.studymate.board.BoardInsideActivity
import com.studymate154.studymate.board.BoardListAdapter
import com.studymate154.studymate.board.GetBoardModel
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.ActivityMyHeartPostBinding
import com.studymate154.studymate.databinding.ActivityMyPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyHeartPostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyHeartPostBinding
    var boardList = listOf<GetBoardModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listAdapter: BoardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyHeartPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        listAdapter = BoardListAdapter(object : BoardListAdapter.OnItemClickListener {
            override fun onItemClick(boardModel: GetBoardModel) {
                val intent = Intent(this@MyHeartPostActivity, BoardInsideActivity::class.java)
                intent.putExtra("boardId", boardModel.post_id)
                startActivity(intent)
            }
        })

        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@MyHeartPostActivity)
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }

        getMyPost()


    }

    private fun getMyPost() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getMyHeartPostEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<List<GetBoardModel>> {
            override fun onResponse(call: Call<List<GetBoardModel>>, response: Response<List<GetBoardModel>>) {
                if (response.isSuccessful) {
                    val boardModelList: List<GetBoardModel>? = response.body()

                    if (boardModelList != null) {
                        boardList = boardModelList
                    }
                    listAdapter.setList(boardList)
                    binding.recyclerView.adapter = listAdapter
                }
            }

            override fun onFailure(call: Call<List<GetBoardModel>>, t: Throwable) {
                Log.e("getBoardList", "Network request failed", t)
            }
        })
    }
}