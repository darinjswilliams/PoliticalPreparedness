package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.asElectionDomainModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.NetworkElection
import com.example.android.politicalpreparedness.network.asDatabaseModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception

class CivicsRepository(private val database: ElectionDatabase) {

    val elections: LiveData<List<Election>> = Transformations.map(
        database.electionDao.getElection()
    ) {
        it?.asElectionDomainModel()
    }


    suspend fun refreshInformation() {
        withContext(Dispatchers.IO) {
            refreshElections()
        }
    }

    suspend fun getVoterInformation(electionId: Int, division: Division){
        withContext(Dispatchers.IO) {
            refreshVoterInformation(electionId, division)
        }
    }

    private suspend fun refreshVoterInformation(electionId: Int, division: Division) {
       Timber.i("CivicsRespository: VoterInformation")

        //format the state and country
        try {
            val stateCounty = "${division.country},${division.state}"
            Timber.i("State and Country Information: $stateCounty ElectionId: $electionId")

            val voterInfoResult = CivicsApi.retrofitService.getVoterInfo(stateCounty, electionId.toLong())

            //Insert data into local database for offline cache
            Timber.i("Voter Information Results $voterInfoResult")

        } catch (e: Exception) {
            Timber.i("Exception Voter Information Results ${e.localizedMessage}")
        }
    }

    private suspend fun refreshElections() {
        Timber.i("CivicsRepository")


        try {
            val electionResults = CivicsApi.retrofitService.getElections()
            Timber.i("Elections Results:%s", electionResults)

            val electionList = electionResults.elections.map {
                NetworkElection(
                    it.id,
                    it.name,
                    it.electionDay,
                    it.division
                )
            }
            //TODO INSERT INTO Database
            database.electionDao.insertElections(
                *electionList.asDatabaseModel().toTypedArray()
            )

        } catch (e: Exception) {
            Timber.i("Elections Results:.." + e.localizedMessage)
        }

    }


}