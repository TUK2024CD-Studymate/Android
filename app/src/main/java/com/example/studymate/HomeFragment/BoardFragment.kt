package com.example.studymate.HomeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studymate.HomeActivity
import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.board.BoardListAdapter
import com.example.studymate.board.BoardWriteActivity
import com.example.studymate.board.GetBoardModel
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding
    var boardList = listOf<GetBoardModel>()
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val listAdapter = BoardListAdapter()
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)

        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }

        //@get
        getBoardList()

        binding.writeBtn.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

    private fun getBoardList() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getPostByEnqueue("Bearer $userToken")
        val listAdapter = BoardListAdapter()

        call.enqueue(object : Callback<List<GetBoardModel>> {
            override fun onResponse(
                call: Call<List<GetBoardModel>>,
                response: Response<List<GetBoardModel>>
            ) {
                if (response.isSuccessful) {
                    val boardModelList: List<GetBoardModel>? = response.body()

                    if (boardModelList != null) {
                        boardList = boardModelList
                        listAdapter.setList(boardList)

                        activity?.runOnUiThread {
                            binding.recyclerView.adapter = listAdapter
                        }
                    } else {
                        Log.e("getBoardList", "Failed to convert response to List<GetBoardModel>")
                    }
                }
            }

            override fun onFailure(call: Call<List<GetBoardModel>>, t: Throwable) {
                Log.e("getBoardList", "Network request failed", t)
            }
        })

    }


}