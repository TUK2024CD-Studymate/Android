package com.example.studymate.HomeFragment

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
import com.example.studymate.MyPage.LogoutModel
import com.example.studymate.MyPage.MyPostActivity
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentMypageBinding
import com.example.studymate.signUp.SignUpResponseBody
import com.example.studymate.signUp.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding
    private lateinit var sharedPreferences: SharedPreferences
    val logoutModel = LogoutModel(null,null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)

        //회원정보 로드
        getUser()

        //내 게시물로 이동
        binding.myPost.setOnClickListener {
            val intent = Intent(requireContext(), MyPostActivity::class.java)
            startActivity(intent)

        }

        //회원탈퇴
        binding.userDelete.setOnClickListener {
            showDeleteDialog()
        }

        //로그아웃
        binding.logout.setOnClickListener {
            showLogoutDialog()
        }




        return binding.root
    }

    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "")
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

    private fun logout() {
        val userToken = sharedPreferences.getString("userToken", "")
        val refreshToken = sharedPreferences.getString("refreshToken","")
        logoutModel.accessToken = userToken
        logoutModel.refreshToken = refreshToken
        val call = PostRetrofitAPI.emgMedService.postLogout("Bearer $userToken",logoutModel)

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

    private fun showDeleteDialog() {
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

    private fun showLogoutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("로그아웃")
        alertDialogBuilder.setMessage("로그아웃하시겠습니까?")
        alertDialogBuilder.setPositiveButton("예") { dialog, _ ->
            logout()
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