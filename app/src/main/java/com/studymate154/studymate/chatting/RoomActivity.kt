package com.studymate154.studymate.chatting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.ActivityChattingRoomBinding
import com.studymate154.studymate.signUp.User
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingRoomBinding
    var jsonObject = JSONObject()
    //쉐얼드프리퍼런스
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nickname: String
    private lateinit var chatMessageAdapter: ChatMessageAdapter

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        Log.d("parkhwan", userToken)

        //줌 로그인 이벤트
        binding.zoomLoginBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://zoom.us/oauth/authorize?response_type=code&client_id=Zgt89KiZRri8SkBqws0SRg&redirect_uri=http%3A%2F%2F3.36.177.42%2Fapi%2Fmeeting%2FzoomApi")
            )
            startActivity(intent)
        }

        //줌링크 이벤트
        binding.getLinkBtn.setOnClickListener {
            getZoomLink()
        }

        //리뷰 이벤트
        binding.reviewBtn.setOnClickListener {
            val customDialogFragment = CustomReviewDialogFragment()
            customDialogFragment.show(this.supportFragmentManager, "CustomDialog")
        }

        getUser()

        //룸 아이디
        val roomId = intent.getStringExtra("roomId").toString()
        Log.d("roomId", roomId)


        val url = "wss://studymate154.com/ws/chat"

        // 스톰프 url생성
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)


        stompClient.topic("/sub/chat/room/${roomId}").subscribe() { topicMessage ->
            Log.i("message Recieve", topicMessage.payload)
            try {
                val messageData = JSONObject(topicMessage.payload)
                val sender = messageData.getString("sender")
                val content = messageData.getString("content")
                val messageModel = MessageModel(sender, content)

                // UI 업데이트
                runOnUiThread {
                    chatMessageAdapter.addMessage(messageModel)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        val headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("Authorization", "Bearer $userToken"))
        stompClient.connect(headerList)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
//
//                    jsonObject.put("type","ENTER")
//                    jsonObject.put("chatRoomId", roomId)
//                    jsonObject.put("sender", nickname)
//                    jsonObject.put("content", "입장")
//                    stompClient.send("/pub/chat/message/${roomId}", jsonObject.toString())

                    binding.sendBtn.setOnClickListener {
                        try {
                            jsonObject.put("type","TALK")
                            jsonObject.put("chatRoomId", roomId)
                            jsonObject.put("sender", nickname)
                            jsonObject.put("content", binding.editMessage.text.toString())
                            Log.d("send",jsonObject.toString())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        stompClient.send("/pub/chat/message/${roomId}", jsonObject.toString())
                            .subscribe {
                                // 성공적으로 메시지를 전송한 경우
                                Log.d("SendMessage", "Message sent successfully")
                                binding.editMessage.text = null // 메시지 전송 후 EditText 비우기
                            }
                    }
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i("CLOSED", "!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i("ERROR", "!!")
                    Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                }
                else -> {
                    Log.i("ELSE", lifecycleEvent.message)
                }
            }

        }
    }


    //내 정보 불러오기
    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getUserByEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    nickname = user!!.nickname.toString()
                    chatMessageAdapter = ChatMessageAdapter(nickname)
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@RoomActivity)
                    binding.recyclerView.adapter = chatMessageAdapter
                } else {

                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun getZoomLink() {
//        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getZoomLink()

        call.enqueue(object : Callback<ZoomLinkModel> {
            override fun onResponse(call: Call<ZoomLinkModel>, response: Response<ZoomLinkModel>) {
                if (response.isSuccessful) {
                    val link = response.body()
                    val joinUrl = link!!.join_url.toString()
                    binding.editMessage.setText("$joinUrl")
                } else {

                }
            }
            override fun onFailure(call: Call<ZoomLinkModel>, t: Throwable) {
                // Handle failure
            }
        })
    }



}