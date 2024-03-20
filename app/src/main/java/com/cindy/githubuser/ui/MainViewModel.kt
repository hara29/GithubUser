package com.cindy.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cindy.githubuser.data.response.GithubResponse
import com.cindy.githubuser.data.response.ItemsItem
import com.cindy.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    companion object{
        private const val TAG = "MainViewModel"
        private const val LOGIN = "Arif"
    }

    init {
        findUsers(LOGIN)
    }

    fun searchUsers(query: String) {
        findUsers(query)
    }
    private fun findUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        // mengeksekusi request secara asynchronous
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUsers.value = response.body()?.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}