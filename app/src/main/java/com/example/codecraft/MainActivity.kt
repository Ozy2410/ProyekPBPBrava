package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Check if user is signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, go straight to the Dashboard
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            // No user is signed in, go to the Login screen
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}
