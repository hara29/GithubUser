package com.cindy.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cindy.githubuser.R
import com.cindy.githubuser.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var sc_github: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sc_github = findViewById(R.id.splash_screen)
        sc_github.alpha = 0f
        sc_github.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}