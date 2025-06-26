package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_result)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val score = intent.getIntExtra("USER_SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 5)
        val sessionId = intent.getStringExtra("SESSION_ID")

        val scoreValue: TextView = findViewById(R.id.score_value)
        val congratulationsText: TextView = findViewById(R.id.congratulations_text)
        val nextButton: Button = findViewById(R.id.next_button_result)

        val percentage = if (totalQuestions > 0) (score * 100 / totalQuestions) else 0
        scoreValue.text = "$percentage%"

        val username = FirebaseAuth.getInstance().currentUser?.displayName ?: "Dewa Coding"
        congratulationsText.text = "Congratulations, $username!"

        val passingScoreText: TextView = findViewById(R.id.passing_score_text)
        passingScoreText.text = "You answered $score out of $totalQuestions correctly."

        // Finalize the quiz session in Firestore
        if (sessionId != null) {
            updateQuizSession(sessionId, score)
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    private fun updateQuizSession(sessionId: String, finalScore: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val sessionRef = firestore.collection("quiz_sessions").document(sessionId)

        val updates = mapOf(
            "score" to finalScore,
            "completedAt" to Timestamp.now()
        )

        sessionRef.update(updates)
            .addOnSuccessListener {
                // Score updated successfully
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save final score: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
