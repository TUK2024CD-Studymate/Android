package com.example.studymate.chatting

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.ReviewItemBinding
import com.example.studymate.signUp.LoginApi
import com.example.studymate.signUp.SignUpResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomReviewDialogFragment : DialogFragment() {
    lateinit var binding: ReviewItemBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ReviewItemBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val reviewModel = ReviewVO()
        val id = sharedPreferences.getString("mentorId", "") ?: ""
        Log.d("shareId",id)
        val userToken = sharedPreferences.getString("userToken", "")

        //매칭 like 버튼
        binding.like.setOnClickListener {
            it.isSelected = !(it.isSelected)
            binding.notLike.isSelected = false
            reviewModel.heart = true
            Log.d("review", reviewModel.toString())
        }

        //매층 notLike 버튼
        binding.notLike.setOnClickListener {
            it.isSelected = !(it.isSelected)
            binding.like.isSelected = false
            reviewModel.heart = false
            Log.d("review", reviewModel.toString())
        }

        //문제 해결 버튼
        binding.solved.setOnClickListener {
            it.isSelected = !(it.isSelected)
            binding.notSolved.isSelected = false
            reviewModel.isSolved = true
            Log.d("review", reviewModel.toString())
        }

        //문제 해결되지 않음 버튼
        binding.notSolved.setOnClickListener {
            it.isSelected = !(it.isSelected)
            binding.solved.isSelected = false
            reviewModel.isSolved = false
            Log.d("review", reviewModel.toString())
        }

        //레이팅바
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            reviewModel.star = rating.toInt()
        }

        //POST API
        binding.submitBtn.setOnClickListener {
            reviewModel.title = binding.editTitle.text.toString()
            reviewModel.content = binding.editReview.text.toString()
            postReview(id, reviewModel)
        }


        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 1)
        val height = (resources.displayMetrics.widthPixels * 1.3).toInt()
        dialog?.window?.setLayout(width, height)
    }

    private fun postReview(id : String, reviewModel : ReviewVO){
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.addReviewByEnqueue("Bearer $userToken",id,reviewModel)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(
                call: Call<SignUpResponseBody>,
                response: Response<SignUpResponseBody>
            ) {

            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                Log.d("로그인 통신 실패", t.message.toString())
            }
        })
    }


}
