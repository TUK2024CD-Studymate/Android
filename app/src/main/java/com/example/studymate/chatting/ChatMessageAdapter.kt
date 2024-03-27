package com.example.studymate.chatting

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.board.GetBoardModel
import com.example.studymate.databinding.CommentItemBinding
import com.example.studymate.databinding.MessageItemBinding

class ChatMessageAdapter() :  RecyclerView.Adapter<ChatMessageAdapter.MyView>() {
    private var messageList = ArrayList<MessageModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(message: MessageModel) {
        messageList.add(message)
        Handler(Looper.getMainLooper()).postDelayed({
            notifyDataSetChanged()
        }, 100)
    }

    inner class MyView(private val binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(pos: Int){
            binding.sender.text = messageList[pos].sender
            binding.message.text = messageList[pos].message
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageAdapter.MyView {
        val view = MessageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: ChatMessageAdapter.MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}