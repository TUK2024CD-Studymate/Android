package com.example.studymate.chatting

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.studymate.databinding.GetMessageItemBinding
import com.example.studymate.databinding.SendMessageItemBinding


class ChatMessageAdapter(private val nickname: String) : RecyclerView.Adapter<ChatMessageAdapter.MyView>() {
    private var messageList = ArrayList<MessageModel>()

    fun addMessage(message: MessageModel) {
        messageList.add(message)
        Handler(Looper.getMainLooper()).postDelayed({
            notifyDataSetChanged()
        }, 100)
    }

    inner class MyView(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            if (binding is SendMessageItemBinding) {
                // 내가 보낸 메시지 처리
                binding.message.text = messageList[pos].message
            } else if (binding is GetMessageItemBinding) {
                // 상대방이 보낸 메시지 처리
                binding.sender.text = messageList[pos].sender
                binding.message.text = messageList[pos].message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if (viewType == VIEW_TYPE_SEND_MESSAGE) {
            SendMessageItemBinding.inflate(inflater, parent, false)
        } else {
            GetMessageItemBinding.inflate(inflater, parent, false)
        }
        return MyView(binding)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messageModel = messageList[position]
        return if (messageModel.sender == nickname) {
            VIEW_TYPE_SEND_MESSAGE
        } else {
            VIEW_TYPE_GET_MESSAGE
        }
    }

    companion object {
        private const val VIEW_TYPE_SEND_MESSAGE = 1
        private const val VIEW_TYPE_GET_MESSAGE = 2
    }
}
