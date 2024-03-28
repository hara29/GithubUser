package com.cindy.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cindy.githubuser.data.local.entity.FavoriteUser

@Dao
interface FavDao {
    @Query("SELECT * FROM favorite_user ORDER BY username ASC")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user where favorite_user.username = :username")
    fun checkUser(username: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: FavoriteUser)

    @Query("DELETE FROM favorite_user WHERE favorite_user.username = :username")
    suspend fun deleteFromFavorite(username: String): Int

}