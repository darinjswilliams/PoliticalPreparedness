package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionViewItemBinding
import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback) {

    //TODO: Create ElectionDiffCallback
    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return newItem.id == oldItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder(ElectionViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val electionProperty = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(electionProperty)
        }
    }
    //TODO: Add companion object to inflate ViewHolder (from)

    //TODO: Create ElectionListener
    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }
}



//TODO: Create ElectionViewHolder
class ElectionViewHolder(private var binding: ElectionViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(election: Election) {
        binding.electionProperty = election

        //evaluate pending values and update views
        binding.executePendingBindings()
    }
}


