package com.example.storyappdicoding.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ID = stringPreferencesKey("id")
    private val NAME = stringPreferencesKey("name")
    private val TOKEN = stringPreferencesKey("token")

    fun getSession(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ID] ?: ""
            preferences[NAME] ?: ""
            preferences[TOKEN] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun saveSession(id: String, name: String, token: String) {
        dataStore.edit { preferences ->
            preferences[ID] = id
            preferences[NAME] = name
            preferences[TOKEN] = token
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = DataPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}