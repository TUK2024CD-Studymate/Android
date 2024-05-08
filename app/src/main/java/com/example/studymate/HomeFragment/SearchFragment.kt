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
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.R
import com.example.studymate.board.CommentListAdapter
import com.example.studymate.board.GetCommentModel
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentSearchBinding
import com.example.studymate.search.*
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listAdapter: MentoListAdapter
    var matchingList = listOf<GetMatchingModel>()
    private lateinit var quesId: String
    private var alertDialog: AlertDialog? = null

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

        //질문 post
        binding.searchMento.setOnClickListener {
            val title = binding.titleEdit.text.toString().trim()
            val content = binding.contentEdit.text.toString().trim()
            val specify = binding.specifyField.text.toString()
            val selectedField = binding.spinner1.selectedItemPosition

            if (title.isEmpty() || content.isEmpty() || selectedField == 0) {
                Toast.makeText(requireContext(),"질문, 내용, 분야를 선택해주세요!",Toast.LENGTH_SHORT).show()
                Log.d("SearchFragment", "Please fill in all fields.")
                return@setOnClickListener
            }

            quesData.title = title
            quesData.content =content
            quesData.specificField = specify
            val retrofitWork = SearchRetrofitWork(userToken.toString(),quesData)
            retrofitWork.work(object : SearchRetrofitWork.Callback {
                override fun onQuestionPosted(questionId: String?) {
                    Log.d("Question ID", questionId.toString())
                    quesId = questionId!!
                    getMatchingList(questionId.toString())
                }

                override fun onFailure(message: String) {
                }
            })

            binding.titleEdit.text = null
            binding.contentEdit.text = null
            binding.specifyField.text = null
        }


        return binding.root
    }

    //KMP 적용한 멘토 조회
    private fun getMatchingList(quesId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getMatchingList("Bearer $userToken", quesId)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.mento_list_dialog,null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.mentoRecyclerview)

        call.enqueue(object : Callback<List<GetMatchingModel>> {
            override fun onResponse(
                call: Call<List<GetMatchingModel>>,
                response: Response<List<GetMatchingModel>>
            ) {
                val matchingResponse: List<GetMatchingModel>? = response.body()

                if (matchingResponse != null) {
                    matchingList = matchingResponse
                    listAdapter.setList(matchingList)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    listAdapter.setOnItemClickListener(object : MentoListAdapter.OnItemClickListener {
                        override fun onInfoClick(item: GetMatchingModel) {
                            val intent = Intent(requireContext(),MentoInfoActivity::class.java)
                            intent.putExtra("name",item.name)
                            intent.putExtra("starAverage",item.starAverage)
                            intent.putExtra("solved",item.solved)
                            intent.putExtra("id",item.id)
                            intent.putExtra("matchingCount",item.matchingCount)
                            startActivity(intent)
                        }

                        @SuppressLint("CommitPrefEdits")
                        override fun onNameClick(item: GetMatchingModel) {
                            alertDialog?.dismiss()

                            sendMatchingAlert(quesId,item.id)

                            val chatFragment = ChatFragment().apply {
                                arguments = Bundle().apply {
                                    putString("nickname",item.nickname)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("mentorId", item.id)
                                    editor.apply()
                                }
                            }
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.container, chatFragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                        }
                    })
                    recyclerView.adapter = listAdapter
                    recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

                    // 다이얼로그를 표시
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("멘토를 선택해주세요")
                        .setView(dialogView)
                        .setCancelable(true)
                        .create()
                        .also {dialog->
                            alertDialog = dialog
                            dialog.show()
                        }
                } else {
                    // 서버로부터의 응답이 null이거나 실패했을 때 처리할 내용을 여기에 추가할 수 있습니다.
                }
            }

            override fun onFailure(call: Call<List<GetMatchingModel>>, t: Throwable) {
                // 서버 통신 실패 시 처리할 내용을 여기에 추가할 수 있습니다.
            }
        })
    }


    private fun sendMatchingAlert(questionID: String,mentorId : String ) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.sendMatchingAlert("Bearer $userToken", questionID, mentorId)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(
                call: Call<SignUpResponseBody>,
                response: Response<SignUpResponseBody>
            ) {
                if (response.isSuccessful) {
                    val signUpResponseBody: SignUpResponseBody? = response.body()
                    Log.d("sendMatchingAlert",signUpResponseBody.toString())
                }
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
            }
        })
    }



}