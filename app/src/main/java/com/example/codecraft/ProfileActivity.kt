package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var closeIcon: ImageView
    private lateinit var ubahButton: Button

    private lateinit var usernameValue: TextView
    private lateinit var asalValue: TextView
    private lateinit var scoreValue: TextView
    private lateinit var levelValue: TextView

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize UI elements
        closeIcon = findViewById(R.id.close_icon)
        ubahButton = findViewById(R.id.ubah_button)
        usernameValue = findViewById(R.id.username_value)
        asalValue = findViewById(R.id.asal_value)
        scoreValue = findViewById(R.id.score_value)
        levelValue = findViewById(R.id.level_value)

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
            // Redirect to login if user is not authenticated
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Fetch all user data from their Firestore document
        firestore.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    usernameValue.text = document.getString("displayName") ?: "No Name Provided"
                    asalValue.text = document.getString("asal") ?: "Not Set"
                    scoreValue.text = (document.getLong("score") ?: 0L).toString()
                    levelValue.text = document.getString("level") ?: "Beginner"
                } else {
                    // Fallback if the user document somehow doesn't exist
                    Toast.makeText(this, "Could not find user profile.", Toast.LENGTH_SHORT).show()
                    usernameValue.text = "Error"
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}