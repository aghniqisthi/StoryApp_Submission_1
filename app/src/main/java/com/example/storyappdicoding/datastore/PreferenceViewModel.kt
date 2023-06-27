package com.example.storyappdicoding.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PreferenceViewModel (private val pref: DataPreferences) : ViewModel() {
    fun getSession(): LiveData<String> {
        return pref.getSession().asLiveData()
    }

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveSession(id: String, name:String, token:String) {
        viewModelScope.launch {
            pref.saveSession(id, name, token)
        }
    }

    fun clearSession(){
        viewModelScope.launch {
            pref.clearSession()
        }
    }
}