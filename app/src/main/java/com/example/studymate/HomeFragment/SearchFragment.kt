package com.example.studymate.HomeFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.R
import com.example.studymate.databinding.FragmentSearchBinding
import com.example.studymate.search.MentoListAdapter
import com.example.studymate.search.MentoModel

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // 더미데이터
        val dummyData = listOf(
            MentoModel("최지혜","코딩"),
            MentoModel("박환","코딩"),
            MentoModel("김선재재","코딩"),
            MentoModel("김희수","코딩"),
            MentoModel("정우혁","코딩"),

        )

        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("멘토를 선택해주세요")
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.mento_list_dialog,null)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.mentoRecyclerview)

        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = MentoListAdapter()
            adapter.setList(dummyData)
            recyclerView.adapter = adapter
            addItemDecoration(itemDecoration)
        }

        builder.setView(dialogView)
        val alertDialog = builder.create()

        val items = resources.getStringArray(R.array.interests_array)
        val myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner1.adapter = myAdapter

        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.searchMento.setOnClickListener {
            alertDialog.show()
        }


        return binding.root
    }


}