package com.example.studymate.HomeFragment

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studymate.HomeActivity
import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.board.*
import com.example.studymate.databinding.FragmentBoardBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding
    var boardList = listOf<GetBoardModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listAdapter: BoardListAdapter



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)



        listAdapter = BoardListAdapter(object : BoardListAdapter.OnItemClickListener {
            override fun onItemClick(boardModel: GetBoardModel) {
                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                startActivity(intent)
            }
        })



        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)

        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 클릭된 탭에 따라서 해당 카테고리에 대한 게시글을 불러옴
                when (tab.position) {
                    0 -> getBoardList("FREE")
                    1 -> getBoardList("QUESTION")
                    2 -> getBoardList("STUDY")
                    // 다른 탭 추가 가능
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.writeBtn.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }





        return binding.root
    }



    private fun getBoardList(category: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getPostByEnqueue("Bearer $userToken", category)
        val listAdapter = BoardListAdapter(object : BoardListAdapter.OnItemClickListener {
            override fun onItemClick(boardModel: GetBoardModel) {
                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                intent.putExtra("boardId", boardModel.id)
                startActivity(intent)
            }
        })

        call.enqueue(object : Callback<List<GetBoardModel>> {
            override fun onResponse(
                call: Call<List<GetBoardModel>>,
                response: Response<List<GetBoardModel>>
            ) {
                if (response.isSuccessful) {
                    val boardModelList: List<GetBoardModel>? = response.body()

                    if (boardModelList != null) {
                        // 해당 카테고리에 맞게 필터링
                        val filteredList = boardModelList.filter { it.category == category }

                        boardList = filteredList
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


