package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText // Import for TextInputEditText

class Login : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var googleSignInButton: Button
    private lateinit var signUpTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Apply window insets for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements using findViewById
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        signInButton = findViewById(R.id.sign_in_button)
        googleSignInButton = findViewById(R.id.google_sign_in_button)
        signUpTextView = findViewById(R.id.signup_text_button)

        // Set up click listeners using Kotlin's lambda syntax
        signInButton.setOnClickListener {
            // Handle Sign In button click
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Implement your sign-in logic here (e.g., API call, validation)
            // For demonstration, let's just print to Logcat
            println("Email: $email, Password: $password")
            // You might want to navigate to another activity upon successful login
            // val intent = Intent(this, MainActivity::class.java)
            // startActivity(intent)
        }

        googleSignInButton.setOnClickListener {
            // Handle Google Sign In button click
            // Implement Google Sign-In integration here
            println("Google Sign In button clicked.")
        }

        signUpTextView.setOnClickListener {
            // Handle Sign Up text click
            // Navigate to your Sign Up activity
            // val intent = Intent(this, SignUpActivity::class.java)
            // startActivity(intent)
            println("Sign Up text clicked.")
        }
    }
}