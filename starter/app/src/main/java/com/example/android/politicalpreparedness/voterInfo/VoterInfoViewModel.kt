package com.example.android.politicalpreparedness.voterInfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Division
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

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    private val database = getInstance(application)
    private val civicsRepository = CivicsRepository(database)

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
            } catch (e: Exception) {
                Timber.i("Exception Calling Get Voter Information")
            }

        }
    }

}