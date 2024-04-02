package com.example.studymate.HomeFragment

import CustomReviewDialogFragment
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.studymate.R
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentMypageBinding
import com.example.studymate.databinding.FragmentSearchBinding
import com.example.studymate.signUp.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)

        //회원정보 로드
        getUser()

//        binding.btn.setOnClickListener {
//            val customDialogFragment = CustomReviewDialogFragment()
//            customDialogFragment.show(requireActivity().supportFragmentManager, "CustomDialog")
//        }


        return binding.root
    }

    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getUserByEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    binding.nameText.text = user!!.name
                    binding.idText.text = user.nickname
                    binding.interestsText.text = user.interests
                    binding.emailText.text = user.email
                    binding.phoneText.text = user.tel
                    binding.urlText.text = user.blogUrl
                    binding.jobText.text = user.job
                    binding.mentorInfoText.text = user.publicRelations

                } else {

                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                // Handle failure
            }
        })
    }

}