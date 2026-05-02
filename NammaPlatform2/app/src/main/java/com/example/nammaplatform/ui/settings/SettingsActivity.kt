package com.example.nammaplatform.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nammaplatform.R
import com.example.nammaplatform.utils.LanguageManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: Switch
    private lateinit var notificationsSwitch: Switch
    private lateinit var languageSwitcher: Button
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeViews()
        loadSettings()
        setupClickListeners()
    }

    private fun initializeViews() {
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        notificationsSwitch = findViewById(R.id.notificationsSwitch)
        languageSwitcher = findViewById(R.id.languageSwitcher)
        saveButton = findViewById(R.id.saveButton)
        
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE)
        languageManager = LanguageManager(this)
    }

    private fun loadSettings() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val isNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true)
        val currentLanguage = languageManager.getCurrentLanguage()

        darkModeSwitch.isChecked = isDarkMode
        notificationsSwitch.isChecked = isNotificationsEnabled
        updateLanguageButtonText(currentLanguage)
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            saveSettings()
        }

        languageSwitcher.setOnClickListener {
            toggleLanguage()
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            applyDarkMode(isChecked)
        }
    }

    private fun saveSettings() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("dark_mode", darkModeSwitch.isChecked)
        editor.putBoolean("notifications_enabled", notificationsSwitch.isChecked)
        editor.apply()

        Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show()
    }

    private fun toggleLanguage() {
        val currentLanguage = languageManager.getCurrentLanguage()
        val newLanguage = if (currentLanguage == "en") "kn" else "en"
        
        languageManager.setLanguage(newLanguage)
        updateLanguageButtonText(newLanguage)
        
        // Restart activity to apply language changes
        recreate()
    }

    private fun updateLanguageButtonText(languageCode: String) {
        languageSwitcher.text = if (languageCode == "en") {
            "Switch to ಕನ್ನಡ (Kannada)"
        } else {
            "Switch to English"
        }
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
