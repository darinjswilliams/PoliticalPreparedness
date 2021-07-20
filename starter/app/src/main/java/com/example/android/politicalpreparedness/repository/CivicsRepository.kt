package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.*
import com.example.android.politicalpreparedness.network.*
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElectionInfo
import com.example.android.politicalpreparedness.network.models.VoterInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import timber.log.Timber
import java.lang.Exception

class CivicsRepository(private val database: ElectionDatabase) {

    lateinit var electionInfoForVoters: Election
    lateinit var informationForVoters: VoterInfo
    var existingFollowedExisting: Boolean = false

    val elections: LiveData<List<Election>> = Transformations.map(
        database.electionDao.getElection()
    ) {
        it?.asElectionDomainModel()
    }


    //    val voterInformation: LiveData<VoterInfo> = Transformations.map(
//        database.electionDao.getVoterInformation()
//    ){
//        it?.asVoterInfoDomainModel()
//    }


    suspend fun refreshInformation() {
        withContext(Dispatchers.IO) {
            refreshElections()
        }
    }

    suspend fun getVoterInformation(electionId: Int, division: Division) {
        withContext(Dispatchers.IO) {
            refreshVoterInformation(electionId, division)
            getVoterElectionInfo(electionId)
        }
    }

    private suspend fun refreshVoterInformation(electionId: Int, division: Division) {
        Timber.i("CivicsRespository: VoterInformation")

        //format the state and country
        try {
            val stateCounty = "${division.country},${division.state}"
            Timber.i("State and Country Information: $stateCounty ElectionId: $electionId")

            val voterInfoResult =
                CivicsApi.retrofitService.getVoterInfo(stateCounty, electionId.toLong())

            val electionInfo =
                database.electionDao.getElectionById(electionId)?.asSingleElectionDomainModel()


            //Insert data into local database for offline cache
            Timber.i("Voter Information Results ${voterInfoResult.state}")
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
            voterInfoList?.asVoterInfoDatabaseModel()?.let {
                database.electionDao.insertVoterInfo(
                    *it.toTypedArray()
                )


                val voterInformation =
                    database.electionDao.getVoterInformation()?.asVoterInfoDomainModel()

                if (voterInformation != null) {
                    informationForVoters = voterInformation
                }

                //Check for Followed Election
                val followedElection =
                    voterInformation?.let { followed ->
                        database.electionDao.getFollowedElection(followed.id)
                            ?.asSingleFollowedElectionDomainModel()
                    }

                existingFollowedExisting = when (followedElection) {
                    null -> true
                    else -> false
                }

//                if (followedElection != null) {
//                    Timber.i("EXISTING: STATE IS: STATE IS : True : ${followedElection.id}")
//                    existingFollowedExisting = true
//                } else {
//                    Timber.i("DOES NOT EXIST: STATE IS FALSE: SET TO UNFOLLOWED: ${followedElection?.id}")
//                    existingFollowedExisting = false
//                }


            }
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

            database.electionDao.insertElections(
                *electionList.asElectionDatabaseModel().toTypedArray()
            )

        } catch (e: Exception) {
            Timber.i("Elections Results:.." + e.localizedMessage)
        }

    }

    private suspend fun getVoterElectionInfo(electionId: Int) {
        Timber.i("ElectionId: $electionId")

        val electionInfo =
            database.electionDao.getElectionById(electionId)?.asSingleElectionDomainModel()

        Timber.i("Election Values ${electionInfo?.name}")

        if (electionInfo != null) {
            electionInfoForVoters = electionInfo
        }

    }

    suspend fun trackFollowedElection(voterInfo: VoterInfo) {

        withContext(Dispatchers.IO) {


            val voterInformation =
                database.electionDao.getVoterInformation()?.asVoterInfoDomainModel()

            val followedElection =
                voterInformation?.let { followed ->
                    database.electionDao.getFollowedElection(followed.id)
                        ?.asSingleFollowedElectionDomainModel()
                }


            existingFollowedExisting = when (followedElection) {
                null -> {
                    Timber.i("DOES NOT EXIST: INSERTING Followed Election:  $voterInfo.id")
                    database.electionDao.insertFollowElection(voterInfo.asVoterInfoToFollowedElectionDatabaseModal())
                    true
                }
                else -> {
                    Timber.i("EXISTING: DELETE STATE IS : True : ${followedElection.id}")
                    database.electionDao.deletedFollowed(voterInfo.id)
                    false
                }
            }

        }
    }

    suspend fun deleteFollowedElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.deletedFollowed(id)
            Timber.i("INSERTING Followed Election:  $id")

            existingFollowedExisting = false
        }
    }


    suspend fun checkForFollowedElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.getFollowedElection(id)
            Timber.i("Check Followed Election:  $id")
        }
    }

}