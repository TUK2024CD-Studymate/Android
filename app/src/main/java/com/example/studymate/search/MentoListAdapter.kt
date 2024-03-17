package com.example.studymate.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.board.BoardInsideActivity
import com.example.studymate.databinding.MentoListBinding


class MentoListAdapter(): RecyclerView.Adapter<MentoListAdapter.MyView>() {

    private var mentoList = listOf<GetMatchingModel>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: Int, pos: Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class MyView(private val binding : MentoListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Int){
            binding.name.text = mentoList[item].name
            binding.interests.text = mapInterestsToKorean(mentoList[item].interests)

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
        return mentoList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GetMatchingModel>){
        mentoList = list
        notifyDataSetChanged()
    }

    private fun mapInterestsToKorean(interests: String): String {
        // 간단한 매핑 예시
        return when (interests) {
            "PROGRAMMING" -> "코딩"
            else -> interests
        }
    }

}
