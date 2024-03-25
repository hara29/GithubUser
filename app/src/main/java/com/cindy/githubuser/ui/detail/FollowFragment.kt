package com.cindy.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cindy.githubuser.data.response.ItemsItem
import com.cindy.githubuser.databinding.FragmentFollowBinding
import com.cindy.githubuser.ui.UsersAdapter
import kotlin.properties.Delegates

class FollowFragment : Fragment() {
    private var position by Delegates.notNull<Int>()
    private lateinit var username: String

    private var _binding:FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFollowBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowViewModel::class.java]
        followViewModel.listFollowers.observe(viewLifecycleOwner) { username ->
            setFollow(username)
        }
        followViewModel.listFollowing.observe(viewLifecycleOwner) { username ->
            setFollow(username)
        }
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        followViewModel.errorToast.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
            }
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }
        if (position == 1){
            followViewModel.searchFollowers(username)
        } else {
            followViewModel.searchFollowing(username)
        }

    }

    private val adapter = UsersAdapter(object : UsersAdapter.OnItemClickCallback {
        override fun onItemClicked(data: ItemsItem) {
            val moveDataIntent = Intent(activity, DetailActivity::class.java)
            moveDataIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            startActivity(moveDataIntent)
        }
    })
    private fun setFollow(userItems: List<ItemsItem>) {
        // val adapter = FollowAdapter()
        adapter.submitList(userItems)
        binding.rvHeroes.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}