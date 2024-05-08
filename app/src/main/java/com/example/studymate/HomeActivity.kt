package com.example.studymate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.studymate.HomeFragment.*
import com.example.studymate.board.GetBoardModel
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.ActivityHomeBinding
import com.example.studymate.databinding.ActivityMainBinding
import com.example.studymate.signUp.User
import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")

        // SSE 연결
        val eventSource: BackgroundEventSource = BackgroundEventSource //백그라운드에서 이벤트를 처리하기위한 EVENTSOURCE의 하위 클래스
            .Builder(
                SseEventHandler(this),
                EventSource.Builder(
                    ConnectStrategy
                        .http(URL("http://studymate154.com:8080/api/subscribe/${userToken}"))
                        // 서버와의 연결을 설정하는 타임아웃
                        .connectTimeout(3, TimeUnit.SECONDS)
                        // 서버로부터 데이터를 읽는 타임아웃 시간
                        .readTimeout(600, TimeUnit.SECONDS)
                )
            )
            .threadPriority(Thread.MAX_PRIORITY) //백그라운드 이벤트 처리를 위한 스레드 우선 순위를 최대로 설정합니다.
            .build()

        // EventSource 연결 시작
        eventSource.start()

        //알림 채널 생성
        createNotificationChannel(this)


        //초기 화면 로드
        loadFragment(SearchFragment())

        //바텀 네비게이션
        // 바 클릭스 프래그먼트 변경
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.searchMento->{
                    loadFragment(SearchFragment())
                    true
                }
                R.id.chat->{
                    loadFragment(ChatFragment())
                    true
                }
                R.id.board->{
                    loadFragment(BoardFragment())
                    true
                }
                R.id.record->{
                    loadFragment(RecordFragment())
                    true
                }
                R.id.mypage->{
                    loadFragment(MypageFragment())
                    true
                }
                else -> false
            }
        }
    }
    // 프래그먼트 로드 함수
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "StudyMate Notifications"
            val descriptionText = "알림 채널 설명"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("studyMateChannelId", name, importance).apply {
                description = descriptionText
            }
            // 알림 매니저 등록
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}