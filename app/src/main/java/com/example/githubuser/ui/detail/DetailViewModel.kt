package com.example.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.database.FavoriteUser
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _followingUser = MutableLiveData<List<ItemsItem>>()
    val followingUser: LiveData<List<ItemsItem>> = _followingUser

    private val _followersUser = MutableLiveData<List<ItemsItem>>()
    val followersUser: LiveData<List<ItemsItem>> = _followersUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveFavoriteUser(favoriteUser: FavoriteUser){
        viewModelScope.launch {
            userRepository.insertFavoritedUsers(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser){
        viewModelScope.launch {
            userRepository.deleteFavoritedUsers(favoriteUser)
        }
    }
    fun getFavoriteUser(username: String) = userRepository.getFavoritedUser(username)

    fun getFollowersUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                ignoredCall: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _followersUser.value = response.body()
                }
            }

            override fun onFailure(ignoredCall: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure Get Followers: ${t.message}")
            }

        })
    }

    fun getFollowingUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                ignoredCall: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _followingUser.value = response.body()
                }
            }

            override fun onFailure(ignoredCall: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure Get Following: ${t.message}")
            }
        })
    }

    fun getDetailUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        _isLoading.value = true
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody.copy()
                    }
                } else {
                    Log.e(TAG, "onResponse Get Detail : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure Get Detail: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }

}