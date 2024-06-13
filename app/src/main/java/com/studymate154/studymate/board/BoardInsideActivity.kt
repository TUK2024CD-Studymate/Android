package com.studymate154.studymate.board

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
import com.bumptech.glide.Glide
import com.studymate154.studymate.R
import com.studymate154.studymate.chatting.ChatMessageAdapter
import com.studymate154.studymate.databinding.ActivityBoardInsideBinding
import com.studymate154.studymate.signUp.SignUpResponseBody
import com.studymate154.studymate.signUp.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var sharedPreferences: SharedPreferences
    var commentList = listOf<GetCommentModel>()
    private lateinit var boardId: String
    private lateinit var boardNickname: String
    private lateinit var nickname: String

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
                    val boardModel = response.body()

                    if(boardModel != null){
                        val korCategory = BoardCategory.fromEngName(boardModel.category ?: "")?.korName
                        boardNickname = boardModel.nickname.toString()
                        binding.category.text =  korCategory
                        binding.title.text = boardModel.title
                        binding.nickname.text = boardModel.nickname
                        binding.date.text = boardModel.createdAt
                        binding.content.text = boardModel.content

                        val imageUrl = boardModel.profileUrl

                        if(imageUrl == "프로필 사진이 없습니다."){
                            binding.userImg.setImageResource(R.drawable.mento_image)
                        }else {
                            Glide.with(this@BoardInsideActivity)
                                .load(imageUrl)
                                .into(binding.userImg)
                        }

                        binding.startChatBtn.setOnClickListener {
                            postChatRoom(boardModel.nickname.toString())
                        }

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
                R.id.put -> {
                    getUser()
                    true
                }
                // 다른 메뉴 아이템에 대한 처리도 추가할 수 있습니다.
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getUserByEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    nickname = user?.nickname.toString()

                    // getUser 함수에서 닉네임을 성공적으로 받아온 후에 메뉴 처리 실행
                    Log.d("boardName", nickname)
                    Log.d("boardName", boardNickname)
                    if (nickname == boardNickname) {
                        val intent = Intent(this@BoardInsideActivity, BoardPutActivity::class.java)
                        intent.putExtra("boardId",boardId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@BoardInsideActivity, "수정 할 권한이 없습니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle failure to get user data
                    Log.e("getUser", "Failed to get user data. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle failure
                Log.e("getUser", "Network request failed", t)
            }
        })
    }

    //채팅방 생성
    private fun postChatRoom(targetNickname : String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.postChatRoom("Bearer $userToken",targetNickname)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Toast.makeText(this@BoardInsideActivity, "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show()

                } else {
                    Log.e("getUser", "Failed to get user data. Response code: ${response.code()}")
                    Toast.makeText(this@BoardInsideActivity, "채팅방이 이미 존재 합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // Handle failure
                Log.e("getUser", "Network request failed", t)
            }
        })
    }


}