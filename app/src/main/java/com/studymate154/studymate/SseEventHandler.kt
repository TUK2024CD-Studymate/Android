package com.studymate154.studymate


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.studymate154.studymate.board.BoardInsideActivity
import com.launchdarkly.eventsource.MessageEvent
import com.launchdarkly.eventsource.background.BackgroundEventHandler
import org.json.JSONObject
import android.content.Context

class SseEventHandler(private val context: Context) : BackgroundEventHandler {

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
        val jsonObject = JSONObject(data!!)

        val nickname = jsonObject.getString("nickname")

        val message = when(event){
            "Like" ->  "$nickname 님이 회원님의 게시글을 좋아합니다."
            "Comment" -> "$nickname 님이 회원님의 게시글에 댓글을 남겼습니다"
            "Matching" ->  "$nickname 님께서 매칭을 요청하였습니다"
            else -> ""
        }
        Log.d("parkhwan",message)


        val builder = NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("알림")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.notify(1, builder.build())
            Log.d("Notification", "Notification created successfully")
        } catch (e: Exception) {
            Log.e("Notification", "Failed to create notification: ${e.message}")
        }

    }

    override fun onComment(comment: String?) {
    }

    override fun onError(t: Throwable?) {
        Log.d("SSE","SSE연결 실패")
    }


}