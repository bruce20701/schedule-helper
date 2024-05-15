package com.example.final_b11009025.opendata

import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayService {
    @GET("https://data.ntpc.gov.tw/api/datasets/308dcd75-6434-45bc-a95f-584da4fed251/json?page=4&size=298")
    suspend fun getHoliday(): List<Holiday>
}