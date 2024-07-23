package com.studymate154.studymate.chatting.ChattingAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.studymate154.studymate.R
import com.studymate154.studymate.chatting.ChatRoom
import com.studymate154.studymate.databinding.RoomItemListBinding

class ChatRoomAdapter(private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<ChatRoomAdapter.MyView>() {

    private var roomList = listOf<ChatRoom>()

    interface OnItemClickListener {
        fun onItemClick(roomModel: ChatRoom)
    }

    inner class MyView(private val binding: RoomItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomModel: ChatRoom) {
            val memberNames = roomModel.members.filter { it.name != "박환" }.joinToString(", ") { it.name }
            binding.name1.text = memberNames
            binding.readCount.text = roomModel.unreadMessageCount.toString()

            val profileImageUrl = roomModel.members.firstOrNull()?.profileImageUrl
            profileImageUrl?.let {
                Glide.with(binding.root)
                    .load(it)
                    .into(binding.mentorImg)  // 프로필 이미지를 표시할 ImageView 지정
            }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view = RoomItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
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