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

    //TODO: Add select all election query
    @Query("Select * from election_table ORDER BY electionDay")
    fun getElection(): LiveData<List<ElectionEntity>>


    @Query("Select * from voter_info_table")
    fun getVoterInformation(): LiveData<VoterInfoEntity>

    //TODO: Add select single election query

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowElection(election: ElectionEntity)

//    @Query("SELECT id from followed_table WHERE id =:id AND  id IN election_table")
//    suspend fun getFollowedElection(id: Int): Int

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id =:id")
    suspend fun deleteFollowedElection(id: Int) : Int

    @Query("DELETE FROM voter_info_table")
    suspend fun deleteVoterInformation()

    //TODO: Add clear query



}