package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(vararg election: ElectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoterInfo(vararg voterInfo: VoterInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowElection(vararg followedElection: FollowedElectionEntity)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table ORDER BY name")
    fun getElection(): LiveData<List<ElectionEntity>>

//    @Query("SELECT * FROM followed_election_table")
//    suspend fun getFollowedElection(): LiveData<List<FollowedElectionEntity>>


    //TODO: Add select single election query

    @Query("SELECT * FROM  election_table WHERE id =:id")
    suspend fun getElectionById(id: Int): ElectionEntity?

    @Query("SELECT * FROM voter_info_table LIMIT 1 ")
    fun getVoterInformation(): VoterInfoEntity?


    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id =:id")
    suspend fun deleteElection(id: Int)

    @Query("DELETE FROM voter_info_table")
    suspend fun deleteVoterInformation()

    @Query("DELETE FROM followed_election_table WHERE id =:id")
    suspend fun deletedFollowed(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun deleteAllElection()



    //TODO: Add clear query



}