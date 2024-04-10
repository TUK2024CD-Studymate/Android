package com.example.studymate.search

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studymate.R
import com.example.studymate.board.CommentListAdapter
import com.example.studymate.board.GetCommentModel
import com.example.studymate.board.PostRetrofitAPI
import com.example.studymate.databinding.ActivityMentoInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MentoInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentoInfoBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentoInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val id = intent.getStringExtra("id").toString()

        binding.nameText.text = intent.getStringExtra("name").toString()
        binding.solved.text = intent.getStringExtra("solved").toString()
        binding.matchingCount.text = intent.getStringExtra("matchingCount").toString()
        binding.ratingBar.rating = intent.getDoubleExtra("starAverage", 0.0).toFloat()

        val listAdapter = MentoReviewAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MentoInfoActivity)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = listAdapter
        }

        getReviewList(id)






    }

    private fun getReviewList(mentorId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = PostRetrofitAPI.emgMedService.getMentorReview("Bearer $userToken", mentorId)
        val listAdapter = MentoReviewAdapter()

        call.enqueue(object : Callback<List<ReviewModel>> {
            override fun onResponse(
                call: Call<List<ReviewModel>>,
                response: Response<List<ReviewModel>>
            ) {
                if (response.isSuccessful) {
                    val reviewModelList: List<ReviewModel>? = response.body()

                    if (reviewModelList != null) {
                        listAdapter.setList(reviewModelList)

                        binding.recyclerView.adapter = listAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<ReviewModel>>, t: Throwable) {
            }
        })
    }
}