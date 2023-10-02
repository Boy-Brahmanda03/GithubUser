package com.example.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserRepository

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getAllFavoriteUsers() = userRepository.getAllFavoritedUsers()
}