package com.example.android.politicalpreparedness.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "election_table")
data class ElectionEntity constructor(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "electionDay")val electionDay: Date,
    @ColumnInfo(name = "isFollowed") var isFollowed: Boolean = false,
    @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
)


//Extension Function to convert from database object to domain object
fun List<ElectionEntity>.asElectionDomainModel(): List<Election>{
    return map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            isFollowed =  it.isFollowed,
            division = it.division
        )
    }
}
