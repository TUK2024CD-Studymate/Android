package com.example.studymate


import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.studymate.board.BoardInsideActivity
import com.launchdarkly.eventsource.MessageEvent
import com.launchdarkly.eventsource.background.BackgroundEventHandler

class SseEventHandler : BackgroundEventHandler {

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

    }

    override fun onComment(comment: String?) {
    }

    override fun onError(t: Throwable?) {
        Log.d("SSE","SSE연결 실패")
    }


}