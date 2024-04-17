package com.example.studymate

import android.util.Log
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
        // SSE 이벤트 도착시 처리 로직 작성

        // event: String = 이벤트가 속한 채널 또는 토픽 이름
        // messageEvent.lastEventId: String = 도착한 이벤트 ID
        // messageEvent.data: String = 도착한 이벤트 데이터
    }

    override fun onComment(comment: String?) {
        TODO("Not yet implemented")
    }

    override fun onError(t: Throwable?) {
        // SSE 연결 전 또는 후 오류 발생시 처리 로직 작성
    }

}