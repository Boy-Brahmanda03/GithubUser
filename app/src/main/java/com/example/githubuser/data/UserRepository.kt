package com.example.githubuser.data

import androidx.lifecycle.LiveData
import com.example.githubuser.data.database.FavoriteUser
import com.example.githubuser.data.database.FavoriteUserDao

class UserRepository private constructor(
    private val favoriteUserDao: FavoriteUserDao
) {

    suspend fun insertFavoritedUsers(favoriteUser: FavoriteUser) {
        favoriteUserDao.insertFavorite(favoriteUser)
    }

    suspend fun deleteFavoritedUsers(favoriteUser: FavoriteUser){
        favoriteUserDao.deleteFavorite(favoriteUser)
    }

    fun getFavoritedUser(username: String): LiveData<FavoriteUser>{
        return favoriteUserDao.isUserFavorited(username)
    }

    fun getAllFavoritedUsers(): LiveData<List<FavoriteUser>>{
        return favoriteUserDao.getAllFavoriteUsers()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            favoriteUserDao: FavoriteUserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(favoriteUserDao)
            }.also { instance = it }
    }
}