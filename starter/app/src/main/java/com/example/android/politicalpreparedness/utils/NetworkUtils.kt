package com.example.android.politicalpreparedness.utils

import java.text.SimpleDateFormat
import java.util.*

object ParseDate {

    fun getToday(): String {
        val calendar =  Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getOneWeekAhead(): String {
        val calendar =  Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}

sealed class NetworkResult <out T: Any?>{
    data class Success<out T: Any?>(val data: T?) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}