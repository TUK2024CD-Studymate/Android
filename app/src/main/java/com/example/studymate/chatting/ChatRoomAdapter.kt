package com.example.studymate.chatting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.R
import com.example.studymate.databinding.RoomItemListBinding

class ChatRoomAdapter(private val itemClickListener:OnItemClickListener): RecyclerView.Adapter<ChatRoomAdapter.MyView>() {

    private var roomList = listOf<ChatRoom>()

    interface OnItemClickListener {
        fun onItemClick(roomModel: ChatRoom)
    }

    inner class MyView(private val binding: RoomItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomModel: ChatRoom) {
            val roomName = roomModel.chatRoomName.replace(" & ", " ")
            binding.name1.text = roomModel.members.joinToString(", ") { it.name }
            binding.readCount.text = roomModel.unreadMessageCount.toString()

            val login = roomModel.members.any { it.login }
            val loginImageResource = if (login) R.drawable.offl_circle_24  else R.drawable.baseline_circle_24
            binding.circleImage.setImageResource(loginImageResource)

            val onlineStatus = if (login) "온라인" else "오프라인"
            binding.online.text = onlineStatus


            itemView.setOnClickListener {
                itemClickListener.onItemClick(roomModel)
            }
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
    fun setList(list: List<ChatRoom>) {
        roomList = list
        notifyDataSetChanged()
    }
}