package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {

    private lateinit var closeIcon: ImageView
    private lateinit var ubahButton: Button

    // Assuming you would populate these dynamically from a user object or preferences
    private lateinit var usernameValue: TextView
    private lateinit var asalValue: TextView
    private lateinit var emailValue: TextView
    private lateinit var passwordValue: TextView // Masked password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        closeIcon = findViewById(R.id.close_icon)
        ubahButton = findViewById(R.id.ubah_button)

        usernameValue = findViewById(R.id.username_value)
        asalValue = findViewById(R.id.asal_value)
        emailValue = findViewById(R.id.email_value)
        passwordValue = findViewById(R.id.password_value) // This will typically display "********"

        // --- Populate dummy data (replace with real data from your app's logic) ---
        // In a real app, you would fetch user data from a database, API, or shared preferences
        usernameValue.text = "Amba Fauzi Ramadhan"
        asalValue.text = "Jakarta"
        emailValue.text = "ambaramadhan@gmail.com"
        // Password should never be stored or displayed in plain text.
        // Always display a masked value like "********"
        passwordValue.text = "********"
        // --------------------------------------------------------------------------


        // Set up click listeners
        closeIcon.setOnClickListener {
            // Handle close action, e.g., go back to the previous screen or home
            finish() // Closes the current activity
        }

        ubahButton.setOnClickListener {
            // Handle "Ubah!" button click - navigate to ChangePasswordActivity
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }
}