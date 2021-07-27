package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepresentativeRepository(private val database: ElectionDatabase) {


    suspend fun getRepresentative(address: String){
        withContext(Dispatchers.IO){
            searchForRepresentative(address)
        }
    }

    private suspend fun searchForRepresentative(address: String) {

        Timber.i("GET REPRESENTATIVE INFORMATION")
    }
}