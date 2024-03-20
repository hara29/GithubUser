package com.cindy.githubuser.data.retrofit

import com.cindy.githubuser.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") q: String
    ):Call<GithubResponse>
}