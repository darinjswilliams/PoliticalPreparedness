package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class CivicsRepository(private val database: ElectionDatabase) {



    suspend fun refreshInformation(){
        withContext(Dispatchers.IO){
            refreshElections()
        }


    }

    private suspend fun refreshElections()  {
        Timber.i("CivicsRepository")


        try {
          val electionResults = CivicsApi.retrofitService.getElections()
            Timber.i("Elections Results:%s", electionResults)

            //TODO INSERT INTO Database

        } catch (e: Exception) {
            Timber.i("Elections Results:.." + e.localizedMessage)
        }

    }


}