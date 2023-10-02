package com.example.githubuser.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favoriteUser: FavoriteUser)

    @Delete
    suspend fun deleteFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun isUserFavorited(username: String): LiveData<FavoriteUser>

    @Query("SELECT * FROM FavoriteUser")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>
}