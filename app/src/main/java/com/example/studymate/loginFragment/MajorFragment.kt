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
import com.example.studymate.R
import com.example.studymate.databinding.FragmentMajorBinding
import com.example.studymate.databinding.FragmentNicknameBinding

class MajorFragment : Fragment() {
    lateinit var binding: FragmentMajorBinding
    private lateinit var majorList: List<Button>

    override fun onStop() {
        super.onStop()
        Log.d("parkHwan", "성별 프라그먼트 onStop() 호출")
        val mainActivity = activity as ProfileSetting
        if(binding.majorBtn1.isSelected) { mainActivity.receiveData(this, mapOf("interests" to binding.majorBtn1.text.toString()) ) }
        else if(binding.majorBtn2.isSelected){mainActivity.receiveData(this, mapOf("interests" to binding.majorBtn2.text.toString()))}
        else if(binding.majorBtn3.isSelected){mainActivity.receiveData(this, mapOf("interests" to binding.majorBtn3.text.toString()))}
        else if(binding.majorBtn4.isSelected){mainActivity.receiveData(this, mapOf("interests" to binding.majorBtn4.text.toString()))}
        else if(binding.majorBtn5.isSelected){mainActivity.receiveData(this, mapOf("interests" to binding.majorBtn5.text.toString()))}
        else{ mainActivity.receiveData(this, mapOf("interests" to binding.majorBtn6.text.toString()))}
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