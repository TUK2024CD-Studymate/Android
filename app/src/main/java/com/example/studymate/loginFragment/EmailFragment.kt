package com.example.studymate.loginFragment

import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.studymate.ProfileSetting
import com.example.studymate.R
import com.example.studymate.databinding.FragmentEmailBinding
import org.json.JSONObject

class EmailFragment : Fragment() {
    lateinit var binding: FragmentEmailBinding

    override fun onStop() {
        super.onStop()

        // 프래그먼트 데이터를 JSON 형식의 문자열로 생성
        val jsonData = JSONObject().apply {
            put("email", binding.editEmail.text.toString())
        }.toString()

        // ProfileSetting 액티비티의 receiveData 함수 호출
        val mainActivity = activity as ProfileSetting
        mainActivity.receiveData(this, jsonData)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailBinding.inflate(inflater, container, false)


        return binding.root
    }

}