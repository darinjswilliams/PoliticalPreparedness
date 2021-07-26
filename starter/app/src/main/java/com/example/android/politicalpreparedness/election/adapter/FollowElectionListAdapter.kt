package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.FollowElectionViewItemBinding
import com.example.android.politicalpreparedness.network.models.Election
import timber.log.Timber

class FollowElectionListAdapter(private val clickListener: FollowElectionListener) :
    ListAdapter<Election
            , FollowedElectionViewHolder>(FollowedElectionDiffCallback) {

    companion object FollowedElectionDiffCallback : DiffUtil.ItemCallback<Election
            >() {
        override fun areItemsTheSame(
            oldItem: Election
            ,
            newItem: Election

        ): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(
            oldItem: Election
            ,
            newItem: Election

        ): Boolean {
            return newItem.id == oldItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowedElectionViewHolder {
        return FollowedElectionViewHolder(FollowElectionViewItemBinding
            .inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FollowedElectionViewHolder, position: Int) {
      val followedElection = getItem(position)
        Timber.i("FollowElection Position $position")

        holder.itemView.setOnClickListener{
            clickListener.onClick(followedElection)
            Timber.i("FollowElectionId: ${followedElection.id}")
        }
        holder.bind(followedElection)
    }

}

class FollowElectionListener(val clickListener: (followedElection: Election
) -> Unit) {
fun onClick(followedElection: Election
) = clickListener(followedElection)
}

class FollowedElectionViewHolder(private var binding: FollowElectionViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(followedElectionInfo: Election
        ){
            binding.followElectionProperty = followedElectionInfo
            binding.executePendingBindings()
        }

    companion object{
        fun from(parent: ViewGroup): FollowedElectionViewHolder {
            return  FollowedElectionViewHolder(FollowElectionViewItemBinding
                .inflate(LayoutInflater.from(parent.context)))
        }
    }

}


