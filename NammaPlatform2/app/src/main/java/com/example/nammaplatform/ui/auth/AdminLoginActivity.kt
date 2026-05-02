package com.example.nammaplatform.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nammaplatform.R
import com.example.nammaplatform.ui.admin.AdminDashboardActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var adminEmailInput: EditText
    private lateinit var adminPasswordInput: EditText
    private lateinit var adminLoginButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        adminEmailInput = findViewById(R.id.adminEmailInput)
        adminPasswordInput = findViewById(R.id.adminPasswordInput)
        adminLoginButton = findViewById(R.id.adminLoginButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupClickListeners() {
        adminLoginButton.setOnClickListener {
            performAdminLogin()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun performAdminLogin() {
        val email = adminEmailInput.text.toString().trim()
        val password = adminPasswordInput.text.toString().trim()

        if (validateAdminInputs(email, password)) {
            // TODO: Implement actual admin authentication logic
            // Verify admin credentials against backend
            Toast.makeText(this, "Admin login in progress...", Toast.LENGTH_SHORT).show()
            navigateToAdminDashboard()
        }
    }

    private fun validateAdminInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                adminEmailInput.error = "Admin email is required"
                false
            }
            password.isEmpty() -> {
                adminPasswordInput.error = "Password is required"
                false
            }
            !isValidEmail(email) -> {
                adminEmailInput.error = "Invalid email format"
                false
            }
            password.length < 8 -> {
                adminPasswordInput.error = "Password must be at least 8 characters"
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun navigateToAdminDashboard() {
        startActivity(Intent(this, AdminDashboardActivity::class.java))
        finish()
    }
}
