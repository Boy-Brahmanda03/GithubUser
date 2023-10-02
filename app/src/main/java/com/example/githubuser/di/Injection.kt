package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.database.FavoriteUserDatabase

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = FavoriteUserDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        return UserRepository.getInstance(dao)
    }

}