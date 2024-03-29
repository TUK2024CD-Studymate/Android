package com.example.studymate.chatting

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.Klaxon
import com.example.studymate.databinding.ActivityChattingRoomBinding
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingRoomBinding
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    var jsonObject = JSONObject()
    //쉐얼드프리퍼런스
    private val SHARED_PREFERENCES_NAME = "MySharedPreferences"
    private val NICKNAME_KEY = "nickname"
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 닉네임을 쉐어드 프리퍼런스에서 가져오기
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val nickname = sharedPreferences.getString(NICKNAME_KEY, "") ?: ""

        //룸 아이디
        val roomId = intent.getStringExtra("roomId").toString()
        Log.d("roomId", roomId)

        val chatMessageAdapter = ChatMessageAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = chatMessageAdapter

        val url = "ws://study-mate.kro.kr:8080/ws/chat"
        val intervalMillis = 1000L
        val client = OkHttpClient()

        // 스톰프 url생성
        val stomp = StompClient(client, intervalMillis).apply {
            this@apply.url = url
        }

        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    topic = stomp.join("/sub/chat/room/${roomId}").subscribe { stompMessage ->
                        val responseData = JSONObject(stompMessage).getString("message")
                        val nickname = JSONObject(stompMessage).getString("sender")
                        Log.d("ReceivedMessage", "Received message: $responseData")
                        val messageModel = MessageModel(nickname, responseData) // 상대방 메시지이므로 고정된 값으로 설정

                        runOnUiThread {
                            chatMessageAdapter.addMessage(messageModel)
                        }
                    }

                    try {
                        jsonObject.put("type", "ENTER")
                        jsonObject.put("roomId", roomId)
                        jsonObject.put("sender", nickname)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    binding.sendBtn.setOnClickListener {
                        try {
                            jsonObject.put("type", "TALK")
                            jsonObject.put("roomId", roomId)
                            jsonObject.put("sender", "박환")
                            jsonObject.put("message", binding.editMessage.text.toString())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        stomp.send("/pub/chat/message/${roomId}}", jsonObject.toString())
                            .subscribe {
                                // 성공적으로 메시지를 전송한 경우
                                Log.d("SendMessage", "Message sent successfully")
                                binding.editMessage.text = null // 메시지 전송 후 EditText 비우기
                            }
                    }

                }

                Event.Type.CLOSED -> {
                }
                Event.Type.ERROR -> {
                    Log.e("web", "err")
                }
                else -> {}
            }
        }
    }
}