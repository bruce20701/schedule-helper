package com.example.final_b11009025.database

import androidx.room.*

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: CalendarEntity): Long

    @Insert
    fun insertAll(item: CalendarEntity)

    @Query("SELECT * FROM calendar_entity")
    fun getAll(): List<CalendarEntity>

    @Query("SELECT * FROM calendar_entity WHERE date = (:QueryDate)")
    fun getByDate(QueryDate: String): List<CalendarEntity>

    @Delete
    fun delete(item: CalendarEntity)

    @Update
    fun update(item: CalendarEntity)

    fun count():Int{
        return getAll().size
    }
}
