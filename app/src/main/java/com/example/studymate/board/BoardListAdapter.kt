package com.example.studymate.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.StudyRecord.StudyModel
import com.example.studymate.databinding.BoardItemListBinding

class BoardListAdapter() : RecyclerView.Adapter<BoardListAdapter.MyView>() {

    private var boardList = listOf<GetBoardModel>()

    inner class MyView(private val binding :BoardItemListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(pos: Int){
            binding.titleText.text = boardList[pos].title
            binding.dateText.text = boardList[pos].createdAt
            binding.nicknameText.text = boardList[pos].nickname
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListAdapter.MyView {
        val view = BoardItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: BoardListAdapter.MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    fun setList(list: List<GetBoardModel>) {
        boardList = list
    }
}