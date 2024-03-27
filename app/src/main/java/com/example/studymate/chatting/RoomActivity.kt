package com.example.studymate.chatting

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.studymate.databinding.ActivityChattingRoomBinding
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject

class RoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingRoomBinding
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    var jsonObject = JSONObject()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //룸 아이디
        val roomId = intent.getStringExtra("roomId").toString()
        Log.d("roomId",roomId)

        val url = "ws://study-mate.kro.kr:8080/ws/chat"
        val intervalMillis = 1000L
        val client = OkHttpClient()

        val stomp = StompClient(client, intervalMillis).apply {
            this@apply.url = url
        }

        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    topic = stomp.join("/sub/chat/room/${roomId}").subscribe()

                    try {
                        jsonObject.put("type", "ENTER")
                        jsonObject.put("roomId", roomId)
                        jsonObject.put("sender", "parkhwan")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    stomp.send("/pub/chat/message",jsonObject.toString()).subscribe()

                    //전송 버튼 클릭시 메시지 전송
                    binding.sendBtn.setOnClickListener {
                        try {
                            jsonObject.put("type", "TALK")
                            jsonObject.put("roomId", roomId)
                            jsonObject.put("sender", "parkhwan")
                            jsonObject.put("message", binding.editMessage.text.toString())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        stomp.send("/pub/chat/message", jsonObject.toString()).subscribe(
                            { // 성공적으로 메시지를 전송한 경우
                                Log.d("SendMessage", "Message sent successfully")
                                binding.editMessage.text = null // 메시지 전송 후 EditText 비우기
                            },
                            { // 메시지 전송 중 오류가 발생한 경우
                                Log.e("SendMessage", "Failed to send message", it)
                            }
                        )
                    }
                }

                Event.Type.CLOSED -> {
                }
                Event.Type.ERROR -> {
                }
                else -> {}
            }
        }









    }


}