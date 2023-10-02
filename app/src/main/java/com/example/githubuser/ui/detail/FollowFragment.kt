package com.example.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.adapter.UserAdapter

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var index: Int? = null
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.let {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
            username = arguments?.getString(ARG_USERNAME).toString()
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFollow?.layoutManager = layoutManager

        detailViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        when (index) {
            1 -> {
                detailViewModel.getFollowingUser(username)
                detailViewModel.followingUser.observe(requireActivity()) {
                    setFollowUser(it)
                }
            }
            2 -> {
                detailViewModel.getFollowersUser(username)
                detailViewModel.followersUser.observe(requireActivity()) {
                    setFollowUser(it)
                }
            }
        }

    }

    private fun setFollowUser(listFollow: List<ItemsItem>) {
        val adapter = UserAdapter {
            val intent = Intent(requireActivity(), DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_LOGIN, it.login)
            intent.putExtra(DetailUserActivity.EXTRA_IMAGE, it.avatarUrl)
            startActivity(intent)
        }
        adapter.submitList(listFollow)
        binding?.rvFollow?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar3?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}