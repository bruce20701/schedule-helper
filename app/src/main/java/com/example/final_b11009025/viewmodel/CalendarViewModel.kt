package com.example.final_b11009025.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_b11009025.database.CalendarEntity
import com.example.final_b11009025.database.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarViewModel:ViewModel() {
    var entityList: MutableLiveData<List<CalendarEntity>> = MutableLiveData<List<CalendarEntity>>()
    lateinit var db: RoomDB

    init {
        entityList.value = listOf()
    }

    fun get(): MutableLiveData<List<CalendarEntity>> {
        return entityList
    }

    fun set(input: CalendarEntity){
        entityList.value = entityList.value?.plus(input)
    }

    fun updateLiveData(date: String){
        GlobalScope.launch {
            entityList.postValue(db.getCalendarDao().getByDate(date))
        }
    }

    fun initDB(context: Context){
        db = RoomDB(context)
    }

}