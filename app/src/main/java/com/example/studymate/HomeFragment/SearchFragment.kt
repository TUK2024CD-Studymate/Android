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
import com.google.gson.annotations.SerializedName
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

        val quesData = QuesModel(null,null,null)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")

        listAdapter = MentoListAdapter() // Initialize listAdapter here

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
            val retrofitWork = SearchRetrofitWork(userToken.toString(),quesData)
            retrofitWork.work(object : SearchRetrofitWork.Callback {
                override fun onQuestionPosted(questionId: String?) {
                    Log.d("Question ID", questionId.toString())
                    getMatchingList(questionId.toString())
                }

                override fun onFailure(message: String) {
                }
            })
        }


        return binding.root
    }

    private fun getMatchingList(quesId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getMatchingList("Bearer $userToken", quesId)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.mento_list_dialog,null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.mentoRecyclerview)

        call.enqueue(object : Callback<GetMatchingResponse> {
            override fun onResponse(
                call: Call<GetMatchingResponse>,
                response: Response<GetMatchingResponse>
            ) {
                if (response.isSuccessful) {
                    val matchingResponse: GetMatchingResponse? = response.body()

                    if (matchingResponse != null) {
                        matchingList = matchingResponse.memberList
                        listAdapter.setList(matchingList)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        listAdapter.setOnItemClickListener(object :
                            MentoListAdapter.OnItemClickListener{
                            override fun onItemClick(v: View, data: Int, pos: Int) {
                                val intent = Intent(requireContext(),MentoInfoActivity::class.java)
                                intent.putExtra("name",matchingList[pos].name)
                                intent.putExtra("nickname",matchingList[pos].nickname)
                                intent.putExtra("interests",matchingList[pos].interests)
                                intent.putExtra("email",matchingList[pos].email)
                                intent.putExtra("url",matchingList[pos].blogUrl)
                                intent.putExtra("job",matchingList[pos].job)
                                intent.putExtra("info",matchingList[pos].publicRelations)
                                startActivity(intent)
                            }
                        })
                        recyclerView.adapter = listAdapter
                        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

                        // 다이얼로그를 표시
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("멘토를 선택해주세요")
                            .setView(dialogView)
                            .create()
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<GetMatchingResponse>, t: Throwable) {
            }
        })
    }


}