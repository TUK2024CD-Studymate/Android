package com.studymate154.studymate.chatting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nickname: String
    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private lateinit var stompClient: ua.naiksoftware.stomp.StompClient
    private val roomId: String by lazy { intent.getStringExtra("roomId").toString() }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        Log.d("parkhwan", userToken)

        chatMessageAdapter = ChatMessageAdapter("")

        binding.zoomLoginBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://zoom.us/oauth/authorize?response_type=code&client_id=Zgt89KiZRri8SkBqws0SRg&redirect_uri=http%3A%2F%2F3.36.177.42%2Fapi%2Fmeeting%2FzoomApi")
            )
            startActivity(intent)
        }

        binding.getLinkBtn.setOnClickListener {
            getZoomLink()
        }

        binding.reviewBtn.setOnClickListener {
            val customDialogFragment = CustomReviewDialogFragment()
            customDialogFragment.show(this.supportFragmentManager, "CustomDialog")
        }

        getUser()
        getChatContent(roomId)

        connectToStomp(userToken, roomId)

        binding.sendBtn.setOnClickListener {
            sendMessage(roomId)
        }
    }

    private fun connectToStomp(userToken: String, roomId: String) {
        val url = "wss://studymate154.com/ws/chat"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        val headerList = arrayListOf<StompHeader>()
        headerList.add(StompHeader("Authorization", "Bearer $userToken"))
        stompClient.connect(headerList)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> Log.i("StompClient", "Stomp connection opened")
                LifecycleEvent.Type.CLOSED -> {
                    Log.i("CLOSED", "Stomp connection closed")
                    reconnect(userToken, roomId)
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i("ERROR", "Stomp connection error")
                    Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                    reconnect(userToken, roomId)
                }
                else -> Log.i("ELSE", lifecycleEvent.message)
            }
        }

        stompClient.topic("/exchange/chat.exchange/room.${roomId}").subscribe { topicMessage ->
            Log.i("message Recieve", topicMessage.payload)
            try {
                val messageData = JSONObject(topicMessage.payload)
                val sender = messageData.getString("sender")
                val content = messageData.getString("content")
                if (sender != "박환") {
                    val messageModel = MessageModel(sender, content)
                    runOnUiThread {
                        chatMessageAdapter.addMessage(messageModel)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun reconnect(userToken: String, roomId: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            connectToStomp(userToken, roomId)
        }, 5000)
    }

    private fun sendMessage(roomId: String) {
        try {
            jsonObject.put("type", "TALK")
            jsonObject.put("chatRoomId", roomId)
            jsonObject.put("sender", nickname)
            jsonObject.put("content", binding.editMessage.text.toString())
            Log.d("send", jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        stompClient.send("/pub/chat.message.${roomId}", jsonObject.toString()).subscribe {
            Log.d("SendMessage", "Message sent successfully")
            binding.editMessage.text = null
        }
    }

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
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun getZoomLink() {
        val call = PostRetrofitAPI.emgMedService.getZoomLink()

        call.enqueue(object : Callback<ZoomLinkModel> {
            override fun onResponse(call: Call<ZoomLinkModel>, response: Response<ZoomLinkModel>) {
                if (response.isSuccessful) {
                    val link = response.body()
                    val joinUrl = link!!.join_url.toString()
                    binding.editMessage.setText(joinUrl)
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<ZoomLinkModel>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun getChatContent(chatRoomId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getChatRoomContent("Bearer $userToken", chatRoomId)

        call.enqueue(object : Callback<List<GetMessageModel>> {
            override fun onResponse(call: Call<List<GetMessageModel>>, response: Response<List<GetMessageModel>>) {
                if (response.isSuccessful) {
                    val messageList = response.body()
                    messageList?.let {
                        for (messageModel in it) {
                            if (messageModel.sender != "박환") {
                                val message = MessageModel(messageModel.sender, messageModel.content)
                                chatMessageAdapter.addMessage(message)
                            }
                        }
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<GetMessageModel>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
