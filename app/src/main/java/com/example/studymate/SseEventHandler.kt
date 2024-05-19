package com.example.studymate


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.studymate.board.BoardInsideActivity
import com.launchdarkly.eventsource.MessageEvent
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import org.json.JSONObject
import android.content.Context

class SseEventHandler(private val context : Context) : BackgroundEventHandler {

    override fun onOpen() {
        // SSE 연결 성공시 처리 로직 작성
        Log.d("SSE","SSE연결 성공")
    }

    override fun onClosed() {
        // SSE 연결 종료시 처리 로직 작성
    }

    override fun onMessage(event: String?, messageEvent: MessageEvent?) {



        Log.d("SSE", "Received data: ${messageEvent?.data}")
        // SSE 이벤트 도착시 처리 로직 작성

        val data  = messageEvent?.data

        data?.let {
            val jsonObject = JSONObject(it)
            val nickname = jsonObject.getString("nickname")
            val postId = jsonObject.getInt("post_id")
            val commentTime = jsonObject.getString("commentTime")
            Toast.makeText(context, "$nickname 님이 댓글을 남겼습니다.", Toast.LENGTH_SHORT).show()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationIntent = Intent(context, BoardInsideActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("postId", postId)
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(context, "studyMateChannelId")
                .setContentTitle("알림")
                .setContentText("$nickname 님이 댓글을 남겼습니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(postId, notification)
        }

    }

    override fun onComment(comment: String?) {
    }

    override fun onError(t: Throwable?) {
        Log.d("SSE","SSE연결 실패")
    }


}