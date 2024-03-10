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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    enum class SortOrder {
        TIME_ASCENDING, TIME_DESCENDING
    }
    private var currentSortOrder: SortOrder = SortOrder.TIME_DESCENDING
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoardBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        getBoardList("FREE")

        listAdapter = BoardListAdapter(object : BoardListAdapter.OnItemClickListener {
            override fun onItemClick(boardModel: GetBoardModel) {
                val intent = Intent(requireContext(), BoardInsideActivity::class.java)
                intent.putExtra("boardId", boardModel.post_id)
                startActivity(intent)
            }
        })
        // 게시글 시간으로 정렬
        binding.sortImage.setOnClickListener {
            currentSortOrder = if (currentSortOrder == SortOrder.TIME_DESCENDING) {
                SortOrder.TIME_ASCENDING
            } else {
                SortOrder.TIME_DESCENDING
            }
            sortAndRefreshList()
        }



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
                getBoardList("FREE")
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

        call.enqueue(object : Callback<List<GetBoardModel>> {
            override fun onResponse(call: Call<List<GetBoardModel>>, response: Response<List<GetBoardModel>>) {
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
    @SuppressLint("NotifyDataSetChanged")
    private fun sortAndRefreshList() {
        // 시간순으로 정렬
        when (currentSortOrder) {
            SortOrder.TIME_ASCENDING -> boardList = boardList.sortedBy { it.createdAt }
            SortOrder.TIME_DESCENDING -> boardList = boardList.sortedByDescending { it.createdAt }
        }

        // 어댑터에 정렬된 리스트 설정 및 갱신
        listAdapter.setList(boardList)
        listAdapter.notifyDataSetChanged()
    }

}


