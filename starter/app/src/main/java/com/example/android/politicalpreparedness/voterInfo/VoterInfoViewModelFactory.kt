package com.example.android.politicalpreparedness.voterInfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.election.ElectionsViewModel

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val app: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}