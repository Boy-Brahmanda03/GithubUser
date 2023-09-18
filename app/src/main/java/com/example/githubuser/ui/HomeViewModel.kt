package com.example.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toast

    companion object {
        private const val TAG = "MainViewModel"
        private var USERNAME = "Boy-Brahmanda03"
    }

    init {
        searchUsers(USERNAME)
    }
    fun searchUsers(username: String) {
        val client = ApiConfig.getApiService().getUser(username)
        _isLoading.value = true
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null){
                        if (responseBody.totalCount < 1){
                            _toast.value = Event("Username tidak tersedia")
                        } else {
                            _listUsers.value = responseBody.items
                        }
                    }
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }


}