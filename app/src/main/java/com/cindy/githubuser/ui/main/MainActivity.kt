package com.cindy.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cindy.githubuser.R
import com.cindy.githubuser.data.remote.response.ItemsItem
import com.cindy.githubuser.databinding.ActivityMainBinding
import com.cindy.githubuser.ui.detail.DetailActivity
import com.cindy.githubuser.ui.UsersAdapter
import com.cindy.githubuser.ui.favorite.FavoriteActivity
import com.cindy.githubuser.ui.setting.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = UsersAdapter(object : UsersAdapter.OnItemClickCallback {
        override fun onItemClicked(data: ItemsItem) {
            val moveDataIntent = Intent(this@MainActivity, DetailActivity::class.java)
            moveDataIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            moveDataIntent.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            startActivity(moveDataIntent)
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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

        with(binding){
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_like -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_setting -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(input: String): Boolean {
                    val query = input.trim()
                    mainViewModel.searchUsers(query)
                    searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean = false
            })
            rvHeroes.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                addItemDecoration(DividerItemDecoration(this@MainActivity, (layoutManager as LinearLayoutManager).orientation))
                setHasFixedSize(true)
                adapter = adapter
            }
        }
    }

    private fun setUsersData(userItems: List<ItemsItem>) {
        if (userItems.isEmpty()) {
            Toast.makeText(this, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show()
        } else {
            adapter.submitList(userItems)
            binding.rvHeroes.adapter = adapter
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}