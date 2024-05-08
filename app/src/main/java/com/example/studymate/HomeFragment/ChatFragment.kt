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
import com.example.studymate.chatting.ChatRoom
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
    var roomList = listOf<ChatRoom>()
    private lateinit var listAdapter: ChatRoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)

        getMyRoomList()

        listAdapter = ChatRoomAdapter(object : ChatRoomAdapter.OnItemClickListener {
            override fun onItemClick(roomModel: ChatRoom) {
                val intent = Intent(requireContext(), RoomActivity::class.java)
                intent.putExtra("roomId", roomId)
                startActivity(intent)
            }
        })

        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }



        return binding.root
    }

    //나의 채팅방 목록 불러오기
    private fun getMyRoomList() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getMyRoom("Bearer $userToken")

        call.enqueue(object : Callback<List<ChatRoom>> {
            override fun onResponse(call: Call<List<ChatRoom>>, response: Response<List<ChatRoom>>) {
                if (response.isSuccessful) {
                    val roomModelList = response.body()
                    roomModelList?.let { rooms ->
                        if (rooms.isNotEmpty()) {
                            val firstRoom = rooms[0]
                            roomId = firstRoom.chatRoomId.toString()
                            Log.d("roomModelList", rooms.toString())
                            roomList = rooms
                            listAdapter.setList(roomList)
                            activity?.runOnUiThread {
                                binding.recyclerView.adapter = listAdapter
                            }
                        } else {
                            // 빈 목록 처리
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ChatRoom>>, t: Throwable) {
                Log.e("getBoardList", "Network request failed", t)
            }
        })
    }

}