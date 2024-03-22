package com.example.studymate.HomeFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Chronometer
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studymate.signUp.LoginApi.Companion.gson
import com.example.studymate.R
import com.example.studymate.StudyRecord.*
import com.example.studymate.calendar.CalendarVO
import com.example.studymate.calendar.CalendarAdapter
import com.example.studymate.databinding.FragmentRecordBinding
import com.example.studymate.signUp.SignUpResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.ArrayList


class RecordFragment : Fragment() {
    lateinit var binding: FragmentRecordBinding
    lateinit var calendarAdapter: CalendarAdapter
    private var calendarList = ArrayList<CalendarVO>()
    private var running : Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var boardId : String
    private lateinit var newRecordId : String
    var recordList = listOf<StudyModel>()
    var pauseTime = 0L
    var studyList = listOf<StudyModel>()
    val studyData = StudyModel(null,null,null,null)

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

       val listAdapter = RecordListAdapter()

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")


        //시작버튼
        // 시작버튼
        binding.startBtn.setOnClickListener {
            if (!running) {
                binding.chronometer.base = SystemClock.elapsedRealtime() - pauseTime
                binding.chronometer.start()
                running = true
                val startTime = Instant.now()
                val zonedDateTime = startTime.atZone(ZoneId.systemDefault())
                studyData.startTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault()))
                updateStudyData(studyData)
            }
        }

// 멈춤버튼
        binding.stopBtn.setOnClickListener {
            if (running) {
                binding.chronometer.stop()
                pauseTime = SystemClock.elapsedRealtime() - binding.chronometer.base
                running = false
                val stopTime = Instant.now()
                val zonedDateTime = stopTime.atZone(ZoneId.systemDefault())
                studyData.endTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault()))
                updateStudyData(studyData)
            }
        }




        val items = resources.getStringArray(R.array.interests_array)
        val myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = myAdapter

        //스피너 과목 선택
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {

                    }
                    1 -> {
                        studyData.studyClass = "MATH"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    2 -> {
                        studyData.studyClass = "CODING"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    3 -> {
                        studyData.studyClass = "KOREAN"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    4 -> {
                        studyData.studyClass = "ENGLISH"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    5 -> {
                        studyData.studyClass = "SCIENCE"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    else -> {
                        studyData.studyClass = "SOCIETY"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        //@post
        binding.saveBtn.setOnClickListener {
            studyData.content = binding.editMemo.text.toString()
            updateStudyData(studyData)
            Log.d("park", userToken.toString())
            binding.editMemo.text = null
            val retrofitWork = RecordRetrofitWork(userToken.toString(),studyData)
            retrofitWork.work{ recordId ->
                newRecordId = recordId

//                //@get
//                getList(newRecordId)

                //@delete
                binding.deleteBtn.setOnClickListener {
                    deleteRecord(newRecordId)

                }
            }

        }




        //리싸이클러뷰에 어뎁터 적용
        binding.recyclerView.apply {
            listAdapter.setList(studyList)
            listAdapter.notifyDataSetChanged()
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weekDay: Array<String> = resources.getStringArray(R.array.calendar_day)
        //켈린더 날짜 클릭
        calendarAdapter = CalendarAdapter(calendarList){ clickedDate ->
            val startTime = "2024-03-$clickedDate"
            Log.d("ClickedDate", "Clicked date: $startTime")
            getListForDate(startTime)
        }

        calendarList.apply {
            val dateFormat = DateTimeFormatter.ofPattern("dd").withLocale(Locale.forLanguageTag("ko"))
            val monthFormat = DateTimeFormatter.ofPattern("yyyy년 MM월").withLocale(Locale.forLanguageTag("ko"))

            val localDate = LocalDateTime.now().format(monthFormat)
            Log.d("localDate",localDate)
            binding.textYearMonth.text = localDate

            var preSunday: LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.previous(
                DayOfWeek.SUNDAY))

            for (i in 0..6) {
                Log.d("날짜만", weekDay[i])

                calendarList.apply {
                    add(CalendarVO(preSunday.plusDays(i.toLong()).format(dateFormat), weekDay[i]))
                }
                Log.d("저번 주 일요일 기준으로 시작!", preSunday.plusDays(i.toLong()).format(dateFormat))
            }
        }
        binding.weekRecycler.adapter = calendarAdapter
        binding.weekRecycler.layoutManager = GridLayoutManager(context, 7)
    }

    private fun updateStudyData(studyData: StudyModel) {
        val json = gson.toJson(studyData)
        Log.d("studymodel", json)
    }

    private fun deleteRecord(calenderId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = StudyRetrofitAPI.emgMedService.deleteRecordByEnqueue("Bearer $userToken", calenderId)

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("deleteRecord", "Record deleted successfully")
                } else {
                    Log.e("deleteRecord", "Failed to delete record. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                // 실패 처리를 원하면 여기에 코드를 추가할 수 있습니다.
                Log.e("deleteRecord", "Network request failed", t)
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getListForDate(startTime: String) {
        // 클릭한 날짜에 해당하는 기록 가져오기 (예시)
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val listAdapter = RecordListAdapter()

        // 서버에 클릭한 날짜에 해당하는 기록을 요청
        val call = StudyRetrofitAPI.emgMedService.getRecordListByEnqueue("Bearer $userToken", startTime)

        call.enqueue(object : Callback<GetRecordResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<GetRecordResponse>, response: Response<GetRecordResponse>) {
                if (response.isSuccessful) {
                    val studyListForDate: GetRecordResponse? = response.body()

                    Log.e("studyListForDate", studyListForDate.toString())
                    if (studyListForDate != null) {
                        val modifiedList = studyListForDate.calenderList.map { record ->
                            // 공백을 기준으로 문자열을 분할하여 날짜 부분만 추출
                            val date = record.startTime!!.split(" ")[0]
                            // 추출한 날짜를 사용하여 새로운 StudyModel 객체를 생성
                            StudyModel(record.id, record.content, record.studyClass, date, record.endTime, record.entireTime)
                        }
                        Log.e("modifiedList", modifiedList.toString())
                        // 필터링된 리스트를 가져오는 부분은 그대로 사용합니다.
                        val filterList = modifiedList.filter { it.startTime == startTime }
                        Log.e("filterList", filterList.toString())
                        recordList = filterList
                        if (recordList.isNotEmpty()) {
                            binding.totalTime.text = recordList[0].entireTime.toString()
                        } else {
                            // recordList가 비어 있다면 처리할 로직 추가
                            Log.e("getListForDate", "Record list is empty.")
                        }
                        listAdapter.setList(recordList)
                        binding.recyclerView.adapter = listAdapter
                        listAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("getListForDate", "Failed to convert response to StudyModel list")
                    }
                }
            }

            override fun onFailure(call: Call<GetRecordResponse>, t: Throwable) {
                Log.e("getListForDate", "Network request failed", t)
            }
        })
    }



}






