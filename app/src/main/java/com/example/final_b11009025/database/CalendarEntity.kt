package com.example.final_b11009025.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.final_b11009025.database.CalendarEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class CalendarEntity(date:String, time:String, text:String, type: String){

    companion object {
        const val TABLE_NAME = "calendar_entity"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo
    var date: String = date

    @ColumnInfo
    var time: String = time

    @ColumnInfo
    var text: String = text

    @ColumnInfo
    var type: String = type

}
