package com.example.codecraft

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var firestore: FirebaseFirestore

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { authTask ->
                        if (authTask.isSuccessful) {
                            // Google Sign-In successful
                            val user = auth.currentUser
                            val userId = user?.uid
                            if (userId != null) {
                                // Optionally, store user data in Realtime Database or Firestore
                                // Example: Store in Realtime Database
                                database.getReference("users").child(userId).setValue(mapOf("email" to user.email, "displayName" to user.displayName))
                                // Example: Store in Firestore
                                firestore.collection("users").document(userId).set(mapOf("email" to user.email, "displayName" to user.displayName))
                            }
                            Toast.makeText(this, "Google Registration successful", Toast.LENGTH_SHORT).show()
                            // Navigate to the next screen (e.g., BakingScreen)
                            // startActivity(Intent(this, BakingActivity::class.java)) // Assuming BakingActivity
                        } else {
                            // Google Sign-In failed
                            Toast.makeText(this, "Google Registration failed: ${authTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: com.google.android.gms.common.api.ApiException) {
                // Google Sign-In failed
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setContent {
            RegisterPage(
                onGoogleSignInClick = {
                    signInWithGoogle()
                }
            )
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Make sure you have this resource in your strings.xml
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
}

@Composable
fun RegisterPage(
    onGoogleSignInClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Registration successful
                            val user = auth.currentUser
                            val userId = user?.uid
                            if (userId != null) {
                                // Optionally, store user data in Realtime Database or Firestore
                                // Example: Store in Realtime Database
                                database.getReference("users").child(userId).setValue(mapOf("email" to email))
                                // Example: Store in Firestore
                                firestore.collection("users").document(userId).set(mapOf("email" to email))
                            }
                            Toast.makeText(context, "Email Registration successful", Toast.LENGTH_SHORT).show()
                            // Navigate to the next screen (e.g., BakingScreen)
                            // context.startActivity(Intent(context, BakingActivity::class.java)) // Assuming BakingActivity
                        } else {
                            // Registration failed
                            Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register with Email")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onGoogleSignInClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register with Google")
        }
    }
}