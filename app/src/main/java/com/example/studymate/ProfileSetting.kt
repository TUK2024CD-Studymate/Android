package com.example.studymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.studymate.databinding.ActivityProfileSettingBinding
import com.example.studymate.loginFragment.*
import com.example.studymate.signUp.RetrofitWork
import com.google.gson.Gson
import org.json.JSONObject

class ProfileSetting : AppCompatActivity() {
    private lateinit var binding: ActivityProfileSettingBinding

    private val fragmentList : List<Fragment> = listOf(
        EmailFragment(),
        PasswordFragment(),
        NameFragment(),
        PartFragment(),
        MajorFragment(),
        NicknameFragment(),
        FinishFragment()
    )


    private var cursor = 1

    private var signUpData: User = User(null, null, null, null, null, null)

    private val stepProgressAmount = 8

    private fun increaseProgress(){
        binding.progressBar.progress += stepProgressAmount
    }

    private fun decreaseProgress(){
        binding.progressBar.progress -= stepProgressAmount
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.max = stepProgressAmount * fragmentList.size
        binding.progressBar.progress = stepProgressAmount

        //시작 프래그먼트 로드
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout,EmailFragment())
            //백스택에 저장하는 이유는 사용자가 활동에서 뒤로 이동하면 다시 이전 프래그먼트 로드 가능 원래는 백스택에 저장되지 않음
            .addToBackStack(null)
            .commit()

        // 뒤로가기 이미지 클릭시 이벤트
        binding.backImg.setOnClickListener {
            if(cursor > 1){
                supportFragmentManager.popBackStack()
                cursor--
                decreaseProgress()
            } else finish()
        }

        //다음 버튼 클릭시 다음 프래그먼트 로드
        binding.nextBtn.setOnClickListener {
            // 커서의 크기가 프래그먼트리스트 사이즈보다 작으면 change()함수를 불러와 다음 커서로 이동
            if (cursor < fragmentList.size){
                changeFragment(fragmentList[cursor++])
                increaseProgress()
                changeState()
            //커서의 크기가 프래그먼트리스트 사이즈랑 같다면 화면 스택을 전체 제거하고 메인엑티비티로 이동
            }else if(cursor ==fragmentList.size){
                ++cursor
                changeState()
            }
        }
    }


    // 프래그먼트를 변경하는 함수
    private fun changeFragment(fragment: Fragment, tag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun changeState() {
        when(cursor) {
            1 -> {
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            2 -> {
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            3 -> {
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            4 -> {
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            5 -> {
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            6 -> {
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            7 ->{
                Log.d("parkHwan", "cursor는 ${cursor}번째")
            }
            8->{

                val retrofitWork = RetrofitWork(signUpData)
                retrofitWork.work()
                finishAffinity()  // 화면 스택 전체 제거
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    fun receiveData(fragment: Fragment, data: String) {
        Log.d("parkHwan", "${fragment.toString()} 프라그먼트의 데이터: $data")

        // JSON 문자열을 JSONObject로 파싱
        val jsonObject = JSONObject(data)

        when (fragment) {
            is EmailFragment -> {
                // EmailFragment에서 필요한 데이터만 가져와서 User에 저장
                signUpData.email = jsonObject.optString("email", "")
            }
            is PasswordFragment -> {
                // PasswordFragment에서 필요한 데이터만 가져와서 User에 저장
                signUpData.password = jsonObject.optString("password", "")
            }
            is NameFragment -> {
                // NameFragment에서 필요한 데이터만 가져와서 User에 저장
                signUpData.name = jsonObject.optString("name", "")
            }
            is PartFragment -> {
                // PartFragment에서 필요한 데이터만 가져와서 User에 저장
                signUpData.part = jsonObject.optString("part", "")
            }
            is MajorFragment -> {
                // InterestsFragment에서 필요한 데이터만 가져와서 User에 저장
                signUpData.interests = jsonObject.optString("interests", "")
            }
            is NicknameFragment -> {
                // NicknameFragment에서 필요한 데이터만 가져와서 User에 저장
                signUpData.nickname = jsonObject.optString("nickname", "")
            }
            // 추가적인 프래그먼트에 대한 처리 추가
        }

        // 로그에 출력
        Log.d("parkHwan", "userData: $signUpData")
    }



    }

