package com.example.codecraft

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.codecraft.LoginActivity

@Composable
fun BakingScreen(
    bakingViewModel: BakingViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by bakingViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is UiState.Initial -> {
                Text(text = "FRONTEND NGENTOTTT")
                Button(onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Login")
                }
            }
            is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Success -> {
                Text(text = (uiState as UiState.Success).outputText)
            }
            is UiState.Error -> {
                Text(text = (uiState as UiState.Error).errorMessage)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Placeholder for image buttons. Replace with your actual implementation.
            // Example for one button:
            IconButton(onClick = { /* TODO: Call ViewModel with this selection */ }) {
                Image(
                    painter = painterResource(R.drawable.codecraft_logo_square), // Placeholder
                    contentDescription = "An item to be baked"
                )
            }
        }
    }
}