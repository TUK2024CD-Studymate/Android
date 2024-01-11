package com.example.studymate.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.R
import com.example.studymate.databinding.FragmentRegionBinding
import com.example.studymate.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    lateinit var binding: FragmentStartBinding

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