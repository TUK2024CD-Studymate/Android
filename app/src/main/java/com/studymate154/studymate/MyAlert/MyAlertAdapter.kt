package com.studymate154.studymate.MyAlert

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.studymate154.studymate.Model.GetMyAlertModel
import com.studymate154.studymate.databinding.AlertItemBinding

class MyAlertAdapter(): RecyclerView.Adapter<MyAlertAdapter.MyView>() {

    private var alertList = listOf<GetMyAlertModel>()

    inner class MyView(private val binding : AlertItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: GetMyAlertModel){
            binding.dateText.text = item.createdAt
            binding.contentText.text = item.content

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view = AlertItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MyAlertAdapter.MyView, position: Int) {
        holder.bind(alertList[position])
    }

    override fun getItemCount(): Int {
        return alertList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<GetMyAlertModel>){
        alertList = list
        notifyDataSetChanged()
    }
}