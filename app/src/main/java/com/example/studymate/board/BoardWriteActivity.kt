package com.example.studymate.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.studymate.R
import com.example.studymate.databinding.ActivityBoardWriteBinding
import com.example.studymate.databinding.ActivityHomeBinding

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val items = resources.getStringArray(R.array.my_array)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = myAdapter


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        // 처리 코드
                    }
                    1 -> {
                        // 처리 코드
                    }
                    2 -> {
                        // 처리 코드
                    }
                    3 -> {
                        // 처리 코드
                    }
                    4 -> {
                        // 처리 코드
                    }
                    // ...
                    else -> {
                        // 처리 코드
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}