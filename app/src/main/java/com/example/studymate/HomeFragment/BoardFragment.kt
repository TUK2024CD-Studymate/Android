package com.example.studymate.HomeFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.HomeActivity
import com.example.studymate.board.BoardWriteActivity
import com.example.studymate.databinding.FragmentBoardBinding



class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater, container, false)

        binding.writeBtn.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }


}