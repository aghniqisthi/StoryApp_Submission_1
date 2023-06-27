package com.example.storyappdicoding.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappdicoding.databinding.ActivityDetailBinding
import com.example.storyappdicoding.datastore.DataPreferences
import com.example.storyappdicoding.datastore.PreferenceViewModel
import com.example.storyappdicoding.datastore.ViewModelFactory
import com.example.storyappdicoding.viewmodel.StoryViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val id = intent.getStringExtra("id")

        val storyVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[StoryViewModel::class.java]
        val pref = DataPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getToken().observe(this) { token ->
            storyVM.getDetailStory(id!!, token)
        }

        storyVM.detailstory.observe(this) { data ->
            binding.tvDetailName.text = data.name
            binding.tvDetailDescription.text = data.description
            binding.tvDetailCreatedate.text = data.createdAt
            Glide.with(this).load(data.photoUrl).into(binding.ivDetailPhoto)
        }

        storyVM.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

    }
}