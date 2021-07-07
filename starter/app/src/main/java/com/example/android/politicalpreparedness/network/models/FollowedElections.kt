package com.example.android.politicalpreparedness.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_table")
data class FollowedElections(
    @PrimaryKey val id: Int
)