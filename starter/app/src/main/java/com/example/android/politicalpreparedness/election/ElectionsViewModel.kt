package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {

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

    //init is called immediately when this ViewModel is created
    init{
        viewModelScope.launch {

            //call api to elections

            //get saved data from local storage
        }
    }

    fun displayElections(election: Election){
        _navigateToElectionsProperty.value = election
    }

    //prevent unwanted naivation
    fun displayElectionCompleted(){
        _navigateToElectionsProperty.let {
            it.value = null
        }
    }
}