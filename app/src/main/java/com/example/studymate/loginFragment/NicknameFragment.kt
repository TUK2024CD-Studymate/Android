package com.example.studymate.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.ProfileSetting
import com.example.studymate.R
import com.example.studymate.databinding.FragmentNicknameBinding
import org.json.JSONObject

class NicknameFragment : Fragment() {
    lateinit var binding: FragmentNicknameBinding

    override fun onStop() {
        super.onStop()

        // 프래그먼트 데이터를 JSON 형식의 문자열로 생성
        val jsonData = JSONObject().apply {
            put("nickname", binding.editNickname.text.toString())
        }.toString()

        // ProfileSetting 액티비티의 receiveData 함수 호출
        val mainActivity = activity as ProfileSetting
        mainActivity.receiveData(this, jsonData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNicknameBinding.inflate(inflater, container, false)

        return binding.root
    }


}