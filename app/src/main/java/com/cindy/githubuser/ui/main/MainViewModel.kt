package com.cindy.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cindy.githubuser.data.remote.response.GithubResponse
import com.cindy.githubuser.data.remote.response.ItemsItem
import com.cindy.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast
    companion object{
        private const val TAG = "MainViewModel"
        // private const val LOGIN = "Arif"
    }

    fun searchUsers(query: String) {
        findUsers(query)
    }
    private fun findUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
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
                    _errorToast.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _errorToast.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}