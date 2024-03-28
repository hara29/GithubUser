package com.cindy.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cindy.githubuser.data.remote.response.ItemsItem
import com.cindy.githubuser.databinding.ActivityFavoriteBinding
import com.cindy.githubuser.ui.UsersAdapter
import com.cindy.githubuser.ui.detail.DetailActivity
import com.cindy.githubuser.ui.setting.SettingPreferences
import com.cindy.githubuser.ui.setting.SettingViewModel
import com.cindy.githubuser.ui.setting.SettingViewModelFactory
import com.cindy.githubuser.ui.setting.dataStore

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private val adapter = UsersAdapter(object : UsersAdapter.OnItemClickCallback {
        override fun onItemClicked(data: ItemsItem) {
            val moveDataIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            moveDataIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
            moveDataIntent.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
            startActivity(moveDataIntent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"

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

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        binding.apply {
            rvHeroes.setHasFixedSize(true)
            rvHeroes.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvHeroes.adapter = adapter
        }

        viewModel.getFavoriteUser().observe(this){
            if(it != null){
                val itemList = it.map { favoriteUser ->
                    ItemsItem(
                        login = favoriteUser.username,
                        avatarUrl = favoriteUser.avatarUrl
                    )
                }
                adapter.submitList(itemList)
            }
        }
    }
}