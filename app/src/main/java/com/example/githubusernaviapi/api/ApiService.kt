package com.example.githubusernaviapi.api

import com.example.githubusernaviapi.responses.FollowsResponseItem
import com.example.githubusernaviapi.responses.SearchResponse
import com.example.githubusernaviapi.responses.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users?")
    fun getSearch(
        @Query("q") q: String,
        @Header("Authorization") token: String
    ):Call<SearchResponse>

    @GET("users/{login}")
    fun getUser(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): Call<UserResponse>

    @GET("users/{login}/followers")
    fun getFollowers(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): Call<List<FollowsResponseItem>>

    @GET("users/{login}/following")
    fun getFollowing(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): Call<List<FollowsResponseItem>>
}