package com.cindy.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.cindy.githubuser.R
import com.cindy.githubuser.data.response.DetailUserResponse
import com.cindy.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
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

        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)
        if (username != null) {
                detailViewModel.searchUsers(username)
        }
        detailViewModel.detailUser.observe(this) { details ->
            setUsersDetail(details)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        if (username != null) {
            sectionsPagerAdapter.username  = username
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }

    }

    private fun setUsersDetail(detail: DetailUserResponse) {
            detailBinding.tvUser.text = detail.login
            val nameUser = detail.name?: detail.login
            detailBinding.tvName.text = nameUser
            val followers = "${detail.followers} Followers"
            detailBinding.tvFollowers.text = followers
            val following = "${detail.following} Following"
            detailBinding.tvFollowing.text = following
            Glide.with(this@DetailActivity)
                .load(detail.avatarUrl)
                .into(detailBinding.imgProfileUser)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailBinding.progressBar2.visibility = View.VISIBLE
        } else {
            detailBinding.progressBar2.visibility = View.GONE
        }
    }

}