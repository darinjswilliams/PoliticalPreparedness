package com.example.android.politicalpreparedness.voterInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        //Add binding values
        val binding = FragmentVoterInfoBinding.inflate((inflater))

        val application = requireNotNull(activity).application
        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = bundle.argElectionId
        val divisionInfo = bundle.argDivision

        //Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

       Timber.d("VoterId ${electionId}")
       Timber.d("Division Info: ${divisionInfo.state} and Country: ${divisionInfo.country}")


        val viewModelFactory = VoterInfoViewModelFactory(electionId, divisionInfo, application)

        //Give binding access to the MainViewModel
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel

        //This will update the data binding layouts
        binding.lifecycleOwner = this

        //Handle loading of URLs
        viewModel.url.observe(viewLifecycleOwner, {
            it?.let {
                loadURLs(it)
            }
        })

        return binding.root
    }

    //Create method to load URL intents
    private fun loadURLs(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
        viewModel.navigationToWebSiteComplete()
    }

}