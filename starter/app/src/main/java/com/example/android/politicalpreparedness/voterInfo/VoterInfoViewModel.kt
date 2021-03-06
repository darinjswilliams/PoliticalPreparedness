package com.example.android.politicalpreparedness.voterInfo

import android.app.Application
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.CivicsRepository
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch
import kotlinx.serialization.protobuf.ProtoBuf.Companion.context
import timber.log.Timber

class VoterInfoViewModel(
    private val electionId: Int,
    private val division: Division,
    private val application: Application
) : ViewModel() {



    //Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo

    //Add var and methods to populate voter info
    private val _voterInformationUrl = MutableLiveData<String>()
    val voterInformationUrl: LiveData<String>
        get() = _voterInformationUrl

    private val _voterData = MutableLiveData<VoterInfo>()
    val voterData: LiveData<VoterInfo>
        get() = _voterData


    private val _ballotUrl = MutableLiveData<String>()
    val ballotUrl: LiveData<String>
        get() = _ballotUrl


    //Add var and methods to support loading URLs
    private val _url = MutableLiveData<String>()

    val url: LiveData<String>
        get() = _url

    private val _voterResponse = MutableLiveData<VoterInfoResponse>()
    val voterResponse: LiveData<VoterInfoResponse>
        get() = _voterResponse


    private val _electionInfo = MutableLiveData<Election>()
    val electionInfo: LiveData<Election>
        get() = _electionInfo

    private val _isfollowingElection = MutableLiveData<Boolean>()
    val isfollowingElection: LiveData<Boolean>
        get() = _isfollowingElection


    //Add var and methods to save and remove elections to local database
    //cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    private val database = getInstance(application)
    private val civicsRepository = CivicsRepository(database)


    init {
        viewModelScope.launch {
           Timber.d("VoterInfoViewModel")

            try {
               Timber.d(
                    "VoterInfoViewModel: ElectionId: $electionId " +
                            "Division Country: ${division.country}  " +
                            "Division State ${division.state}"
                )
                civicsRepository.getVoterInformation(electionId, division)


                _voterInfo.value = civicsRepository.informationForVoters

               Timber.d("Voter Information DB: ${_voterInfo.value?.name}  and ${_voterInfo.value?.ballotInfoUrl}")
               Timber.d("Is following Election : $isfollowingElection")

                //check followed elections
                _isfollowingElection.value =  when(civicsRepository.checkForFollowedElection(electionId)){
                    is Result.Success<*> -> {
                       Timber.d("Init: Result is True")
                        true
                    }
                    is Result.Error -> {
                       Timber.d("Init: Result is False")
                        false
                    }
                }


            } catch (e: Exception) {
               Timber.d("Exception Calling Get Voter Information: ${e.localizedMessage}")
            }

        }
    }

    fun navigateToWebSite(url: String) {
       Timber.d("URL: $url")
        _url.value = url
    }

    fun navigationToWebSiteComplete() {
        _url.value = null
    }
//
//    fun doneNavigation() {
//        _navigateToElection.value = null
//    }

    /**
     * insert the followed election into  database.
     *
     * Then navigates back to the ElectionFragment.
     */

    fun onFollowedElectionTracking() {
        viewModelScope.launch {
           Timber.d("OnFollowedElection: ")

           Timber.d("BEFORE IS FOLLOWED ${_isfollowingElection.value}")


            try {
                _isfollowingElection.value =  when(
                    voterInfo.let {civicsRepository.checkForFollowedElection(voterInfo.value!!.id)}){
                    is Result.Success<*> -> {
                       Timber.d("onFollowedElectionTracking: Result is True: ${voterInfo.value!!.id}")
                        voterInfo.value?.let { civicsRepository.deleteFollowedElection(voterInfo.value!!) }
                        false
                    }
                    is Result.Error -> {
                       Timber.d("onFollowedElectionTracking: Result is False: ${voterInfo.value!!.id}")
                        voterInfo.value?.let { civicsRepository.insertfollowElection(voterInfo.value!!) }
                        true
                    }
                }
            } catch (e: Exception) {
               Timber.d(("Incomplete voter information"))
                Toast.makeText(application.applicationContext, R.string.voterIncomplete, Toast.LENGTH_LONG).show()
            }

           Timber.d("After IS FOLLOWED ${_isfollowingElection.value}")

        }
    }


}