package com.example.codecraft

import android.os.Bundle
import android.widget.Button
import android.widget.Toast // For showing temporary messages
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var rewritePasswordEditText: TextInputEditText
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_changepassword) // Ensure this points to your change password layout

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        newPasswordEditText = findViewById(R.id.new_password_edit_text)
        rewritePasswordEditText = findViewById(R.id.rewrite_password_edit_text)
        confirmButton = findViewById(R.id.confirm_button)

        // Set up click listener for the Confirm button
        confirmButton.setOnClickListener {
            // Get input values
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val rewritePassword = rewritePasswordEditText.text.toString()

            // Basic validation
            if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty() || rewritePassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (newPassword != rewritePassword) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // All fields filled and passwords match, proceed with password change logic
                // For demonstration, print to Logcat and show a toast
                println("Password change requested for Username: $username, Email: $email with New Password: $newPassword")
                Toast.makeText(this, "Password Change Confirmed!", Toast.LENGTH_SHORT).show()

                // In a real app, you would:
                // 1. Send this data to your backend API.
                // 2. Handle success/failure responses.
                // 3. Navigate back to the Login screen or a profile screen upon success.
                // Example:
                // val intent = Intent(this, Login::class.java)
                // startActivity(intent)
                // finish() // Close the current activity
            }
        }
    }
}