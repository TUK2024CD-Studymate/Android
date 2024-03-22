package com.example.studymate.HomeFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentChatBinding
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragment : Fragment() {
    lateinit var binding : FragmentChatBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomId : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val roomName = arguments?.getString("nickname").toString()
        //룸 생성
        postRoom(roomName)




        return binding.root
    }

    private fun postRoom(name : String){
        val userToken = sharedPreferences.getString("userToken", "") ?: ""

        // 채팅방 생성 요청
        val call = PostRetrofitAPI.emgMedService.postRoom("Bearer $userToken", name)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(
                call: Call<SignUpResponseBody>,
                response: Response<SignUpResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.d("로그인 통신 성공", response.toString())
                    Log.d("로그인 통신 성공", response.body().toString())
                    roomId = response.body()!!.roomId.toString()
                    Log.d("roomId",roomId)
                } else {
                    Log.d("postRoom", "Failed to create chat room. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                Log.d("postRoom", "Failed to create chat room", t)
            }
        })
    }



}