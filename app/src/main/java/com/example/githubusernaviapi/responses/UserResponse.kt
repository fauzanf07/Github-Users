package com.example.githubusernaviapi.responses

data class UserResponse(
	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("avatar_url")
	val avatar_url: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("name")
	val name: Any?,

	@field:SerializedName("bio")
	val bio: Any?,

	@field:SerializedName("location")
	val location: Any?,

	@field:SerializedName("login")
	val login: String
)
