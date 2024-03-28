package com.cindy.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cindy.githubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favDao(): FavDao
    companion object {
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FavoriteDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteDatabase::class.java, "favorite_user_database")
                        .build()
                }
            }
            return INSTANCE as FavoriteDatabase
        }
    }
}