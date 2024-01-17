package com.example.studymate.loginFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.studymate.ProfileSetting
import com.example.studymate.R
import com.example.studymate.databinding.FragmentEmailBinding
import com.example.studymate.databinding.FragmentStartBinding

class EmailFragment : Fragment() {
    lateinit var binding: FragmentEmailBinding

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as ProfileSetting
        mainActivity.receiveData(this, mapOf("email" to binding.editEmail.text.toString()) )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailBinding.inflate(inflater, container, false)


        return binding.root
    }

}