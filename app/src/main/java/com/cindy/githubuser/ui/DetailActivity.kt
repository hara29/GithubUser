package com.cindy.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cindy.githubuser.data.response.DetailUserResponse
import com.cindy.githubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailBinding
    companion object{
        const val EXTRA_USERNAME = "extra_username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.hide()

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
    }

    private fun setUsersDetail(detail: DetailUserResponse) {
        detailBinding.tvUser.text = detail.login
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