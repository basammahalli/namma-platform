package com.example.nammaplatform.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nammaplatform.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var editButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeViews()
        loadProfileData()
        setupClickListeners()
        setEditMode(false)
    }

    private fun initializeViews() {
        profileImageView = findViewById(R.id.profileImageView)
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        addressInput = findViewById(R.id.addressInput)
        editButton = findViewById(R.id.editButton)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
    }

    private fun loadProfileData() {
        // TODO: Load actual user profile data from database or shared preferences
        nameInput.setText("John Doe")
        emailInput.setText("john.doe@example.com")
        phoneInput.setText("+91 98765 43210")
        addressInput.setText("123 Main Street, Bangalore, KA 560001")
    }

    private fun setupClickListeners() {
        editButton.setOnClickListener {
            isEditing = true
            setEditMode(true)
        }

        saveButton.setOnClickListener {
            saveProfileData()
        }

        cancelButton.setOnClickListener {
            isEditing = false
            setEditMode(false)
            loadProfileData() // Reload original data
        }

        profileImageView.setOnClickListener {
            if (isEditing) {
                // TODO: Open image picker
                Toast.makeText(this, "Image picker functionality to be implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setEditMode(editing: Boolean) {
        nameInput.isEnabled = editing
        emailInput.isEnabled = editing
        phoneInput.isEnabled = editing
        addressInput.isEnabled = editing

        if (editing) {
            editButton.visibility = Button.GONE
            saveButton.visibility = Button.VISIBLE
            cancelButton.visibility = Button.VISIBLE
        } else {
            editButton.visibility = Button.VISIBLE
            saveButton.visibility = Button.GONE
            cancelButton.visibility = Button.GONE
        }
    }

    private fun saveProfileData() {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()
        val address = addressInput.text.toString().trim()

        if (validateProfileData(name, email, phone)) {
            // TODO: Save profile data to database
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            isEditing = false
            setEditMode(false)
        }
    }

    private fun validateProfileData(name: String, email: String, phone: String): Boolean {
        return when {
            name.isEmpty() -> {
                nameInput.error = "Name is required"
                false
            }
            email.isEmpty() -> {
                emailInput.error = "Email is required"
                false
            }
            !isValidEmail(email) -> {
                emailInput.error = "Invalid email format"
                false
            }
            phone.isEmpty() -> {
                phoneInput.error = "Phone number is required"
                false
            }
            phone.length < 10 -> {
                phoneInput.error = "Invalid phone number"
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
