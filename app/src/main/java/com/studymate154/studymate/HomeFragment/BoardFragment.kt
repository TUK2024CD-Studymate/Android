package com.studymate154.studymate.HomeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.studymate154.studymate.R
import com.studymate154.studymate.board.*
import com.studymate154.studymate.databinding.FragmentBoardBinding
import com.google.android.material.tabs.TabLayout
import com.studymate154.studymate.Model.GetBoardModel
import com.studymate154.studymate.board.BoardAdapter.BoardListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        //메뉴 이벤트
        binding.menuImg.setOnClickListener {
            showOptionMenu(it)
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
                boardList = emptyList()
                listAdapter.setList(boardList)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        //서취뷰
        initSearchView()

        binding.writeBtn.setOnClickListener {
            val intent = Intent(requireContext(), BoardWriteActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    //게시글 불러오기
    private fun getBoardList(category: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService

        lifecycleScope.launch(Dispatchers.IO) {
            val response = call.getPostByEnqueue("Bearer $userToken", category)

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val boardModelList: List<GetBoardModel>? = response.body()

                    if (boardModelList != null) {
                        // 해당 카테고리에 맞게 필터링
                        val filteredList = boardModelList.filter { it.category == category }
                        Log.d("filteredList",filteredList.toString())
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
        }


    }

    //게시글 시간순으로 정렬
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

    //searchview 사용
    private fun initSearchView() {
        // init SearchView
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { //검색을 완료하였을 경우 (키보드에 있는 '검색' 돋보기 버튼을 선택하였을 경우)
                query?.let { getSearch(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { //검색어를 변경할 때마다 실행됨
                newText?.let { getSearch(it) }

                return true
            }
        })
    }

    //검색어 사용
    private fun getSearch(keyword: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService

        lifecycleScope.launch(Dispatchers.IO) {
            val response = call.getPostSearchEnqueue("Bearer $userToken", keyword)

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val boardModelList: List<GetBoardModel>? = response.body()

                    if (boardModelList != null) {

                        boardList = boardModelList
                        listAdapter.setList(boardList)
                        binding.recyclerView.adapter = listAdapter

                    } else {
                        Log.e("getBoardList", "Failed to convert response to List<GetBoardModel>")
                    }
                }
            }
        }
    }

    //메뉴 아이템 클릭 이벤트
    private fun showOptionMenu(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.board_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    currentSortOrder = if (currentSortOrder == SortOrder.TIME_DESCENDING) {
                        SortOrder.TIME_ASCENDING
                    } else {
                        SortOrder.TIME_DESCENDING
                    }
                    sortAndRefreshList()
                    true
                }
                // 다른 메뉴 아이템에 대한 처리도 추가할 수 있습니다.
                else -> false
            }
        }
        popupMenu.show()
    }

}


