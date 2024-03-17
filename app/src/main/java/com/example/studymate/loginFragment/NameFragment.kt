package com.example.studymate.loginFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.studymate.ProfileSetting
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.FragmentNameBinding
import com.example.studymate.signUp.LoginBackendResponse
import com.example.studymate.signUp.SignUpResponseBody
import com.example.studymate.signUp.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameFragment : Fragment() {
    lateinit var binding: FragmentNameBinding
    private var signUpData: User = User(null, null, null,null, null, null, null,null,null,null)
    override fun onStop() {
        super.onStop()
        val mainActivity = activity as ProfileSetting
        val jsonData = JSONObject().apply {
            put("name", binding.editName.text.toString())
            put("tel",binding.editTel.text.toString())
        }.toString()
        mainActivity.receiveData(this, jsonData)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNameBinding.inflate(inflater, container, false)

        binding.getNumBtn.setOnClickListener {
            signUpData.tel = binding.editTel.text.toString()
            postTel(signUpData)
            binding.verifyNumEdit.visibility = View.VISIBLE
            binding.verifyNumBtn.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun postTel(userTel : User){
        val call = PostRetrofitAPI.emgMedService.postTelByEnqueue(userTel)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(
                call: Call<SignUpResponseBody>,
                response: Response<SignUpResponseBody>
            ) {
                Log.d("로그인 통신 성공", response.toString())
                Log.d("로그인 통신 성공", response.body().toString())
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // 실패
                Log.d("로그인 통신 실패", t.message.toString())
                Log.d("로그인 통신 실패", "fail")
            }
        })
    }

}