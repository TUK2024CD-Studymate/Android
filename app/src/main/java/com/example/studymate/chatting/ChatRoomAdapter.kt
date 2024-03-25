package com.example.studymate.chatting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.board.GetBoardModel
import com.example.studymate.databinding.BoardItemListBinding
import com.example.studymate.databinding.RoomItemListBinding

class ChatRoomAdapter: RecyclerView.Adapter<ChatRoomAdapter.MyView>() {

    private var roomList = listOf<RoomDto>()

    inner class MyView(private val binding: RoomItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomModel: RoomDto) {
            binding.name.text = roomModel.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomAdapter.MyView {
        val view = RoomItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: ChatRoomAdapter.MyView, position: Int) {
        holder.bind(roomList[position])
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RoomDto>) {
        roomList = list
        notifyDataSetChanged()
    }
}