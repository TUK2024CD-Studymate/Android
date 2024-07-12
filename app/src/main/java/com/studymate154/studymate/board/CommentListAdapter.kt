package com.studymate154.studymate.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studymate154.studymate.Model.GetCommentModel
import com.studymate154.studymate.databinding.CommentItemBinding

class CommentListAdapter() : RecyclerView.Adapter<CommentListAdapter.MyView>() {

    private var commentList = listOf<GetCommentModel>()

    fun setList(list: List<GetCommentModel>){
        commentList=list
        notifyItemInserted(commentList.size-1)
    }

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

}