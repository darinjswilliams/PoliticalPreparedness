package com.example.android.politicalpreparedness.voterInfo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class VoterInfoFragment : Fragment() {

        //TODO: Add ViewModel values and create ViewModel
        private val viewModel: VoterInfoViewModel by lazy {
            val activity = requireNotNull(this.activity) {
                "You can only access the viewModel after onViewCreated"
            }
            ViewModelProvider(
                this,
                VoterInfoViewModelFactory(activity.application)
            ).get(VoterInfoViewModel::class.java)
        }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (TODO( "Needs Implementation, not implemented Yet "))
        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks


        //TODO: Create method to load URL intents
    }

}