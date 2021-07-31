package com.example.android.politicalpreparedness.representative


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
        const val REQUEST_CODE_LOCATION_PERMISSION = 1
    }

    //TODO: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            getString(R.string.access)
        }
        ViewModelProvider(
            this,
            RepresentativeViewModelFactory(activity.application)
        ).get(RepresentativeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Establish bindings
        val binding = FragmentRepresentativeBinding.inflate(inflater)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this


        //TODO: Define and assign Representative adapter
        //TODO: Populate Representative adapter
        val repAdpater = RepresentativeListAdapter()
        binding.representativeRecycler.adapter = repAdpater

         viewModel.representatives.observe(viewLifecycleOwner, Observer {
             it?.let{
                 repAdpater.submitList(it)
                 hideKeyboard()
             }
         })


        //TODO: Establish button listeners for field and location search
//        binding.buttonSearch.setOnClickListener{
//            viewModel.getRepresentatives(viewModel.address.value.toString())
//        }

        binding.buttonLocation.setOnClickListener{
            Timber.i("UseLocation Button Clicked")
            getLocation()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        when (requestCode) {
            REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                }
            }
            else -> {
                Timber.i("Permission not Granted Place in SnackBar or Show Rationale")
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)

        return activity?.let {
            ContextCompat.checkSelfPermission(
                it,
               ACCESS_FINE_LOCATION
            )
        } === PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        when (checkLocationPermissions()) {
            true -> {
                LocationServices.getFusedLocationProviderClient(requireContext())
                    .lastLocation.addOnSuccessListener { geolocation ->
                        viewModel.getAddressFromGeoLocation(geoCodeLocation(geolocation))
                        viewModel.getRepresentatives(viewModel.address.value.toString())
                    }
            }
            else -> {
                requestPermissions(
                    arrayOf(ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION_PERMISSION
                )
            }
        }

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        Timber.i("GeocodeLocation: $location")
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        Timber.i("Hide Keyboard")
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}