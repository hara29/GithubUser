package com.cindy.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.*
import com.cindy.githubuser.data.remote.response.ItemsItem
import com.cindy.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.*

class FollowViewModel: ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>?>()
    val listFollowers: MutableLiveData<List<ItemsItem>?> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>?>()
    val listFollowing: MutableLiveData<List<ItemsItem>?> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast
    companion object{
        private const val TAG = "FollowViewModel"
    }
    fun searchFollowers(query: String){
        findFollowers(query)
    }
    fun searchFollowing(query: String){
        findFollowing(query)
    }
    private fun findFollowers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody
                    }
                } else {
                    _errorToast.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorToast.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    private fun findFollowing(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = responseBody
                    }
                } else {
                    _errorToast.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _errorToast.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}