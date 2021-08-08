package com.example.android.politicalpreparedness.election

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.CivicsRepository
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch
import timber.log.Timber

//Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    //Create live data val for upcoming elections
    private val _elections = MutableLiveData<Election>()

    //Create live data val for saved elections
    private val _savedElections = MutableLiveData<Election>()

    private val context = application


    //Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    val elections: LiveData<Election>
        get() = _elections

    val savedElections: LiveData<Election>
        get() = _savedElections


    // Create functions to navigate to saved or upcoming election voter info

    private val _navigateToElectionsProperty = MutableLiveData<Election>()

    val navigateToElectionsProperty: LiveData<Election>
        get() = _navigateToElectionsProperty

    //Repository and Database
    private val database = getInstance(application)
    private val electionRepository = CivicsRepository(database)
    val electionList  = electionRepository.elections
    val followElectionList = electionRepository.followedElectionList

    //init is called immediately when this ViewModel is created
    init {
        viewModelScope.launch {

            //call api to elections
           Timber.d("Before Calling api")
            try {
                when(context.isNetworkAvailable()){
                 true -> {
                     Timber.d("Connection is Available ${context.isNetworkAvailable()}")
                     electionRepository.refreshInformation()
                 }
                 else -> Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
               Timber.d(" Exception Calling RefreshElection%s", e.localizedMessage)
            }
           Timber.d("After Calling api")
        }
    }

    fun displayElections(election: Election) {
       Timber.d("Election ID: ${election.id}")
       Timber.d("Election State & Country: ${election.division!!.state}, ${election.division!!.country}")
        _navigateToElectionsProperty.value = election
    }

    fun displayFollowedElection(followedElectionInfo: Election){
       Timber.d("FollowedElection ID: ${followedElectionInfo.id}")
       Timber.d("FollowedElection state: ${followedElectionInfo.division!!.state}")
       Timber.d("FollowedElection county: ${followedElectionInfo.division!!.country}")

        _navigateToElectionsProperty.value = followedElectionInfo
    }

    //prevent unwanted naivation
    fun displayElectionCompleted() {
        _navigateToElectionsProperty.value = null
    }
}