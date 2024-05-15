package com.example.final_b11009025.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.final_b11009025.database.TodolistEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class TodolistEntity(text:String){

    companion object {
        const val TABLE_NAME = "todolist_entity"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo
    var text: String = text

}