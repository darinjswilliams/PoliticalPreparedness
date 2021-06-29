package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.CivicsRepository
import com.example.android.politicalpreparedness.utils.ParseDate
import com.example.android.politicalpreparedness.utils.ParseDate.getCurrentDateTime
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: Create live data val for upcoming elections
    private val _elections = MutableLiveData<Election>()

    //TODO: Create live data val for saved elections
    private val _savedElections = MutableLiveData<Election>();


    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    val elections: LiveData<Election>
        get() = _elections

    val savedElections: LiveData<Election>
        get() = _savedElections


    //TODO: Create functions to navigate to saved or upcoming election voter info

    private val _navigateToElectionsProperty = MutableLiveData<Election>()

    val navigateToElectionsProperty: LiveData<Election>
        get() = _navigateToElectionsProperty

    //Repository and Database
    private val database = getInstance(application)
    private val electionRepository = CivicsRepository(database)
    val electionList = buildElectionList().toMutableList()

    //init is called immediately when this ViewModel is created
    init {
        viewModelScope.launch {

            //call api to elections
            electionRepository.refreshElections()

            //get saved data from local storage
        }
    }

    fun displayElections(election: Election) {
        _navigateToElectionsProperty.value = election
    }

    //prevent unwanted naivation
    fun displayElectionCompleted() {
        _navigateToElectionsProperty.let {
            it.value = null
        }
    }

    private fun buildElectionList() = listOf(
        Election(
            200, "Test",
            getCurrentDateTime(), Division("203", "US", "Texas")
        ),
        Election(
            300, "Test 2",
            getCurrentDateTime(), Division("304", "US", "Ok")
        ),
        Election(
            400, "Test 2",
            getCurrentDateTime(), Division("756", "US", "LA")
        )

    )

}