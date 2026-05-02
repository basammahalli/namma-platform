package com.example.nammaplatform.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nammaplatform.R
import com.example.nammaplatform.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var adminLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        adminLoginButton = findViewById(R.id.adminLoginButton)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            performUserLogin()
        }

        adminLoginButton.setOnClickListener {
            navigateToAdminLogin()
        }
    }

    private fun performUserLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (validateInputs(email, password)) {
            // TODO: Implement actual authentication logic
            Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
            navigateToHome()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                emailInput.error = "Email is required"
                false
            }
            password.isEmpty() -> {
                passwordInput.error = "Password is required"
                false
            }
            !isValidEmail(email) -> {
                emailInput.error = "Invalid email format"
                false
            }
            password.length < 6 -> {
                passwordInput.error = "Password must be at least 6 characters"
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToAdminLogin() {
        startActivity(Intent(this, AdminLoginActivity::class.java))
    }
}
