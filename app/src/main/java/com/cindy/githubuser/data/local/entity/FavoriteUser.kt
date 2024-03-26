package com.cindy.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
class FavoriteUser (
    @PrimaryKey(autoGenerate = false)
    var username: String = "",

    var avatarUrl: String? = null,

    @field:ColumnInfo(name = "liked")
    var isLiked: Boolean
)