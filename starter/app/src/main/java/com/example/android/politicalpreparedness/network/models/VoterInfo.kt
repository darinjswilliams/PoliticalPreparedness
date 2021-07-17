package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class VoterInfo(
    val id: Int,
    val name: String? = null,
    val electionDay: Date,
    val electionInfoUrl: String? = null,
    val votingLocationFinderUrl: String? = null,
    val ballotInfoUrl: String? = null,
    val correspondenceAddress: Address? = null
) : Parcelable
