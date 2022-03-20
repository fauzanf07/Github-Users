package com.example.githubusernaviapi.responses

annotation class SerializedName(val value: String)
data class SearchResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<ItemsItem>
)


data class ItemsItem(

	@field:SerializedName("avatar_url")
	val avatar_url: String,

	@field:SerializedName("login")
	val login: String
)
