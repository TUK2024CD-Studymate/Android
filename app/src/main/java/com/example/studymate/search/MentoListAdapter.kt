package com.example.studymate.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.board.BoardListAdapter
import com.example.studymate.databinding.MentoListBinding


class MentoListAdapter(): RecyclerView.Adapter<MentoListAdapter.MyView>() {

    private var mentoList = listOf<GetMatchingModel>()

    inner class MyView(private val binding : MentoListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(pos : Int){
            binding.name.text = mentoList[pos].name
            binding.interests.text = mentoList[pos].interests
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
        return mentoList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GetMatchingModel>){
        mentoList = list
        notifyDataSetChanged()
    }


}