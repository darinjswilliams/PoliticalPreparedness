package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class FollowedElectionInfo (
    val id: Int,
    val name: String,
    val electionDay: Date
) : Parcelable