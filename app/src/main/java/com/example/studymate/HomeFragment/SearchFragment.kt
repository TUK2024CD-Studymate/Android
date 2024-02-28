package com.example.studymate.HomeFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.studymate.StudyRecord.RecordInsideActivity
import com.example.studymate.board.CommentListAdapter
import com.example.studymate.board.GetCommentModel
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentSearchBinding
import com.example.studymate.search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listAdapter: MentoListAdapter
    var matchingList = listOf<GetMatchingModel>()

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val dummyData = listOf(
            MentoModel("최지혜","코딩"),
            MentoModel("박환","코딩"),
            MentoModel("김선재재","코딩"),
            MentoModel("김희수","코딩"),
            MentoModel("정우혁","코딩"),

            )

        val quesData = QuesModel(null,null,null)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")


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

            adapter.setOnItemClickListener(object :
            MentoListAdapter.OnItemClickListener{
                override fun onItemClick(v: View, data: Int, pos: Int) {
                    Intent(requireContext(),MentoInfoActivity::class.java).run { startActivity(this) }
                }

            }
            )

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
                    1 -> {
                        quesData.interests = "MATH"
                    }
                    2 -> {
                        quesData.interests = "PROGRAMMING"
                    }
                    3 -> {
                        quesData.interests = "KOREAN"
                    }
                    4 -> {
                        quesData.interests = "ENGLISH"
                    }
                    5 -> {
                        quesData.interests = "SCIENCE"
                    }
                    6 -> {
                        quesData.interests = "SOCIETY"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //@post
        binding.searchMento.setOnClickListener {
            quesData.title = binding.titleEdit.text.toString()
            quesData.content = binding.contentEdit.text.toString()
//            val retrofitWork = SearchRetrofitWork(userToken.toString(),quesData)
//            retrofitWork.work(object : SearchRetrofitWork.Callback {
//                override fun onQuestionPosted(questionId: String?) {
//                    Log.d("Question ID", questionId.toString())
//                    getMatchingList(questionId.toString())
//                }
//
//                override fun onFailure(message: String) {
//                }
//            })

            alertDialog.show()
        }


        return binding.root
    }

//    private fun getMatchingList(quesId: String) {
//        val userToken = sharedPreferences.getString("userToken", "") ?: ""
//        val call = PostRetrofitAPI.emgMedService.getMatchingList("Bearer $userToken", quesId)
//        val inflater = layoutInflater
//        val dialogView = inflater.inflate(R.layout.mento_list_dialog,null)
//
//        call.enqueue(object : Callback<List<GetMatchingModel>> {
//            override fun onResponse(
//                call: Call<List<GetMatchingModel>>,
//                response: Response<List<GetMatchingModel>>
//            ) {
//                if (response.isSuccessful) {
//                    val matchingModelList: List<GetMatchingModel>? = response.body()
//
//                    if (matchingModelList != null) {
//                        matchingList = matchingModelList
//                        val listAdapter = MentoListAdapter()
//                        listAdapter.setList(matchingList)
//
//                        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.mentoRecyclerview)
//                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//                        recyclerView.adapter = listAdapter
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<GetMatchingModel>>, t: Throwable) {
//            }
//        })
//    }


}