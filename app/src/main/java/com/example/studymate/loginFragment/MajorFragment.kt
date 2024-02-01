package com.example.studymate.loginFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.studymate.ProfileSetting
import com.example.studymate.databinding.FragmentMajorBinding
import org.json.JSONObject

class MajorFragment : Fragment() {
    lateinit var binding: FragmentMajorBinding
    private lateinit var majorList: List<Button>

    override fun onStop() {
        super.onStop()
        Log.d("parkHwan", "성별 프라그먼트 onStop() 호출")

        // 선택된 버튼에 따라 JSON 형식의 문자열 생성
        val selectedButtonText = when {
            binding.majorBtn1.isSelected -> binding.majorBtn1.text.toString()
            binding.majorBtn2.isSelected -> binding.majorBtn2.text.toString()
            binding.majorBtn3.isSelected -> binding.majorBtn3.text.toString()
            binding.majorBtn4.isSelected -> binding.majorBtn4.text.toString()
            binding.majorBtn5.isSelected -> binding.majorBtn5.text.toString()
            else -> binding.majorBtn6.text.toString()
        }

        // JSON 객체를 생성하고 키 "interests"에 해당 버튼의 텍스트를 넣어줍니다.
        val json = JSONObject().apply {
            put("interests", selectedButtonText)
        }

        // ProfileSetting 액티비티의 receiveData 함수 호출
        val mainActivity = activity as ProfileSetting
        mainActivity.receiveData(this, json.toString())
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMajorBinding.inflate(inflater, container, false)

        majorList = listOf(
            binding.majorBtn1,
            binding.majorBtn2,
            binding.majorBtn3,
            binding.majorBtn4,
            binding.majorBtn5,
            binding.majorBtn6
        )

        majorList.forEach { Button ->
            val remainList = majorList.filter { it != Button }

            Button.setOnClickListener {
                it.isSelected = !(it.isSelected)
                remainList.forEach { it.isSelected = false }
                Log.d("daeYoung", "선택된 뷰: ${(it as TextView).text}")
            }
        }

        return binding.root
    }


}