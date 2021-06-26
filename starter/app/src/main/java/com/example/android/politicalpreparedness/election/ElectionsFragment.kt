package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
     * do in this Fragment.
     */
    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onViewCreated("
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
): View? {

    //TODO: Add ViewModel values and create ViewModel
    val binding = FragmentElectionBinding.inflate(inflater)

    //TODO: Add binding values
    binding.lifecycleOwner = this

    binding.viewModel = viewModel


    //TODO: Link elections to voter info

    //TODO: Initiate recycler adapters
    binding.upcomingElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
        viewModel.displayElections(it)
    })

    //TODO: Populate recycler adapters



    return binding.root
}

//TODO: Refresh adapters when fragment loads

}