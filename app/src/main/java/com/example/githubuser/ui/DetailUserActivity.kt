package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_LOGIN).toString()

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

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_following,
            R.string.tab_followers
        )
    }
}