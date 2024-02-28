package com.example.studymate.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.databinding.MentoListBinding


class MentoListAdapter(): RecyclerView.Adapter<MentoListAdapter.MyView>() {

    private var mentoList = listOf<GetMatchingModel>()
    private var dummyList = listOf<MentoModel>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: Int, pos: Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class MyView(private val binding : MentoListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Int){
            binding.name.text = dummyList[item].name
            binding.interests.text = dummyList[item].interests

            binding.mentoImg.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(binding.mentoImg, item, pos)
                }
            }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentoListAdapter.MyView {
        val view = MentoListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MentoListAdapter.MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dummyList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<MentoModel>){
        dummyList = list
        notifyDataSetChanged()
    }


}