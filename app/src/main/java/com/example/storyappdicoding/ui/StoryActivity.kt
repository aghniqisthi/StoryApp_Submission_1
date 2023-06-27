package com.example.storyappdicoding.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storyappdicoding.adapter.ListStoriesAdapter
import com.example.storyappdicoding.databinding.ActivityStoryBinding
import com.example.storyappdicoding.datastore.DataPreferences
import com.example.storyappdicoding.datastore.PreferenceViewModel
import com.example.storyappdicoding.datastore.ViewModelFactory
import com.example.storyappdicoding.model.LoginResult
import com.example.storyappdicoding.model.Story
import com.example.storyappdicoding.viewmodel.StoryViewModel

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.btnTranslate.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        val pref = DataPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]
        val storyVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryViewModel::class.java]

        prefVM.getSession().observe(this) { session->
            if(session.isNullOrEmpty()){
                val sessionData = intent.getParcelableExtra<LoginResult>("data")
                if(sessionData != null){
                    prefVM.saveSession(sessionData.userId, sessionData.name, sessionData.token)
                }
            }
        }

        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(this, 2)
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this, 4)
            else -> GridLayoutManager(this, 2)
        }

        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        prefVM.getToken().observe(this) {
            storyVM.getAllStories(null, null, null, it)
        }

        storyVM.listStories.observe(this) { list ->
            setListStories(list)
        }

        storyVM.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.actionAdd.setOnClickListener {
            val move = Intent(this, AddStoryActivity::class.java)
            startActivity(move)
        }

        binding.actionLogout.setOnClickListener {
            prefVM.clearSession()
            val move = Intent(this, AuthenticationActivity::class.java)
            move.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(move)
            finish()
        }
    }

    private fun setListStories(stories: List<Story>) {
        val listData = stories.map{it}
        val adapter = ListStoriesAdapter(listData)
        binding.rvStory.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}