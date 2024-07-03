package com.studymate154.studymate.loginFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.studymate154.studymate.ProfileSetting
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.FragmentNameBinding
import com.studymate154.studymate.signUp.SignUpResponseBody
import com.studymate154.studymate.signUp.UserInfoModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameFragment : Fragment() {
    lateinit var binding: FragmentNameBinding
    private var signUpData = UserInfoModel(null, null, null,null, null, null, null,null,null,null)
    private var verifyData : MessageVerifyModel = MessageVerifyModel(null,null)
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
            val phoneNumber = binding.editTel.text.toString()
            if (phoneNumber.length != 11) {
                // 전화번호가 11자리가 아닌 경우 예외 처리
                Toast.makeText(context, "전화번호를 형식에 맞게 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 전화번호가 11자리인 경우에만 인증번호 요청
                signUpData.tel = phoneNumber
                postTel(signUpData)
                binding.verifyNumEdit.visibility = View.VISIBLE
                binding.verifyNumBtn.visibility = View.VISIBLE
            }
        }
        binding.verifyNumBtn.setOnClickListener {
            verifyData.phoneNumber = binding.editTel.text.toString()
            verifyData.randomNumber = binding.verifyNumEdit.text.toString()
            postVerify(verifyData)
        }

        return binding.root
    }

    private fun postTel(userTel : UserInfoModel){
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
    private fun postVerify(userVerify : MessageVerifyModel){
        val call = PostRetrofitAPI.emgMedService.postVerifyByEnqueue(userVerify)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(
                call: Call<SignUpResponseBody>,
                response: Response<SignUpResponseBody>
            ) {
                when (response.code()) {
                    200 -> {
                        Toast.makeText(context, "인증번호가 일치합니다.", Toast.LENGTH_SHORT).show()
                    }
                    401 -> {
                        Toast.makeText(context, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // 통신 실패 시
                Toast.makeText(context, "네트워크 연결 실패", Toast.LENGTH_SHORT).show()
                Log.d("로그인 통신 실패", t.message.toString())
            }
        })
    }

}