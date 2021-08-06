package com.example.android.politicalpreparedness.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.FollowElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElectionInfo
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative
import timber.log.Timber

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
   Timber.d("bindRecyclerViewCalled: size of data" + data?.size)
   Timber.d("bindRecyclerViewCalled: size of data %s ", data)
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listFollowElectionData")
fun bindFollowElectionRecyclerView(recyclerView: RecyclerView, data: List<Election>?){
   Timber.d("followElectionbindRecyclerViewCalled: size of data" + data?.size)
   Timber.d("followElectionbindRecyclerViewCalled: size of data %s ", data)
    val adapter = recyclerView.adapter as FollowElectionListAdapter
    adapter.submitList(data)
}

