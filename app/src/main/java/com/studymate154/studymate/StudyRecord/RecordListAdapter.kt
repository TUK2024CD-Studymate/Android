package com.studymate154.studymate.StudyRecord

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.studymate154.studymate.databinding.RecordListBinding

class RecordListAdapter(): RecyclerView.Adapter<RecordListAdapter.MyView>() {

    private var recordList = listOf<StudyModel>()

    inner class MyView(private val binding: RecordListBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(pos: StudyModel) {
            binding.name.text = pos.subjectName
            binding.recordTime.text = pos.entireTime


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListAdapter.MyView {
        val view = RecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecordListAdapter.MyView, position: Int) {
        holder.bind(recordList[position])
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<StudyModel>) {
        recordList = list
        notifyDataSetChanged()
    }


}