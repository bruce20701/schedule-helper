package com.example.final_b11009025.database

import androidx.room.*

@Dao
interface TodolistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: TodolistEntity): Long

    @Insert
    fun insertAll(item: TodolistEntity)

    @Query("SELECT * FROM todolist_entity")
    fun getAll(): List<TodolistEntity>

    @Delete
    fun delete(item: TodolistEntity)

    @Update
    fun update(item: TodolistEntity)

    fun count():Int{
        return getAll().size
    }

}