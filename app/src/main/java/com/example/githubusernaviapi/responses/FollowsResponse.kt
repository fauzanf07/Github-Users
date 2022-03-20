package com.example.githubusernaviapi.responses

data class FollowsResponse(
	@field:SerializedName("FollowsResponse")
	val FollowsResponse: List<FollowsResponseItem>
)

data class FollowsResponseItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatar_url: String,
)
