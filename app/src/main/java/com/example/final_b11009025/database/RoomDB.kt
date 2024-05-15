package com.example.final_b11009025.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(TodolistEntity::class), (CalendarEntity::class)], version = 1)
abstract class RoomDB : RoomDatabase() {

    companion object {
        @Volatile private var instance: RoomDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            RoomDB::class.java, "item-list.db").build()
    }

    abstract fun getTodolistDao(): TodolistDao
    abstract fun getCalendarDao(): CalendarDao
}