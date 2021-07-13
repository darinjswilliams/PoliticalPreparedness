package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.database.ElectionEntity
import com.example.android.politicalpreparedness.database.VoterInfoEntity
import com.example.android.politicalpreparedness.network.models.Division
import com.squareup.moshi.JsonClass
import java.util.*


@JsonClass(generateAdapter = true)
data class NetworkElection(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division
)

fun List<NetworkElection>.asElectionDatabaseModel(): List<ElectionEntity>{
    return map {
        ElectionEntity(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            division = it.division
        )
    }
}

@JsonClass(generateAdapter = true)
data class NetworkVoterInfo(
    val id: Int,
    val votingLocationFinderUrl: String,
    val ballotInfoUrl: String
)

fun List<NetworkVoterInfo>.asVoterInfoDatabaseModel(): List<VoterInfoEntity>{
    return map {
        VoterInfoEntity(
            id = it.id,
            votingLocationFinderUrl = it.votingLocationFinderUrl,
            ballotInfoUrl = it.ballotInfoUrl
        )
    }
}