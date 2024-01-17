package com.example.studymate.loginFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.ProfileSetting
import com.example.studymate.databinding.FragmentStartBinding

class PartFragment : Fragment() {
    lateinit var binding: FragmentStartBinding

    override fun onStop() {
        super.onStop()
        Log.d("daeYoung", "성별 프라그먼트 onStop() 호출")
        val mainActivity = activity as ProfileSetting
        if(binding.mentiBtn.isSelected) { mainActivity.receiveData(this, mapOf("part" to binding.mentiBtn.text.toString()) ) }
        else { mainActivity.receiveData(this, mapOf("part" to binding.mentorBtn.text.toString())) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.mentorBtn.setOnClickListener {
            it.isSelected = !(it.isSelected)
            binding.mentiBtn.isSelected = false
        }

        binding.mentiBtn.setOnClickListener {
            it.isSelected = !(it.isSelected)
            binding.mentorBtn.isSelected = false
        }

        return binding.root
    }

}