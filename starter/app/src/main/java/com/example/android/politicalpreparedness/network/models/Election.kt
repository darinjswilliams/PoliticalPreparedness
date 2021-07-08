package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Election(
    val id: Int,
    val name: String,
    val electionDay: Date,
    var isFollowed: Boolean = false,
    val division: Division
) : Parcelable