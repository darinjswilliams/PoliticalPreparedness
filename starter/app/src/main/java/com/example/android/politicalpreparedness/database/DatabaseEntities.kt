package com.example.android.politicalpreparedness.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.network.models.*
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "election_table")
data class ElectionEntity constructor(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "electionDay") val electionDay: Date,
    @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
)


//Extension Function to convert from database object to domain object
fun List<ElectionEntity>.asElectionDomainModel(): List<Election> {
    return map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            division = it.division
        )
    }
}

//Extension Function to convert from database object to domain object
fun ElectionEntity.asSingleElectionDomainModel(): Election {
    return Election(
        id = this.id,
        name = this.name,
        electionDay = this.electionDay,
        division = this.division
    )

}

@Entity(tableName = "voter_info_table")
data class VoterInfoEntity constructor(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "electionDay") val electionDay: Date,
    @ColumnInfo(name = "electionInfoUrl") val electionInfoUrl: String? = null,
    @ColumnInfo(name = "votingLocationFinderUrl") val votingLocationFinderUrl: String? = null,
    @ColumnInfo(name = "ballotInfoUrl") val ballotInfoUrl: String? = null,
)

fun VoterInfoEntity.asVoterInfoDomainModel(): VoterInfo {
    return VoterInfo(
        id = this.id,
        name = this.name,
        electionDay = this.electionDay,
        electionInfoUrl = this.electionInfoUrl,
        votingLocationFinderUrl = this.votingLocationFinderUrl,
        ballotInfoUrl = this.votingLocationFinderUrl
    )
}

@Entity(tableName = "followed_election_table")
data class FollowedElectionEntity constructor(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "electionDay") val electionDay: Date
)

fun FollowedElectionEntity.asSingleFollowedElectionDomainModel(): FollowedElectionInfo {
    return FollowedElectionInfo(
        id = this.id,
        name = this.name,
        electionDay = this.electionDay
        )
}

fun List<FollowedElectionEntity>.asFollowedElectionDomainModal(): List<FollowedElectionInfo>{
    return map {
        FollowedElectionInfo(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay
        )
    }
}


