package com.studymate154.studymate.SignUpFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.studymate154.studymate.ProfileSetting
import com.studymate154.studymate.databinding.FragmentPartBinding
import org.json.JSONObject

class PartFragment : Fragment() {
    lateinit var binding: FragmentPartBinding

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as ProfileSetting
        val partValue = if(binding.mentiBtn.isSelected) {
            binding.mentiBtn.text.toString()
        } else {
            binding.mentorBtn.text.toString()
        }
        val jsonData = JSONObject().apply {
            put("part", partValue)
        }.toString()
        mainActivity.receiveData(this, jsonData)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPartBinding.inflate(inflater, container, false)

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