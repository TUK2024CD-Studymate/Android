package com.example.studymate.HomeFragment

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    var pauseTime = 0L
    var studyList = listOf<StudyModel>()

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

       val listAdapter = RecordListAdapter()


        val studyData = StudyModel(null,null,null,null)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken", "")


        //시작버튼
        binding.startBtn.setOnClickListener {
            if (!running) {
                binding.chronometer.base = SystemClock.elapsedRealtime() - pauseTime
                binding.chronometer.start()
                running = true
                val startTime = Instant.now()
                val zonedDateTime = startTime.atZone(ZoneId.systemDefault())
                studyData.startTime = zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                updateStudyData(studyData)
            }
        }

        //멈춤버튼
        binding.stopBtn.setOnClickListener {
            if (running) {
                binding.chronometer.stop()
                pauseTime = SystemClock.elapsedRealtime() - binding.chronometer.base
                running = false
                val stopTime = Instant.now()
                val zonedDateTime = stopTime.atZone(ZoneId.systemDefault())
                studyData.endTime = zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                updateStudyData(studyData)
            }
        }



        val items = resources.getStringArray(R.array.interests_array)
        val myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = myAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        studyData.studyClass = "MATH"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    1 -> {
                        studyData.studyClass = "CODING"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    2 -> {
                        studyData.studyClass = "KOREAN"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    3 -> {
                        studyData.studyClass = "ENGLISH"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    4 -> {
                        studyData.studyClass = "SCIENCE"
                        updateStudyData(studyData)
                        Log.d("studymodel", updateStudyData(studyData).toString())
                    }
                    // ...
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
            val retrofitWork = RecordRetrofitWork(userToken.toString(),studyData)
            retrofitWork.work()

        }

        //@delete
        binding.deleteBtn.setOnClickListener {
            deleteRecord("1")
            listAdapter.notifyDataSetChanged()
        }


        //리싸이클러뷰에 어뎁터 적용
        binding.recyclerView.apply {
            listAdapter.setList(studyList)
            listAdapter.notifyDataSetChanged()
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        //@get
        initList("1")

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weekDay: Array<String> = resources.getStringArray(R.array.calendar_day)

        calendarAdapter = CalendarAdapter(calendarList)

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

    private fun initList(calenderId: String) {
        val userToken = sharedPreferences.getString("userToken", "") ?: ""
        val call = StudyRetrofitAPI.emgMedService.getRecordByEnqueue("Bearer $userToken", calenderId)
        val listAdapter = RecordListAdapter()

        call.enqueue(object : Callback<StudyModel> {
            override fun onResponse(call: Call<StudyModel>, response: Response<StudyModel>) {
                if (response.isSuccessful) {
                    // 서버 응답을 StudyModel 객체로 변환
                    val studyModel: StudyModel? = response.body()

                    if (studyModel != null) {
                        // 성공적으로 변환되었다면 원하는 작업을 수행
                        studyList = listOf(studyModel)
                        listAdapter.setList(studyList)

                        // UI 작업을 UI 스레드에서 수행
                        activity?.runOnUiThread {
                            binding.recyclerView.adapter = listAdapter // RecyclerView에 어댑터 설정
                        }
                    } else {
                        Log.e("initList", "Failed to convert response to StudyModel")
                    }
                }
            }

            override fun onFailure(call: Call<StudyModel>, t: Throwable) {
                // 실패 처리를 원하면 여기에 코드를 추가할 수 있습니다.
                Log.e("initList", "Network request failed", t)
            }
        })
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






}