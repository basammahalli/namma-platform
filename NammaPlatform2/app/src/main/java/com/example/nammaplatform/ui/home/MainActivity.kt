package com.example.nammaplatform.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nammaplatform.R
import com.example.nammaplatform.ui.profile.ProfileActivity
import com.example.nammaplatform.ui.settings.SettingsActivity
import com.example.nammaplatform.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var settingsButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        profileButton = findViewById(R.id.profileButton)
        settingsButton = findViewById(R.id.settingsButton)
        logoutButton = findViewById(R.id.logoutButton)
    }

    private fun setupClickListeners() {
        profileButton.setOnClickListener {
            navigateToProfile()
        }

        settingsButton.setOnClickListener {
            navigateToSettings()
        }

        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun navigateToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun performLogout() {
        // TODO: Clear user session
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
