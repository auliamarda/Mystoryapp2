package com.aulmrd.mystory.ui.home

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aulmrd.mystory.data.response.ListStoryItem
import com.aulmrd.mystory.data.result.Result
import com.aulmrd.mystory.R
import com.aulmrd.mystory.adapter.ListStoryAdapter
import com.aulmrd.mystory.databinding.ActivityHomeBinding
import com.aulmrd.mystory.ui.detail.DetailActivity
import com.aulmrd.mystory.ui.factory.FactoryStoryViewModel
import com.aulmrd.mystory.ui.story.StoryActivity

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        title = "MyStory App"
        upload()
    }

    private fun setupViewModel() {
        val factoryStoryViewModel: FactoryStoryViewModel = FactoryStoryViewModel.getInstance(this)
        homeViewModel = ViewModelProvider(this, factoryStoryViewModel)[HomeViewModel::class.java]
//        homeViewModel.doLogin().observe(this){
//            if (!it){
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            }
//        }

        homeViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                Toast.makeText(this, "token not empty", Toast.LENGTH_SHORT).show()
                homeViewModel.getStories(token).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val stories = result.data.listStory
                                val listStoryAdapter =
                                    ListStoryAdapter(stories as ArrayList<ListStoryItem>)
                                binding.rvStory.adapter = listStoryAdapter

                                listStoryAdapter.setOnItemClickCallback(object :
                                    ListStoryAdapter.OnItemClickCallback {
                                    override fun onItemCLicked(data: ListStoryItem) {
                                        showSelectedStory(data)
                                    }
                                })
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "failure : " + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun upload() {
        with(binding) {
            addNewStory.setOnClickListener {
                Intent(this@Home, StoryActivity::class.java)
                    .apply {
                        startActivity(this)
                    }
            }
        }
    }

    private fun showSelectedStory(story: ListStoryItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STORY, story)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                homeViewModel.logout()
                Intent(this, KeluarActivity::class.java).also {
                    startActivity(it)
                }
                finishAffinity()
                true
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }
}