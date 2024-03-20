package com.cindy.githubuser.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cindy.githubuser.data.response.ItemsItem
import com.cindy.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.listUsers.observe(this) { users ->
            setUsersData(users)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // mengatur layout recycle view user
        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            // listener ketika editText diedit
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                // Mengatur text pada searchView ke dalam serachBar
                searchBar.setText(searchView.getText())
                // Ketika pencarian
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // ambil text pada searchView
                    val query = searchView.text.toString().trim()
                    // cek jika text tidak kosong, maka cari user berdasarkan query text
                    if (query.isNotEmpty()) {
                        mainViewModel.searchUsers(query)
                    } else {
                        Toast.makeText(this@MainActivity, "Please enter a search query", Toast.LENGTH_SHORT).show()
                    }
                    // Sembunyikan SearchView setelah teks dimasukkan dan tombol enter ditekan
                    searchView.hide()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setUsersData(userItems: List<ItemsItem>) {
        val adapter = UsersAdapter()
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