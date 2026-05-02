package com.example.nammaplatform.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nammaplatform.R
import com.example.nammaplatform.ui.auth.LoginActivity

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView
    private lateinit var usersManagementButton: Button
    private lateinit var reportsButton: Button
    private lateinit var settingsButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        initializeViews()
        setupClickListeners()
        displayAdminInfo()
    }

    private fun initializeViews() {
        welcomeText = findViewById(R.id.welcomeText)
        usersManagementButton = findViewById(R.id.usersManagementButton)
        reportsButton = findViewById(R.id.reportsButton)
        settingsButton = findViewById(R.id.settingsButton)
        logoutButton = findViewById(R.id.logoutButton)
    }

    private fun displayAdminInfo() {
        // TODO: Load actual admin user info
        welcomeText.text = "Welcome, Admin User"
    }

    private fun setupClickListeners() {
        usersManagementButton.setOnClickListener {
            // TODO: Navigate to Users Management Activity
        }

        reportsButton.setOnClickListener {
            // TODO: Navigate to Reports Activity
        }

        settingsButton.setOnClickListener {
            // TODO: Navigate to Admin Settings Activity
        }

        logoutButton.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        // TODO: Clear admin session
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
