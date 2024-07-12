package com.studymate154.studymate.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.studymate154.studymate.Model.GetMatchingModel
import com.studymate154.studymate.databinding.MentoListBinding


class MentoListAdapter(): RecyclerView.Adapter<MentoListAdapter.MyView>() {

    private var mentoList = listOf<GetMatchingModel>()

    interface OnItemClickListener {
        fun onInfoClick(item: GetMatchingModel)
        fun onNameClick(item: GetMatchingModel)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class MyView(private val binding : MentoListBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: GetMatchingModel){
            binding.name.text = item.name

            Glide.with(binding.root)
                .load(item.imageUrl)
                .into(binding.mentoImg)

            // 이미지 클릭 이벤트 설정
            binding.mentoInfo.setOnClickListener {
                listener?.onInfoClick(item)
            }

            // 이름 클릭 이벤트 설정
            binding.mentoAlert.setOnClickListener {
                listener?.onNameClick(item)
            }

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentoListAdapter.MyView {
        val view = MentoListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MentoListAdapter.MyView, position: Int) {
        holder.bind(mentoList[position])
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
