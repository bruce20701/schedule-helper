package com.example.final_b11009025.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.final_b11009025.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TodoListViewModel:ViewModel() {

    var entityList:MutableLiveData<List<TodolistEntity>> = MutableLiveData<List<TodolistEntity>>()

    lateinit var db:RoomDB

    init {
        entityList.value = listOf()
    }

    fun get(): MutableLiveData<List<TodolistEntity>>{
        return entityList
    }

    fun set(input:TodolistEntity){
        entityList.value = entityList.value?.plus(input)
    }

    fun updateLiveData(){
        GlobalScope.launch {
            entityList.postValue(db.getTodolistDao().getAll())
        }
    }

    fun initDB(context: Context){
        db = RoomDB(context)
    }
}
