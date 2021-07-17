package com.example.android.politicalpreparedness.voterInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO: Add binding values
        val binding = FragmentVoterInfoBinding.inflate((inflater))

        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application
        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = bundle.argElectionId
        val divisionInfo = bundle.argDivision

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        Timber.i("VoterId ${electionId}")
        Timber.i("Division Info: ${divisionInfo.state} and Country: ${divisionInfo.country}")


        val viewModelFactory = VoterInfoViewModelFactory(electionId, divisionInfo, application)

        //Give binding access to the MainViewModel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        //TODO: Handle loading of URLs
        viewModel.url.observe(viewLifecycleOwner, {
            it?.let {
                loadURLs(it)
            }
        })


        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks - created onClick Handler
        viewModel.navigateToElection.observe(viewLifecycleOwner, Observer {

            if(it == true){
            findNavController().navigate(
                VoterInfoFragmentDirections.actionVoterInfoFragmentToElectionsFragment()
            )
            viewModel.doneNavigation()
                binding.followedButton.text = getString(R.string.unfollowed)
        }})

        return binding.root
    }

    //TODO: Create method to load URL intents
    private fun loadURLs(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
        viewModel.navigationToWebSiteComplete()
    }

}