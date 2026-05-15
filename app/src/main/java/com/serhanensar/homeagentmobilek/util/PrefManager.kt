package com.serhanensar.homeagentmobilek.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PrefManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "home_agent_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setAuthenticated(isAuthenticated: Boolean) {
        sharedPreferences.edit().putBoolean("authenticated", isAuthenticated).apply()
    }

    fun isAuthenticated(): Boolean {
        return sharedPreferences.getBoolean("authenticated", false)
    }

    fun saveApiKey(apiKey: String) {
        sharedPreferences.edit().putString("api_key", apiKey).apply()
    }

    fun getApiKey(): String? {
        // No default — API key must be entered in the Settings screen
        return sharedPreferences.getString("api_key", null)
    }

    fun saveServerUrl(url: String) {
        sharedPreferences.edit().putString("server_url", url).apply()
    }

    fun getServerUrl(): String? {
        return sharedPreferences.getString("server_url", "http://10.55.210.92:8000/") // Default backend
    }
}
