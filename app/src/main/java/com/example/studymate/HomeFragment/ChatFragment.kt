package com.example.studymate.HomeFragment

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
import com.example.studymate.board.BoardInsideActivity
import com.example.studymate.board.BoardListAdapter
import com.example.studymate.board.GetBoardModel
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.chatting.ChatRoomAdapter
import com.example.studymate.chatting.RoomActivity
import com.example.studymate.chatting.RoomDto
import com.example.studymate.databinding.FragmentChatBinding
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment() {
    lateinit var binding : FragmentChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomId : String
    var roomList = listOf<RoomDto>()
    private lateinit var listAdapter: ChatRoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val roomName = arguments?.getString("nickname").toString()

        listAdapter = ChatRoomAdapter(object : ChatRoomAdapter.OnItemClickListener {
            override fun onItemClick(roomModel: RoomDto) {
                val intent = Intent(requireContext(), RoomActivity::class.java)
                intent.putExtra("roomId", roomModel.roomId)
                startActivity(intent)
            }
        })

        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }

        //채팅방 가져오기
        getRoomList(roomName)


        return binding.root
    }
    private fun getRoomList(name: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getRoomList("Bearer $userToken", name)

        call.enqueue(object : Callback<List<RoomDto>> {
            override fun onResponse(call: Call<List<RoomDto>>, response: Response<List<RoomDto>>) {
                if (response.isSuccessful) {
                    val roomModelList: List<RoomDto>? = response.body()

                    if (roomModelList != null) {
                        // 해당 카테고리에 맞게 필터링
                        val filteredList = roomModelList.filter { it.name == name }
                        Log.d("filteredList",filteredList.toString())

                        roomList = filteredList
                        listAdapter.setList(roomList)

                        activity?.runOnUiThread {
                            binding.recyclerView.adapter = listAdapter
                        }
                    } else {
                        Log.e("getBoardList", "Failed to convert response to List<GetBoardModel>")
                    }
                }
            }

            override fun onFailure(call: Call<List<RoomDto>>, t: Throwable) {
                Log.e("getBoardList", "Network request failed", t)
            }
        })
    }

}