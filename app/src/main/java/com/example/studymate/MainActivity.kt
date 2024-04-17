package com.example.studymate

import android.content.Intent
import java.util.concurrent.TimeUnit
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.studymate.databinding.ActivityMainBinding
import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //gif 이미지 넣기
        Glide.with(this).load(R.drawable.study).into(binding.gifImage)

        binding.LoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupText.setOnClickListener {
            val intent = Intent(this, ProfileSetting::class.java)
            startActivity(intent)
        }

        // SSE 연결
        val eventSource: BackgroundEventSource = BackgroundEventSource
            .Builder(
                SseEventHandler(),
                EventSource.Builder(
                    ConnectStrategy
                            //유저 아이디 설정해줘야됨 아직 설정안해둠
                        .http(URL("study-mate.kro.kr:8080/subscribe/{user-id}"))
                        // 커스텀 요청 헤더를 명시
                        .connectTimeout(3, TimeUnit.SECONDS)
                        // 최대 연결 유지 시간을 설정, 서버에 설정된 최대 연결 유지 시간보다 길게 설정
                        .readTimeout(600, TimeUnit.SECONDS)
                )
            )
            .threadPriority(Thread.MAX_PRIORITY)
            .build()

// EventSource 연결 시작
        eventSource.start()


    }

}