package com.cindy.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
class FavoriteUser (
    @PrimaryKey(autoGenerate = false)
    val username: String = "",
    @field:ColumnInfo(name = "avatarURL")
    val avatarUrl: String = "",
)