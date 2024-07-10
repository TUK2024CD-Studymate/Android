package com.studymate154.studymate.search

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.studymate154.studymate.Model.ReviewModel
import com.studymate154.studymate.board.PostRetrofitAPI
import com.studymate154.studymate.databinding.ActivityMentoInfoBinding
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
        //프로필 이미지 설정

        val name = intent.getStringExtra("name")
        val starAverage = intent.getDoubleExtra("starAverage",0.0) // 기본값을 0.0f로 설정합니다.
        val solved = intent.getIntExtra("solved",0)
        val matchingCount = intent.getIntExtra("matchingCount",0) // 기본값을 0으로 설정합니다.

        Log.d("MentoInfoActivity", "Name: $name")
        Log.d("MentoInfoActivity", "Star Average: $starAverage")
        Log.d("MentoInfoActivity", "Solved: $solved")
        Log.d("MentoInfoActivity", "Matching Count: $matchingCount")

        val id = intent.getStringExtra("id").toString()
        binding.nameText.text = name.toString()
        binding.solved.text = solved.toString()
        binding.matchingCount.text = matchingCount.toString()
        binding.ratingBar.rating = starAverage.toFloat()

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