package com.cindy.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cindy.githubuser.R
import com.cindy.githubuser.data.remote.response.DetailUserResponse
import com.cindy.githubuser.databinding.ActivityDetailBinding
import com.cindy.githubuser.ui.setting.SettingPreferences
import com.cindy.githubuser.ui.setting.SettingViewModel
import com.cindy.githubuser.ui.setting.SettingViewModelFactory
import com.cindy.githubuser.ui.setting.dataStore
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailBinding
    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR = "extra_avatar"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

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

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)

        supportActionBar?.title = username

        val detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        if (username != null) {
                detailViewModel.searchUsers(username)
        }
        detailViewModel.detailUser.observe(this) { details ->
            setUsersDetail(details)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.errorToast.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        if (username != null) {
            with(detailBinding){
                sectionsPagerAdapter.username  = username
                sectionsPagerAdapter.avatar  = avatar?:""
                viewPager.adapter = sectionsPagerAdapter
                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
                supportActionBar?.elevation = 0f
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = username?.let { detailViewModel.checkuser(it) }
            withContext(Dispatchers.Main){
                if (count!= null){
                    if (count>0){
                        detailBinding.toggleFavorite.isChecked = true
                        _isChecked = true
                    }else{
                        detailBinding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }
        detailBinding.toggleFavorite.setOnClickListener{
            _isChecked = !_isChecked
            if(_isChecked){
                if (username != null && avatar != null) {
                    detailViewModel.addToFavorite(username,avatar)
                }
            }else{
                if (username != null) {
                    detailViewModel.deleteUser(username)
                }
            }
            detailBinding.toggleFavorite.isChecked = _isChecked
        }
    }

    private fun setUsersDetail(detail: DetailUserResponse) {
        with(detailBinding){
            tvUser.text = detail.login
            tvName.text = detail.name?: detail.login
            tvFollowers.text = StringBuilder(detail.followers.toString()).append(" Followers")
            tvFollowing.text = StringBuilder(detail.following.toString()).append(" Following")
            Glide.with(this@DetailActivity)
                .load(detail.avatarUrl)
                .into(imgProfileUser)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailBinding.progressBar2.visibility = View.VISIBLE
        } else {
            detailBinding.progressBar2.visibility = View.GONE
        }
    }
}