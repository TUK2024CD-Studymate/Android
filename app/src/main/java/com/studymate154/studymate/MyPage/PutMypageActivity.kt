package com.studymate154.studymate.MyPage


import android.content.SharedPreferences

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.studymate154.studymate.board.PostRetrofitAPI

import com.studymate154.studymate.databinding.ActivityPutMypageBinding
import com.studymate154.studymate.Model.GetMatchingModel
import com.studymate154.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PutMypageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPutMypageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPutMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val putUserModel = PutUserModel("","","","","","","","")

        binding.saveBtn.setOnClickListener {
            putUserModel.blogUrl = binding.urlText.text.toString()
            putUserModel.job = binding.jobText.text.toString()
            putUserModel.expertiseField = binding.mentorInfoText.text.toString()
            putUserModel.interests = binding.interestsText.text.toString()
            putUserModel.name = binding.nameText.text.toString()
            putUserModel.nickname = binding.idText.text.toString()
            putUserModel.part = binding.partText.text.toString()
            putUserModel.publicRelations = binding.urlText.text.toString()
            Log.d("model", "$putUserModel")

            putUser(putUserModel)
        }

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        getUser()



    }

    //회원정보 불러오기
    private fun getUser() {
        val userToken = sharedPreferences.getString("userToken", "")
        val call = PostRetrofitAPI.emgMedService.getUserByEnqueue("Bearer $userToken")

        call.enqueue(object : Callback<GetMatchingModel> {
            override fun onResponse(call: Call<GetMatchingModel>, response: Response<GetMatchingModel>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    // 나머지 유저 정보 설정
                    binding.nameText.text = user?.name?.toEditable()
                    binding.idText.text = user?.nickname?.toEditable()
                    binding.interestsText.text = user?.interests?.toEditable()
                    binding.phoneText.text = user?.tel?.toEditable()
                    binding.urlText.text = user?.blogUrl?.toEditable()
                    binding.jobText.text = user?.job?.toEditable()
                    binding.partText.text = user?.part?.toEditable()


                }
            }
            override fun onFailure(call: Call<GetMatchingModel>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun String?.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this ?: "")
    }

    //회원정보 수정
    private fun putUser(putUserModel: PutUserModel) {
        val userToken = sharedPreferences.getString("userToken", "")
        val call = PostRetrofitAPI.emgMedService.putUserEnqueue("Bearer $userToken",putUserModel)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if (response.isSuccessful) {
                    finish()
                } else Toast.makeText(this@PutMypageActivity, "회원정보 수정 실패", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {

                Toast.makeText(this@PutMypageActivity, "회원정보 수정 실패", Toast.LENGTH_SHORT).show()

            }
        })
    }


}
