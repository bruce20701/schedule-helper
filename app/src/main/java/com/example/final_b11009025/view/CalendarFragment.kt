package com.example.final_b11009025.view

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_b11009025.R
import com.example.final_b11009025.adapter.CalendarAdapter
import com.example.final_b11009025.database.CalendarEntity
import com.example.final_b11009025.database.RoomDB
import com.example.final_b11009025.databinding.FragmentCalendarBinding
import com.example.final_b11009025.opendata.HolidayService
import com.example.final_b11009025.viewmodel.CalendarViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


class CalendarFragment: Fragment() {

    //viewBinding
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    //viewModel
    private val model: CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //獲取今天日期
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONDAY)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        //存放calendarView點擊日期(預設為今日)
        var selectedDate = "$year/${month + 1}/$day"

        //建立DB
        var db = RoomDB(view.context)

        //設定viewModel裡的DB
        model.initDB(view.context)

        //利用SharedPreferences儲存已載入的opendata資料年分
        val pref = view.context.getSharedPreferences("check", Context.MODE_PRIVATE)
        val editor = pref.edit()
        val saveYear : String? = pref.getString("saveYear", "no data")

        //檢查今年份的opendata是否已存入資料庫
        if (!saveYear.equals(year.toString())){
            //接opendata
            val retrofit = Retrofit.Builder()
                .baseUrl("https://data.ntpc.gov.tw/")
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder().add(
                            KotlinJsonAdapterFactory()
                        ).build()
                    )
                )
                .build()

            val holidayService = retrofit.create(HolidayService::class.java)
            //儲存假日&補班opendata進資料庫
            CoroutineScope(Dispatchers.IO).launch {

                //獲取opendata
                val holidays = holidayService.getHoliday()

                //過濾出今年的假日&補班
                for (holiday in holidays) {
                    if(holiday.date.substring(0,4).equals(year.toString())){

                        //資料分類
                        val text: String
                        val type: String

                        if (holiday.holidaycategory.equals("補行上班日")){
                            text = holiday.holidaycategory
                            type = "補班"
                        }
                        else {

                            if (holiday.chinese.equals("")){
                                if (holiday.holidaycategory.equals("星期六、星期日"))
                                    text = "周休二日"
                                else
                                    text = holiday.holidaycategory
                            }
                            else
                                text = holiday.chinese
                            type = "放假"
                        }

                        //建立實體並存入資料庫
                        val insertItem = CalendarEntity(holiday.date,"全天",text, type)
                        db.getCalendarDao().insert(insertItem)

                        //儲存SharedPreferences
                        editor.putString("saveYear", year.toString()).apply()
                    }
                }

                //livedata更新資料
                model.updateLiveData(selectedDate)
            }
        }

        //初始化viewModel
        model.updateLiveData(selectedDate)

        //recyclerView連接adapter
        val adapter = CalendarAdapter(listOf())
        binding.eventList.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        binding.eventList.adapter = adapter

        //觀察livedata資料有無變動
        model.get().observe(this,{
            adapter.DataSetUpdate(it)
        })

        //calendarView點擊監聽事件
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year/${month + 1}/$dayOfMonth"
            model.updateLiveData(selectedDate)
        }

        //新增活動按鈕監聽事件
        binding.insertCalendar.setOnClickListener {

            //建立insert todolist dialog
            val dialog = Dialog(view.context)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_insert_calendar)
            val timePicker: TimePicker = dialog.findViewById(R.id.timePicker)
            val insertText: EditText = dialog.findViewById(R.id.CIText)
            val insertType: EditText = dialog.findViewById(R.id.CIType)
            val confirm: TextView = dialog.findViewById(R.id.CIConfirm)
            val cancel: TextView = dialog.findViewById(R.id.CICancel)

            //確認按鈕監聽事件
            confirm.setOnClickListener {

                val insertItem = CalendarEntity(selectedDate,String.format("%02d:%02d",timePicker.hour,timePicker.minute), insertText.text.toString(), insertType.text.toString())

                GlobalScope.launch {

                    //資料庫新增資料
                    db.getCalendarDao().insert(insertItem)

                    //livedata更新資料
                    model.updateLiveData(selectedDate)

                }

                //dialog關閉
                dialog.dismiss()
            }

            //取消按鈕監聽事件
            cancel.setOnClickListener {

                //dialog關閉
                dialog.dismiss()

            }

            //dialog顯示
            dialog.show()

        }

        //建立item點擊監聽事件
        adapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, item: CalendarEntity) {

                //opendata不可被刪除
                if (!(item.type.equals("放假") or item.type.equals("補班"))){

                    //建立delete todolist dialog
                    val dialog = Dialog(view.context)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_delete_item)
                    val deleteText: TextView = dialog.findViewById(R.id.deleteText)
                    val yes: TextView = dialog.findViewById(R.id.yes)
                    val no: TextView = dialog.findViewById(R.id.no)

                    //設置deleteText文字
                    deleteText.text = item.text

                    //yes按鈕監聽事件
                    yes.setOnClickListener {

                        GlobalScope.launch {

                            //資料庫刪除資料
                            db.getCalendarDao().delete(item)

                            //livedata更新資料
                            model.updateLiveData(selectedDate)

                        }

                        //livedata刪除資料
                        model.entityList.value = model.entityList.value!! - item

                        //dialog關閉
                        dialog.dismiss()
                    }

                    //no按鈕監聽事件
                    no.setOnClickListener {

                        //dialog關閉
                        dialog.dismiss()

                    }

                    //dialog顯示
                    dialog.show()

                }

            }
        })

    }

}