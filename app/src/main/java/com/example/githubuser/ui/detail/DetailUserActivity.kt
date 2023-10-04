package com.example.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.database.FavoriteUser
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var isFavorited: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val username = intent.getStringExtra(EXTRA_LOGIN).toString()
        val urlImage = intent.getStringExtra(EXTRA_IMAGE).toString()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionPagerAdapter = SectionPagerAdapter(this@DetailUserActivity)
        sectionPagerAdapter.username = username
        binding.vpUser.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tlUser, binding.vpUser) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.getDetailUser(username)

        detailViewModel.detailUser.observe(this) { user ->
            setDetailUser(user)
        }

        detailViewModel.getFavoriteUser(username).observe(this) {
            if (it == null) {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
                isFavorited = false
            } else {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.baseline_favorite_24
                    )
                )
                isFavorited = true
            }
        }

        binding.fabFavorite.setOnClickListener {
            val favUser = FavoriteUser(
                username,
                urlImage
            )
            if (isFavorited) {
                detailViewModel.deleteFavoriteUser(favUser)
                Toast.makeText(this,
                    getString(R.string.remove_user_from_favorite), Toast.LENGTH_SHORT).show()
            } else {
                detailViewModel.saveFavoriteUser(favUser)
                Toast.makeText(this, getString(R.string.add_user_to_favorite), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setDetailUser(user: DetailUserResponse) {
        binding.apply {
            tvNameDetail.text = user.name
            tvUsernameDetail.text = user.login
            tvFollowing.text = resources.getString(R.string.following, user.following)
            tvFollowers.text = resources.getString(R.string.followers, user.followers)
        }
        Glide.with(this)
            .load(user.avatarUrl)
            .into(binding.ivUserProfile)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_IMAGE = "extra_image"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_following,
            R.string.tab_followers
        )
    }
}