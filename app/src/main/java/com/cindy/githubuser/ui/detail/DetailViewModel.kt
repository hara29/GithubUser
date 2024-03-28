package com.cindy.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.cindy.githubuser.data.local.entity.FavoriteUser
import com.cindy.githubuser.data.local.room.FavDao
import com.cindy.githubuser.data.local.room.FavoriteDatabase
import com.cindy.githubuser.data.remote.response.DetailUserResponse
import com.cindy.githubuser.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.*
import retrofit2.*

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorToast = MutableLiveData<String>()
    val errorToast: LiveData<String> = _errorToast

    private var favDao: FavDao
    private var db : FavoriteDatabase

    companion object {
        private const val TAG = "DetailViewModel"
    }

    init {
        db = FavoriteDatabase.getDatabase(application)
        favDao = db.favDao()
    }
    fun searchUsers(query: String) {
        findDetailUser(query)
    }

    private fun findDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = response.body()
                    }
                } else {
                    _errorToast.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorToast.value = t.message
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun addToFavorite(username: String, avatar: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                avatar
            )
            favDao.insertUser(user)
        }
    }
    fun checkuser(username: String) = favDao.checkUser(username)

    fun deleteUser(username: String){
        CoroutineScope(Dispatchers.IO).launch {
            favDao.deleteFromFavorite(username)
        }
    }
}