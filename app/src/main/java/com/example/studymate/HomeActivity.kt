package com.example.studymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.studymate.HomeFragment.BoardFragment
import com.example.studymate.HomeFragment.ChatFragment
import com.example.studymate.HomeFragment.RecordFragment
import com.example.studymate.HomeFragment.SearchFragment
import com.example.studymate.databinding.ActivityHomeBinding
import com.example.studymate.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
}