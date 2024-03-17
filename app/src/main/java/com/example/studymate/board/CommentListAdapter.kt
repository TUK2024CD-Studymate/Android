package com.example.studymate.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.databinding.CommentItemBinding

class CommentListAdapter() : RecyclerView.Adapter<CommentListAdapter.MyView>() {

    private var commentList = listOf<GetCommentModel>()

    inner class MyView(private val binding: CommentItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pos: Int){
            binding.nickname.text = commentList[pos].nickname
            binding.content.text = commentList[pos].content
            binding.time.text = commentList[pos].createdAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListAdapter.MyView {
       val view = CommentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: CommentListAdapter.MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun setList(list: List<GetCommentModel>){
        commentList = list
    }
}