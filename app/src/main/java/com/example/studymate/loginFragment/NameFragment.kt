package com.example.studymate.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.ProfileSetting
import com.example.studymate.databinding.FragmentNameBinding
import org.json.JSONObject

class NameFragment : Fragment() {
    lateinit var binding: FragmentNameBinding

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as ProfileSetting
        val jsonData = JSONObject().apply {
            put("name", binding.editName.text.toString())
        }.toString()
        mainActivity.receiveData(this, jsonData)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNameBinding.inflate(inflater, container, false)

        return binding.root
    }

}