package com.example.studymate.HomeFragment

import CustomReviewDialogFragment
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.R
import com.example.studymate.databinding.FragmentMypageBinding
import com.example.studymate.databinding.FragmentSearchBinding


class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.btn.setOnClickListener {
            val customDialogFragment = CustomReviewDialogFragment()
            customDialogFragment.show(requireActivity().supportFragmentManager, "CustomDialog")
        }


        return binding.root
    }

}