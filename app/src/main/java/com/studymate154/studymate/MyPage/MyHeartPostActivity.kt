package com.studymate154.studymate.MyPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.studymate154.studymate.board.BoardInsideActivity
import com.studymate154.studymate.board.BoardAdapter.BoardListAdapter
import com.studymate154.studymate.Model.GetBoardModel
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.ActivityMyHeartPostBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        onRefresh()

    }

    //내가 좋아요 누른 게시물 불러오기
    private fun getMyPost() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService

       lifecycleScope.launch(Dispatchers.IO) {
           val response = call.getMyHeartPostEnqueue("Bearer $userToken")

           withContext(Dispatchers.Main){
               val boardModelList: List<GetBoardModel>? = response.body()

               if (boardModelList != null) {
                   boardList = boardModelList
               }
               listAdapter.setList(boardList)
               binding.recyclerView.adapter = listAdapter
           }
       }
    }

    //새로고침
    private fun onRefresh(){
        binding.refreshLayout.setOnRefreshListener {
            getMyPost()

            binding.refreshLayout.isRefreshing = false
        }
    }
}