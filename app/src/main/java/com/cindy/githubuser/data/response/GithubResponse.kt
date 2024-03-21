package com.cindy.githubuser.data.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<ItemsItem>
)

data class ItemsItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

)
data class DetailUserResponse(

	@field:SerializedName("following_url")
	val followingUrl: String? = null,

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("starred_url")
	val starredUrl: String? = null,

	@field:SerializedName("followers_url")
	val followersUrl: String? = null,

	@field:SerializedName("followers")
	val followers: Int? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("html_url")
	val htmlUrl: String? = null,

	@field:SerializedName("following")
	val following: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

//	@field:SerializedName("location")
//	val location: Any? = null,

//	@field:SerializedName("twitter_username")
//	val twitterUsername: Any? = null,

//	@field:SerializedName("bio")
//	val bio: Any? = null,

//	@field:SerializedName("blog")
//	val blog: String? = null,

//	@field:SerializedName("company")
//	val company: Any? = null,

//	@field:SerializedName("public_repos")
//	val publicRepos: Int? = null,

//	@field:SerializedName("gravatar_id")
//	val gravatarId: String? = null,

//	@field:SerializedName("email")
//	val email: Any? = null,

//	@field:SerializedName("organizations_url")
//	val organizationsUrl: String? = null,

//	@field:SerializedName("hireable")
//	val hireable: Any? = null,

//	@field:SerializedName("public_gists")
//	val publicGists: Int? = null,

//	@field:SerializedName("url")
//	val url: String? = null,

//	@field:SerializedName("received_events_url")
//	val receivedEventsUrl: String? = null,

//	@field:SerializedName("events_url")
//	val eventsUrl: String? = null,

//	@field:SerializedName("node_id")
//	val nodeId: String? = null
)
