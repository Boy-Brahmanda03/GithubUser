package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            svUser.setupWithSearchBar(sbUser)
            svUser
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    sbUser.text = svUser.text
                    svUser.hide()
                    if (textView.text.toString().isEmpty()) {
                        //do nothing
                    } else {
                        homeViewModel.searchUsers(textView.text.toString())
                    }
                    false
                }

        }

        //setting layout manager for recylerview
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.layoutManager = layoutManager

        homeViewModel.listUser.observe(viewLifecycleOwner) { users ->
            setUsersData(users)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.toastText.observe(requireActivity()) {
            it.getContentIfNotHandled()?.let {toastText ->
                Toast.makeText(requireActivity(), toastText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUsersData(users: List<ItemsItem>) {
        val adapter = UserAdapter(){
            val intent = Intent(requireActivity(), DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_LOGIN, it.login)
            startActivity(intent)
        }
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}