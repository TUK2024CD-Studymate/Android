package com.studymate154.studymate.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.studymate154.studymate.R
import com.studymate154.studymate.databinding.MentoReviewItemBinding

class MentoReviewAdapter :  RecyclerView.Adapter<MentoReviewAdapter.MyView>() {

    private var reviewList = listOf<ReviewModel>()

    inner class MyView(private val binding: MentoReviewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pos: Int){
            binding.nameText.text = reviewList[pos].writer
            binding.ratingBar.rating = reviewList[pos].star.toFloat()
            binding.createAt.text = reviewList[pos].createAt
            binding.content.text = reviewList[pos].content

            if(reviewList[pos].imageUrl == "프로필 사진이 없습니다"){
                binding.profileImage.setImageResource(R.drawable.mento_image)
            }else {
                Glide.with(binding.root)
                    .load(reviewList[pos].imageUrl)
                    .into(binding.profileImage)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentoReviewAdapter.MyView {
        val view = MentoReviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MentoReviewAdapter.MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    fun setList(list: List<ReviewModel>){
        reviewList = list
    }

}