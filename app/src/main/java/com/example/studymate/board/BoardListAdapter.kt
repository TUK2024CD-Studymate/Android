package com.example.studymate.board

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.databinding.BoardItemListBinding

class BoardListAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<BoardListAdapter.MyView>() {

    private var boardList = listOf<GetBoardModel>()
    private var filteredList = listOf<GetBoardModel>()


    inner class MyView(private val binding: BoardItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(boardModel: GetBoardModel) {
            binding.titleText.text = boardModel.title
            binding.dateText.text = boardModel.createdAt
            binding.nicknameText.text = boardModel.nickname

            itemView.setOnClickListener {
                itemClickListener.onItemClick(boardModel)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListAdapter.MyView {
        val view = BoardItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: BoardListAdapter.MyView, position: Int) {
        holder.bind(boardList[position])
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GetBoardModel>) {
        boardList = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(boardModel: GetBoardModel)
    }




}