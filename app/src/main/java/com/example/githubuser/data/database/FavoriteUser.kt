package com.example.githubuser.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    val avatarUrl: String? = null
)
