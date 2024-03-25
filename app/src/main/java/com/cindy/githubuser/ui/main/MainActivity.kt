package com.cindy.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cindy.githubuser.R
import com.cindy.githubuser.data.response.ItemsItem
import com.cindy.githubuser.databinding.ActivityMainBinding
import com.cindy.githubuser.ui.detail.DetailActivity
import com.cindy.githubuser.ui.UsersAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewModel.listUsers.observe(this) { users ->
            setUsersData(users)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.errorToast.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchView.text.toString().trim()
                    if (query.isNotEmpty()) {
                        mainViewModel.searchUsers(query)
                    } else {
                        Toast.makeText(this@MainActivity,
                            getString(R.string.enter_query_warning), Toast.LENGTH_SHORT).show()
                    }
                    searchView.hide()
                    true
                } else {
                    false
                }
            }
        }

    }

    private val adapter = UsersAdapter(object : UsersAdapter.OnItemClickCallback {
        override fun onItemClicked(data: ItemsItem) {
            val moveDataIntent = Intent(this@MainActivity, DetailActivity::class.java)
            moveDataIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            startActivity(moveDataIntent)
        }
    })
    private fun setUsersData(userItems: List<ItemsItem>) {
        adapter.submitList(userItems)
        binding.rvHeroes.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}