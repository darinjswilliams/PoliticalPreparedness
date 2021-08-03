package com.example.android.politicalpreparedness.representative.adapter

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative
import timber.log.Timber

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(view)
            .load(uri)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_profile)
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
            )
            .into(view)

    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

@BindingAdapter("representativeList")
fun bindRepresentativeRecyclerView(recyclerView: RecyclerView, data: List<Representative?>) {
    Timber.i("Here are the representatives size of data:" + data?.size)
    val adapter = recyclerView.adapter as RepresentativeListAdapter
    adapter.submitList(data)
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}


fun Context.isNetworkAvailable() : Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
    return isConnected

}
