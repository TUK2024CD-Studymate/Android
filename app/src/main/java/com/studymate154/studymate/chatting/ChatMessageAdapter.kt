package com.studymate154.studymate.chatting

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.studymate154.studymate.databinding.GetMessageItemBinding
import com.studymate154.studymate.databinding.SendMessageItemBinding


class ChatMessageAdapter(private val nickname: String) : RecyclerView.Adapter<ChatMessageAdapter.MyView>() {
    private var messageList = ArrayList<MessageModel>()

    fun addMessage(message: MessageModel) {
        messageList.add(message)
        notifyItemInserted(messageList.size-1)
        Log.d("ChatMessageAdapter", "Message added: $message")
    }

    inner class MyView(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            if (binding is SendMessageItemBinding) {
                // 내가 보낸 메시지 처리
                binding.message.text = messageList[pos].content
                Log.d("ChatMessageAdapter", "Bind Send Message at position $pos")

            } else if (binding is GetMessageItemBinding) {
                // 상대방이 보낸 메시지 처리
                binding.sender.text = messageList[pos].sender
                binding.message.text = messageList[pos].content
                Log.d("ChatMessageAdapter", "Bind Get Message at position $pos")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SEND_MESSAGE -> {
                val binding = SendMessageItemBinding.inflate(inflater, parent, false)
                MyView(binding)
            }
            VIEW_TYPE_GET_MESSAGE -> {
                val binding = GetMessageItemBinding.inflate(inflater, parent, false)
                MyView(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
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
            Log.d("ChatAdapter", "Send message type for position $position")
            VIEW_TYPE_SEND_MESSAGE
        } else {
            Log.d("ChatAdapter", "Get message type for position $position")
            VIEW_TYPE_GET_MESSAGE
        }
    }

    companion object {
        private const val VIEW_TYPE_SEND_MESSAGE = 1
        private const val VIEW_TYPE_GET_MESSAGE = 2
    }
}