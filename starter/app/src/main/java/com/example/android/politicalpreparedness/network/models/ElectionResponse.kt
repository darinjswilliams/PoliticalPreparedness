package com.example.android.politicalpreparedness.network.models

import com.example.android.politicalpreparedness.database.ElectionEntity
import com.example.android.politicalpreparedness.network.NetworkElection
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionResponse(
        val kind: String,
        val elections: List<ElectionEntity>
)
