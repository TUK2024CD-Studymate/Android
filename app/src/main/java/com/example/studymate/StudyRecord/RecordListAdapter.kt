package com.example.studymate.StudyRecord

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.databinding.RecordListBinding

class RecordListAdapter(): RecyclerView.Adapter<RecordListAdapter.MyView>() {

    private var recordList = listOf<StudyModel>()

    inner class MyView(private val binding: RecordListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.text01.text = recordList[pos].studyClass
            binding.text02.text = recordList[pos].content
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListAdapter.MyView {
        val view = RecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: RecordListAdapter.MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    fun setList(list: List<StudyModel>) {
        recordList = list
    }
}