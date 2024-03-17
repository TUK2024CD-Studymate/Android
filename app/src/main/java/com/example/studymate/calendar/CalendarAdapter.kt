package com.example.studymate.calendar

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.R
import com.example.studymate.databinding.ItemCalendarListBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarAdapter(private val cList: List<CalendarVO>, private val dateClickListener: (String) -> Unit) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: String? = LocalDate.now().format(DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko")))
    class CalendarViewHolder(private val binding: ItemCalendarListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: CalendarVO, selectedDate: String?) {
            binding.date.text = item.cl_date
            binding.day.text = item.cl_day

            if (item.cl_date == selectedDate) {
                binding.weekCardView.setBackgroundResource(R.drawable.background_blue)
            } else {
                binding.weekCardView.setBackgroundResource(R.color.white)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = cList[position]

        holder.itemView.setOnClickListener {
            dateClickListener.invoke(item.cl_date)

            // 클릭한 날짜를 저장하고 어댑터 갱신
            selectedDate = item.cl_date
            notifyDataSetChanged()
        }
        // 선택한 날짜 정보를 bind 메서드에 전달
        holder.bind(item, selectedDate)
    }

    override fun getItemCount(): Int {
        return cList.size
    }
}