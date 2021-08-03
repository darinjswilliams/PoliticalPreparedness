package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.CustomDateAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(KotlinJsonAdapterFactory())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(CivicsHttpClient.getClient())
    .baseUrl(Constants.BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //TODO: Add elections API Call
    @GET(Constants.ELECTIONS_FEED)
    suspend fun getElections(): ElectionResponse


    //TODO: Add voterinfo API Call
    @GET(Constants.VOTER_INFO_FEED)
    suspend fun getVoterInfo(@Query(Constants.VOTER_ADDRESS) stateAndCountry: String,
                             @Query(Constants.VOTER_ID) voterId: Long) : VoterInfoResponse

    //TODO: Add representatives API Call
    @GET(Constants.REPRESENTATIVE_FEED)
    suspend fun getRepresentatives(@Query(Constants.VOTER_ADDRESS) address: String?): RepresentativeResponse
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}