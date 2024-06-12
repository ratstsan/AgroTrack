package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.StoryViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.add_story.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        StoryViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var loadStateAdapter: LoadingStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        adapter = StoryAdapter()
        loadStateAdapter = LoadingStateAdapter { adapter.retry() }

        setupView()
        setupAction()
        if (intent.getBooleanExtra("FETCH_NEW_DATA", false)) {
            fetchNewData()
        } else {
            getStory()
        }
    }

    private fun fetchNewData() {
        viewModel.getStories().observe(this) { pagingData ->
            lifecycleScope.launch {
                adapter.submitData(pagingData)
            }
        }
    }

    private fun getStory() {
        viewModel.getStories().observe(this) { pagingData ->
            lifecycleScope.launch {
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading state: Loading")
                }
                is LoadState.NotLoading -> {
                    showLoading(false)
                    Log.d("MainActivity", "Loading state: NotLoading")
                }
                is LoadState.Error -> {
                    showLoading(false)
                    Log.d("MainActivity", "Loading state: Error")
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            // Log jumlah item yang dimuat
            val itemCount = adapter.itemCount
            Log.d("MainActivity", "Loaded $itemCount items")
        }

        binding.rvStory.adapter = adapter.withLoadStateFooter(loadStateAdapter)
    }

    private fun setupView() {
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).also { layoutManager ->
                addItemDecoration(DividerItemDecoration(this@MainActivity, layoutManager.orientation))
            }
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Oops!")
                setMessage("Apa anda yakin ingin keluar?")
                setPositiveButton("Ya") { _, _ ->
                    viewModel.logout()
                }
                setNegativeButton("Tidak", null)
            }.create().show()
        }
        binding.addStoryButton.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
        binding.mapsButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
