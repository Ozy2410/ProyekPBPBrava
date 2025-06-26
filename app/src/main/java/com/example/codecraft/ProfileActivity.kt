package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var closeIcon: ImageView
    private lateinit var ubahButton: Button

    private lateinit var usernameValue: TextView
    private lateinit var asalValue: TextView
    private lateinit var emailValue: TextView
    private lateinit var passwordValue: TextView

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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
        passwordValue = findViewById(R.id.password_value)

        loadUserProfile()

        closeIcon.setOnClickListener {
            finish()
        }

        ubahButton.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Set email and masked password immediately
        emailValue.text = currentUser.email
        passwordValue.text = "********"

        // Fetch additional data from Firestore
        firestore.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    usernameValue.text = document.getString("displayName") ?: "No Name"
                    asalValue.text = document.getString("asal") ?: "Not Set"
                } else {
                    usernameValue.text = currentUser.email // Fallback if no profile doc
                    asalValue.text = "Not Set"
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
