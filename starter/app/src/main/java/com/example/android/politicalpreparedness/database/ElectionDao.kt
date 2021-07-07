package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionAndFollowElections

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(vararg election: Election)

    //TODO: Add select all election query
    @Query("Select * from election_table ORDER BY electionDay")
    fun getElection(): LiveData<List<Election>>

    //TODO: Add select single election query

    suspend fun insertFollowElection(election: Election)

    @Query("SELECT id from followed_table WHERE id =:id AND  id IN election_table")
    suspend fun getFollowedElection(id: Int): Int


    @Transaction
    @Query("SELECT * FROM election_table")
    suspend fun getElectionAndFollowElection() : List<ElectionAndFollowElections>

    //TODO: Add delete query
    @Delete
    suspend fun deleteFollowedElection(id: Int) : Int

    //TODO: Add clear query


}