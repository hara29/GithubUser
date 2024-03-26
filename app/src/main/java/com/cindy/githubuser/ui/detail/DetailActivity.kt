package com.cindy.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cindy.githubuser.R
import com.cindy.githubuser.data.remote.response.DetailUserResponse
import com.cindy.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailBinding
    companion object{
        const val EXTRA_USERNAME = "extra_username"
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

        val username = intent.getStringExtra(EXTRA_USERNAME)

        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]
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
                viewPager.adapter = sectionsPagerAdapter
                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
                supportActionBar?.elevation = 0f
            }
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