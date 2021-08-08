package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ElectionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(vararg election: ElectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoterInfo(vararg voterInfo: VoterInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowElection(vararg followedElection: FollowedElectionEntity)


    @Query("SELECT * FROM election_table ORDER BY name")
    fun getElection(): LiveData<List<ElectionEntity>>

    @Query("SELECT * FROM followed_election_table WHERE id =:id")
    suspend fun getFollowedElection(id: Int): FollowedElectionEntity?

    @Query("SELECT * FROM election_table WHERE id in (SELECT id FROM followed_election_table) ORDER BY electionDay DESC")
    fun getAllFollowedElection(): LiveData<List<ElectionEntity>>

    @Query("SELECT * FROM followed_election_table ORDER BY name")
    fun getFollowedElections(): LiveData<List<FollowedElectionEntity>>


    @Query("SELECT * FROM  election_table WHERE id =:id")
    suspend fun getElectionById(id: Int): ElectionEntity?

    @Query("SELECT * FROM voter_info_table LIMIT 1 ")
    fun getVoterInformation(): VoterInfoEntity?



    @Query("DELETE FROM election_table WHERE id =:id")
    suspend fun deleteElection(id: Int)

    @Query("DELETE FROM voter_info_table")
    suspend fun deleteVoterInformation()

    @Query("DELETE FROM followed_election_table WHERE id =:id")
    suspend fun deletedFollowed(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun clear()




}