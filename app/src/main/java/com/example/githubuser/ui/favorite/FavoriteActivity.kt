package com.example.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.ui.detail.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.layoutManager = layoutManager

        favoriteViewModel.getAllFavoriteUsers().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            setFavoritedUsers(items)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setFavoritedUsers(favoriteUsers: List<ItemsItem>) {
        val adapter = UserAdapter {
            val intent = Intent(this, DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_LOGIN, it.login)
            intent.putExtra(DetailUserActivity.EXTRA_IMAGE, it.avatarUrl)
            startActivity(intent)
        }
        adapter.submitList(favoriteUsers)
        binding.rvFavoriteUser.adapter = adapter
    }
}