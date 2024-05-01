package com.example.studymate.board

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studymate.HomeFragment.BoardFragment
import com.example.studymate.R
import com.example.studymate.SseEventHandler
import com.example.studymate.databinding.ActivityBoardInsideBinding
import com.example.studymate.signUp.SignUpResponseBody
import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var sharedPreferences: SharedPreferences
    var commentList = listOf<GetCommentModel>()
    private lateinit var boardId: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardInsideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // SSE 연결
        val eventSource: BackgroundEventSource = BackgroundEventSource //백그라운드에서 이벤트를 처리하기위한 EVENTSOURCE의 하위 클래스
            .Builder(
                SseEventHandler(),
                EventSource.Builder(
                    ConnectStrategy
                        //유저 아이디 설정해줘야됨 아직 설정안해둠
                        .http(URL("http://10.0.2.2:8080/subscribe/11"))
                        // 서버와의 연결을 설정하는 타임아웃
                        .connectTimeout(3, TimeUnit.SECONDS)
                        // 서버로부터 데이터를 읽는 타임아웃 시간
                        .readTimeout(600, TimeUnit.SECONDS)
                )
            )
            .threadPriority(Thread.MAX_PRIORITY) //백그라운드 이벤트 처리를 위한 스레드 우선 순위를 최대로 설정합니다.
            .build()

        // EventSource 연결 시작
        eventSource.start()


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

        //메뉴 이벤트
        binding.menuImg.setOnClickListener {
            showOptionMenu(it)
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
            layoutManager = LinearLayoutManager(this@BoardInsideActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = listAdapter
        }

    }

    //게시글 내용 불러오기
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

    // 카테고리 한국어로 변경
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

    //댓글 목록 불러오기
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
            }
        })
    }

    //게시글 삭제
    private fun deletePost(boardId: String, onPostDeleted: (Boolean) -> Unit) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.deletePostByEnqueue("Bearer $userToken", boardId)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    Log.d("deleteRecord", "Record deleted successfully")
                    withContext(Dispatchers.Main) {
                        onPostDeleted(true) // 성공적으로 삭제되었을 때 true 전달
                    }
                } else {
                    Log.e("deleteRecord", "Failed to delete record. Response code: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        onPostDeleted(false) // 삭제 실패 시 false 전달
                    }
                }
            } catch (e: IOException) {
                Log.e("deleteRecord", "Network request failed", e)
                withContext(Dispatchers.Main) {
                    onPostDeleted(false) // 삭제 실패 시 false 전달
                }
            }
        }
    }

    // 좋아요 누르기
    private fun postHeart(boardId : String){
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.postHeart("Bearer $userToken",boardId)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(
                call: Call<SignUpResponseBody>,
                response: Response<SignUpResponseBody>
            ) {

            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                Log.d("로그인 통신 실패", t.message.toString())
            }
        })
    }

    //아이템 메뉴
    private fun showOptionMenu(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        boardId = intent.getStringExtra("boardId").toString()
        popupMenu.menuInflater.inflate(R.menu.board_inside_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    deletePost(boardId) { isSuccess ->
                        if (isSuccess) {
                            finish()
                        } else {
                            runOnUiThread {
                                Toast.makeText(this, "삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    true
                }
                R.id.like -> {
                    postHeart(boardId)
                    true

                }
                // 다른 메뉴 아이템에 대한 처리도 추가할 수 있습니다.
                else -> false
            }
        }
        popupMenu.show()
    }

}