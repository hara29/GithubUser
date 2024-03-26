package com.cindy.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cindy.githubuser.data.local.entity.FavoriteUser

@Dao
interface FavDao {
    @Query("SELECT * FROM favorite_user ORDER BY username ASC")
    fun getAllUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favorite_user where liked = 1")
    fun getLikedUser(): LiveData<List<FavoriteUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(news: List<FavoriteUser>)

    @Update
    suspend fun updateUser(user: FavoriteUser)

    @Query("DELETE FROM favorite_user WHERE liked = 0")
    suspend fun deleteAll()

//    @Query("SELECT * from favorite_user ORDER BY username ASC")
//    fun getAllUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_user WHERE username = :username AND liked = 1)")
    suspend fun isUserLiked(username: String): Boolean
}