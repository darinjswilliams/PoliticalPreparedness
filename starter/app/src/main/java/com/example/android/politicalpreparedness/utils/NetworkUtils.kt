package com.example.android.politicalpreparedness.utils

import com.example.android.politicalpreparedness.network.jsonadapter.CustomDateAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object ParseDate {

    fun getToday(): String {
        val calendar =  Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getDateFromJson(str: String): Date? {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.parse(str)
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