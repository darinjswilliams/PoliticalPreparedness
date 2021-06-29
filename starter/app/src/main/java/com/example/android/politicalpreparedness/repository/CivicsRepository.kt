package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import timber.log.Timber
import java.lang.Exception

class CivicsRepository(private val database: ElectionDatabase) {

    suspend fun refreshElections() {
        try {
            Timber.i("CivicsRepository")
            val electionResults = CivicsApi.retrofitService.getElections()
            Timber.i("Elections Results:%s", electionResults)


        } catch (ex: Exception) {

        }
    }
}