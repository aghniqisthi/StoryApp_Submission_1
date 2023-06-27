package com.example.storyappdicoding.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityMainBinding
import com.example.storyappdicoding.datastore.DataPreferences
import com.example.storyappdicoding.datastore.PreferenceViewModel
import com.example.storyappdicoding.datastore.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    companion object{
        const val DELAY_DURATION = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Glide.with(this).asGif().load(R.drawable.imgsplash).into(binding.imgSplash)

        val pref = DataPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getToken().observe(this) { token->
            Handler(Looper.getMainLooper()).postDelayed({
                intent = if(token.isNullOrEmpty()){
                    Intent(this, AuthenticationActivity::class.java)
                } else Intent(this, StoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
                                                        }, DELAY_DURATION)
        }
    }
}