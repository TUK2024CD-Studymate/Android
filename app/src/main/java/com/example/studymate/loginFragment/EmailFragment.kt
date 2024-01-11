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
import com.example.studymate.R
import com.example.studymate.databinding.FragmentEmailBinding
import com.example.studymate.databinding.FragmentStartBinding

class EmailFragment : Fragment() {
    lateinit var binding: FragmentEmailBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailBinding.inflate(inflater, container, false)

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