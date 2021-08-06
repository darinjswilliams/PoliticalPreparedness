package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.widget.Toast
import androidx.databinding.InverseMethod
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.android.politicalpreparedness.R

class RepresentativeViewModel(application: Application) : AndroidViewModel(application) {

    //TODO: Establish live data for representatives and address

    //Internal Encapsulation
    private val _representatives = MutableLiveData<List<Representative>>()
    var _address = MutableLiveData<Address>().apply { value = Address() }

    //External
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val address: LiveData<Address>
        get() = _address

    val context = application

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    fun searchForRepresentatives() {
        Timber.i("SearchRepresentatives")
        getRepresentatives()
    }

    //TODO: Create function get address from geo location
    private fun getRepresentatives() {
        viewModelScope.launch {
            try {
                Timber.i("GetRepresentatives:")

                when (context.isNetworkAvailable()) {
                    true -> {
                        Timber.d("Connection is Available ${context.isNetworkAvailable()}")

                        val (offices, officials)
                                = CivicsApi.retrofitService.getRepresentatives(_address.value?.toFormattedString())

                        //Stream data from officials
                        _representatives.value = offices.flatMap { office ->
                            office.getRepresentatives(officials)
                        }
                        Timber.i("Representatives: ${representatives.value}")
                    }
                    else -> Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Timber.i("Exception ${e.localizedMessage}")
            }

        }
    }

    //TODO: Create function to get address from individual fields
    fun getAddressFromGeoLocation(address: Address) {
        Timber.i("Address: $address")
        _address.value = address
    }

}

class RepresentativeViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepresentativeViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}
