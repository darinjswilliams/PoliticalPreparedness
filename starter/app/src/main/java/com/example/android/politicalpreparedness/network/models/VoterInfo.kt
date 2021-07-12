package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoterInfo(
    val election: Election,
    val state: State
) : Parcelable
