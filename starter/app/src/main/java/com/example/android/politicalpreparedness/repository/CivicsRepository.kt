package com.example.android.politicalpreparedness.repository

import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.*
import com.example.android.politicalpreparedness.network.*
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElectionInfo
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import timber.log.Timber

class CivicsRepository(private val database: ElectionDatabase) {

    lateinit var informationForVoters: VoterInfo

    val elections: LiveData<List<Election>> = Transformations.map(
        database.electionDao.getElection()
    ) {
        it?.asElectionDomainModel()
    }

    val followedElectionList: LiveData<List<Election>> = Transformations.map(
        database.electionDao.getAllFollowedElection()
    ) {
        it?.asElectionDomainModel()
    }


    suspend fun refreshInformation() {
        withContext(Dispatchers.IO) {
            refreshElections()
        }
    }

    suspend fun getVoterInformation(electionId: Int, division: Division) {
        withContext(Dispatchers.IO) {
            refreshVoterInformation(electionId, division)
        }
    }

    private suspend fun refreshVoterInformation(electionId: Int, division: Division) {
       Timber.d("CivicsRespository: VoterInformation")

        try {
            val stateCounty = "${division.country},${division.state}"

            //TODO check to if the network is up it down, than call database

            val voterInfoResult =
                CivicsApi.retrofitService.getVoterInfo(stateCounty, electionId.toLong())

            val electionInfo =
                database.electionDao.getElectionById(electionId)?.asSingleElectionDomainModel()


            //Insert data into local database for offline cache
            val voterInfoList = voterInfoResult.state?.map {
                NetworkVoterInfo(
                    electionId,
                    electionInfo!!.name,
                    electionInfo!!.electionDay,
                    it.electionAdministrationBody.votingLocationFinderUrl!!,
                    it.electionAdministrationBody.ballotInfoUrl!!
                )
            }

            database.electionDao.deleteVoterInformation()

            //insert minimum voter information into database
            voterInfoList?.asVoterInfoDatabaseModel()?.let { it ->
                database.electionDao.insertVoterInfo(
                    *it.toTypedArray()
                )

                val voterInformation =
                    database.electionDao.getVoterInformation()?.asVoterInfoDomainModel()

                if (voterInformation != null) {
                    informationForVoters = voterInformation
                }

            }
        } catch (e: Exception) {
           Timber.d("Exception Voter Information Results ${e.localizedMessage}")
        }
    }

    private suspend fun refreshElections() {
       Timber.d("CivicsRepository")


        try {

            val electionResults = CivicsApi.retrofitService.getElections()
           Timber.d("Elections Results:%s", electionResults)


            val electionList = electionResults.elections.map {
                NetworkElection(
                    it.id,
                    it.name,
                    it.electionDay,
                    it.division
                )
            }

            database.electionDao.insertElections(
                *electionList.asElectionDatabaseModel().toTypedArray()
            )

        } catch (e: Exception) {
           Timber.d("Elections Results:.." + e.localizedMessage)
        }

    }


    suspend fun deleteFollowedElection(voterInfo: VoterInfo) {
        withContext(Dispatchers.IO) {
            database.electionDao.deletedFollowed(voterInfo.id)
            checkForFollowedElection(voterInfo.id)

        }
    }

    suspend fun insertfollowElection(voterInfo: VoterInfo) {
        withContext(Dispatchers.IO) {

            database.electionDao.insertFollowElection(voterInfo.asVoterInfoToFollowedElectionDatabaseModal())
            checkForFollowedElection(voterInfo.id)

        }
    }

    suspend fun checkForFollowedElection(id: Int): Result<FollowedElectionEntity> = withContext(Dispatchers.IO) {

        return@withContext try {
            val result = database.electionDao.getFollowedElection(id)
            if(result != null){
               Timber.d("CheckForFollowedElection Results: True: ${result.id}")
                return@withContext Result.Success(result)
            } else {
               Timber.d("CheckForFollowedElection Results: False: ${result?.id}")
                return@withContext Result.Error("Election Information Not Found!")
            }

        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }

    }

}