package com.cindy.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cindy.githubuser.data.local.entity.FavoriteUser
import com.cindy.githubuser.data.local.room.FavDao
import com.cindy.githubuser.data.local.room.FavoriteDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var favDao: FavDao
    private var db : FavoriteDatabase

    init {
        db = FavoriteDatabase.getDatabase(application)
        favDao = db.favDao()
    }

    fun getFavoriteUser() : LiveData<List<FavoriteUser>>{
        return favDao.getFavoriteUser()
    }
}