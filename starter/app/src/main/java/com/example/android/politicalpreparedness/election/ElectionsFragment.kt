package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.election.adapter.FollowElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.FollowElectionListener
import com.example.android.politicalpreparedness.voterInfo.VoterInfoFragment
import com.example.android.politicalpreparedness.voterInfo.VoterInfoViewModel
import timber.log.Timber

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
     * do in this Fragment.
     */
    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity){
            getString(R.string.access)
        }
        ViewModelProvider(
            this,
            ElectionsViewModelFactory(activity.application)
        ).get(ElectionsViewModel::class.java)
    }


override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {

    //TODO: Add ViewModel values and create ViewModel
    val binding = FragmentElectionBinding.inflate(inflater)

    //TODO: Add binding values
    // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
    binding.lifecycleOwner = this

    // Giving the binding access to the OverviewViewModel
    binding.viewModel = viewModel

    //TODO: Link elections to voter info

    //TODO: Initiate recycler adapters
    //TODO: Populate recycler adapters
    binding.upcomingElectionRecycler.adapter = ElectionListAdapter(ElectionListener{
        viewModel.displayElections(it)
    })

    binding.savedElectionsRecycler.adapter = FollowElectionListAdapter(FollowElectionListener {
        viewModel.displayFollowedElection(it)
    })

    viewModel.navigateToElectionsProperty.observe(viewLifecycleOwner, Observer {
        if(null != it){
           Timber.d("Navigate to VoterInfo")
            this.findNavController().navigate(
                ElectionsFragmentDirections
                    .actionElectionsFragmentToVoterInfoFragment(it.id, it.division!!))
            viewModel.displayElectionCompleted()
        }
    })


    return binding.root
}

//TODO: Refresh adapters when fragment loads

}