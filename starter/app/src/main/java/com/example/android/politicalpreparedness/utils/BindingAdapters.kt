package com.example.android.politicalpreparedness.utils

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import timber.log.Timber

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    Timber.i("bindRecyclerViewCalled: size of data" + data?.size)
    Timber.i("bindRecyclerViewCalled: size of data %s ", data)
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}
