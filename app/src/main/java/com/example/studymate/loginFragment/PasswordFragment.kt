package com.example.studymate.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.studymate.ProfileSetting
import com.example.studymate.R
import com.example.studymate.databinding.ActivityProfileSettingBinding
import com.example.studymate.databinding.FragmentPasswordBinding
import org.json.JSONObject

class PasswordFragment : Fragment() {
    lateinit var binding: FragmentPasswordBinding

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as ProfileSetting
        val jsonData = JSONObject().apply {
            put("password", binding.editPasswd1.text.toString())
        }.toString()
        mainActivity.receiveData(this, jsonData)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)

        binding.editPasswd2.addTextChangedListener { editable ->
            val password1 = binding.editPasswd1.text.toString()
            val password2 = editable?.toString() ?: ""

            binding.pwConfirm.text = if (password1 == password2) {
                "비밀번호가 일치합니다"
            } else {
                "비밀번호가 일치하지 않습니다"
            }
        }

        return binding.root
    }


}