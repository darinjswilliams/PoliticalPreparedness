package com.example.android.politicalpreparedness.voterInfo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Add binding values
        val binding = FragmentVoterInfoBinding.inflate((inflater))

        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val divisionInfo = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        Timber.i("VoterId ${electionId}")
        Timber.i("Division Info: ${divisionInfo.state} and Country: ${divisionInfo.country}")


        val viewModelFactory = VoterInfoViewModelFactory(electionId, divisionInfo, application)

        //Give binding access to the MainViewModel
        binding.viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(VoterInfoViewModel::class.java)

        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks


        //TODO: Create method to load URL intents

        return binding.root
    }

}