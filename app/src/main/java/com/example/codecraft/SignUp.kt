package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast // For showing temporary messages
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var asalEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var rewritePasswordEditText: TextInputEditText
    private lateinit var confirmButton: Button
    private lateinit var signInTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username_edit_text)
        asalEditText = findViewById(R.id.asal_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        rewritePasswordEditText = findViewById(R.id.rewrite_password_edit_text)
        confirmButton = findViewById(R.id.confirm_button)
        signInTextView = findViewById(R.id.sign_in_text_button)

        confirmButton.setOnClickListener {
            // Get input values
            val username = usernameEditText.text.toString()
            val asal = asalEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val rewritePassword = rewritePasswordEditText.text.toString()

            if (username.isEmpty() || asal.isEmpty() || email.isEmpty() || password.isEmpty() || rewritePassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != rewritePassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {

                println("Username: $username, Asal: $asal, Email: $email, Password: $password")
                Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

            }
        }

        signInTextView.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}