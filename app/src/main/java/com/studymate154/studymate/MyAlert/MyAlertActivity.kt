package com.studymate154.studymate.MyAlert

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.studymate154.studymate.Model.GetBoardModel
import com.studymate154.studymate.Model.GetMyAlertModel
import com.studymate154.studymate.R
import com.studymate154.studymate.board.BoardAdapter.BoardListAdapter
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.ActivityMyAlertBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAlertActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyAlertBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listAdapter: MyAlertAdapter
    var alertList = listOf<GetMyAlertModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAlertBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        listAdapter = MyAlertAdapter()

        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@MyAlertActivity)
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }

        getMyAlert()



    }

    //내 알림 불러오기
    private fun getMyAlert() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService

        lifecycleScope.launch(Dispatchers.IO) {
            val response = call.getMyAlert("Bearer $userToken")

            withContext(Dispatchers.Main){
                val alertModelList: List<GetMyAlertModel>? = response.body()

                if (alertModelList != null) {
                    alertList = alertModelList
                }
                listAdapter.setList(alertList)
                binding.recyclerView.adapter = listAdapter
            }
        }
    }
}