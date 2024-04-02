package com.example.studymate.HomeFragment

import CustomReviewDialogFragment
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.studymate.*
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentMypageBinding
import com.example.studymate.databinding.FragmentSearchBinding
import com.example.studymate.signUp.SignUpResponseBody
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

        //내 게시물
        binding.myPost.setOnClickListener {
            val intent = Intent(requireContext(),MyPostActivity::class.java)
            startActivity(intent)

        }

        //회원탈퇴
        binding.userDelete.setOnClickListener {
            showConfirmDialog()
        }

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

    private fun deleteUser() {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.deleteUser("Bearer $userToken")

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if (response.isSuccessful) {
                    val user = response.body()
                }
            }
            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun showConfirmDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("회원 탈퇴")
        alertDialogBuilder.setMessage("정말 탈퇴하시겠습니까?")
        alertDialogBuilder.setPositiveButton("예") { dialog, _ ->
            deleteUser()
            dialog.dismiss()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("아니오") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}