package com.example.codecraft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginPage()
        }
    }
}

@Composable
fun LoginPage() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                isLoading = true
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    isLoading = false
                    if (authTask.isSuccessful) {
                        Toast.makeText(context, "Google Sign-In Success", Toast.LENGTH_SHORT).show()

                        // Navigate to Dashboard with user's email
                        val user = authTask.result?.user
                        val intent = Intent(context, DashboardActivity::class.java).apply {
                            putExtra("USER_EMAIL", user?.email)
                        }
                        context.startActivity(intent)
                        (context as? Activity)?.finishAffinity()
                    } else {
                        error = authTask.exception?.message ?: "An unknown error occurred."
                    }
                }
            } catch (e: ApiException) {
                error = "Google Sign-In failed. Code: ${e.statusCode}"
            }
        } else {
            error = "Google Sign-In was cancelled."
        }
    }

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Email/Password Login
                if (email.isBlank() || password.isBlank()) {
                    error = "Email and password cannot be empty."
                    return@Button
                }
                isLoading = true
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    isLoading = false
                    if(task.isSuccessful) {
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                        val user = task.result?.user
                        val intent = Intent(context, DashboardActivity::class.java).apply {
                            putExtra("USER_EMAIL", user?.email)
                        }
                        context.startActivity(intent)
                        (context as? Activity)?.finishAffinity()
                    } else {
                        error = task.exception?.message ?: "Login Failed"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Login with Email")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                error = null
                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    googleSignInLauncher.launch(signInIntent)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Sign in with Google")
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { context.startActivity(Intent(context, RegisterActivity::class.java)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
