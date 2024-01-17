package com.example.studymate.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.ProfileSetting
import com.example.studymate.databinding.FragmentPhoneNumberBinding

class NameFragment : Fragment() {
    lateinit var binding: FragmentPhoneNumberBinding

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as ProfileSetting
        mainActivity.receiveData(this, mapOf("name" to binding.editName.text.toString()) )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)

        return binding.root
    }

}