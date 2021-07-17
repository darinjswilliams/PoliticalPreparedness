package com.example.android.politicalpreparedness.network

import androidx.room.ColumnInfo
import com.example.android.politicalpreparedness.database.ElectionEntity
import com.example.android.politicalpreparedness.database.FollowedElectionEntity
import com.example.android.politicalpreparedness.database.VoterInfoEntity
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.FollowedElectionInfo
import com.example.android.politicalpreparedness.network.models.VoterInfo
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
    val name: String,
    val electionDay: Date,
    val votingLocationFinderUrl: String,
    val ballotInfoUrl: String
)

fun List<NetworkVoterInfo>.asVoterInfoDatabaseModel(): List<VoterInfoEntity>{
    return map {
        VoterInfoEntity(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            votingLocationFinderUrl = it.votingLocationFinderUrl,
            ballotInfoUrl = it.ballotInfoUrl
        )
    }
}

@JsonClass(generateAdapter = true)
data class NetworkFollowedElectionInfo(
    val id: Int,
    val name: String,
    val electionDay: Date
)

fun List<NetworkVoterInfo>.asFollowedElectionDatabaseModel(): List<FollowedElectionEntity>{
    return map {
        FollowedElectionEntity(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay
        )
    }
}

fun NetworkVoterInfo.asSingleFollowedElectionDatabaseModel(): FollowedElectionEntity{
    return FollowedElectionEntity(
            id = this.id,
            name = this.name,
            electionDay = this.electionDay
        )

}


fun VoterInfo.asVoterInfoToFollowedElectionDatabaseModal(): FollowedElectionEntity {
    return FollowedElectionEntity(
        id = this.id,
        name = this.name!!,
        electionDay = this.electionDay
    )

}





