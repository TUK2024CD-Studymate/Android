package com.example.studymate

import android.content.Context
import android.content.SharedPreferences
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

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

    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getUserByEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    val nickname = user?.nickname ?: ""
                    saveNicknameToSharedPreferences(nickname)
                } else {

                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun saveNicknameToSharedPreferences(nickname: String) {
        val editor = sharedPreferences.edit()
        editor.putString("nickname", nickname)
        editor.apply()
    }
}