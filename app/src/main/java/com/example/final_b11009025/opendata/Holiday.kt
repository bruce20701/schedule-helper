package com.example.final_b11009025.opendata

import com.squareup.moshi.Json


data class Holiday (
    @Json(name = "date")
    val date:String,
    val chinese:String,
    val holidaycategory: String
)