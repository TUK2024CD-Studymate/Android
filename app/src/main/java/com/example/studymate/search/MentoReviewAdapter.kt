package com.example.studymate.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.databinding.MentoReviewItemBinding

class MentoReviewAdapter :  RecyclerView.Adapter<MentoReviewAdapter.MyView>() {

    private var reviewList = listOf<ReviewModel>()

    inner class MyView(private val binding: MentoReviewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pos: Int){
            binding.nameText.text = reviewList[pos].writer
            binding.ratingBar.rating = reviewList[pos].star.toFloat()
            binding.createAt.text = reviewList[pos].createAt
            binding.content.text = reviewList[pos].content
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