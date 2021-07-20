package com.example.android.politicalpreparedness.voterInfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.CivicsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(
    private val electionId: Int,
    private val division: Division,
    private val application: Application
) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo

    //TODO: Add var and methods to populate voter info
    private val _voterInformationUrl = MutableLiveData<String>()
    val voterInformationUrl: LiveData<String>
        get() = _voterInformationUrl

    private val _voterData = MutableLiveData<VoterInfo>()
    val voterData: LiveData<VoterInfo>
        get() = _voterData


    private val _ballotUrl = MutableLiveData<String>()
    val ballotUrl: LiveData<String>
        get() = _ballotUrl


    //TODO: Add var and methods to support loading URLs
    private val _url = MutableLiveData<String>()

    val url: LiveData<String>
        get() = _url

    private val _voterResponse = MutableLiveData<VoterInfoResponse>()
    val voterResponse: LiveData<VoterInfoResponse>
        get() = _voterResponse


    private val _electionInfo = MutableLiveData<Election>()
    val electionInfo: LiveData<Election>
        get() = _electionInfo

    private val _navigateToElection = MutableLiveData<Boolean?>()
    val navigateToElection: LiveData<Boolean?>
        get() = _navigateToElection


    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    private val database = getInstance(application)
    private val civicsRepository = CivicsRepository(database)
//    val voterInfoData = civicsRepository.voterInformation


    init {
        viewModelScope.launch {
            Timber.i("VoterInfoViewModel")

            try {
                Timber.i(
                    "VoterInfoViewModel: ElectionId: $electionId " +
                            "Division Country: ${division.country}  " +
                            "Division State ${division.state}"
                )
                civicsRepository.getVoterInformation(electionId, division)


                val stateCounty = "${division.country},${division.state}"
//                _voterResponse.value =
//                    CivicsApi.retrofitService.getVoterInfo(stateCounty, electionId.toLong())

                _voterInfo.value = civicsRepository.informationForVoters
//
//                _electionInfo.value = civicsRepository.electionInfoForVoters

//                Timber.i("Title ${_electionInfo.value?.name}")
                _navigateToElection.value = civicsRepository.existingFollowedExisting

                Timber.i("Voter Information DB: ${_voterInfo.value?.name}  and ${_voterInfo.value?.ballotInfoUrl}")


            } catch (e: Exception) {
                Timber.i("Exception Calling Get Voter Information: ${e.localizedMessage}")
            }

        }
    }

    fun navigateToWebSite(url: String) {
        Timber.i("URL: $url")
        _url.value = url
    }

    fun navigationToWebSiteComplete() {
        _url.value = null
    }

    fun doneNavigation() {
        _navigateToElection.value = null
    }

    /**
     * insert the followed election into  database.
     *
     * Then navigates back to the ElectionFragment.
     */

    fun onFollowedElectionTracking() {
        viewModelScope.launch {
            Timber.i("OnFollowedElection: ")


            Timber.i("BEFORE IS FOLLOWED ${civicsRepository.existingFollowedExisting}")
            Timber.i("BEFORE IS FOLLOWED ${_navigateToElection.value}")

            //Voter Info value is populated when viewmodel is launched
//            val followedElection = voterInfo.value ?: return@launch
//            val followedElection = voterInfo.value

            voterInfo.value?.let { civicsRepository.trackFollowedElection(it) }

            _navigateToElection.value = civicsRepository.existingFollowedExisting
        }
    }


}