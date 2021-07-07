package com.example.android.politicalpreparedness.network.models

import androidx.room.Embedded
import androidx.room.Relation

class ElectionAndFollowElections {
    @Embedded
    val election: Election? = null


    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val followedElections: List<FollowedElections> = ArrayList()
}