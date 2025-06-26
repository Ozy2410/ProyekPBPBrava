package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class QuizResultActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        val score = intent.getIntExtra("USER_SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 5)
        val sessionId = intent.getStringExtra("SESSION_ID")

        val scoreValue: TextView = findViewById(R.id.score_value)
        val congratulationsText: TextView = findViewById(R.id.congratulations_text)
        val gradeText: TextView = findViewById(R.id.grade_text)
        val nextButton: Button = findViewById(R.id.next_button_result)
        val pointValue: TextView = findViewById(R.id.point_value)

        val percentage = if (totalQuestions > 0) (score * 100 / totalQuestions) else 0
        scoreValue.text = "$percentage%"

        val username = auth.currentUser?.displayName ?: "Dewa Coding"
        congratulationsText.text = "Congratulations, $username!"

        val passingScoreText: TextView = findViewById(R.id.passing_score_text)
        passingScoreText.text = "You answered $score out of $totalQuestions correctly."

        val grade = when {
            percentage >= 90 -> "A"
            percentage >= 80 -> "B"
            percentage >= 70 -> "C"
            percentage >= 60 -> "D"
            else -> "E"
        }
        gradeText.text = "Your Grade: $grade"

        val pointsEarned = score * 5
        pointValue.text = pointsEarned.toString()
        updateUserTotalScore(pointsEarned)

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

    private fun updateUserTotalScore(pointsEarned: Int) {
        val currentUser = auth.currentUser ?: return
        val userDocRef = firestore.collection("users").document(currentUser.uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)
            if (snapshot.exists()) {
                // If the document exists, increment the score
                transaction.update(userDocRef, "score", FieldValue.increment(pointsEarned.toLong()))
            } else {
                // If the document does not exist, create it with the initial score
                val newUser = hashMapOf(
                    "uid" to currentUser.uid,
                    "displayName" to (currentUser.displayName ?: currentUser.email),
                    "email" to currentUser.email,
                    "level" to "Beginner",
                    "score" to pointsEarned
                )
                transaction.set(userDocRef, newUser)
            }
            null // Transaction must return null
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to update total score: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateQuizSession(sessionId: String, finalScore: Int) {
        val sessionRef = firestore.collection("quiz_sessions").document(sessionId)
        val updates = mapOf(
            "score" to finalScore,
            "completedAt" to Timestamp.now()
        )
        sessionRef.update(updates)
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save final score: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}