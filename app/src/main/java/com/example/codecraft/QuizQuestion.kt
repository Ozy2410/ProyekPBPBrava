package com.example.codecraft

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore

class QuizQuestion : AppCompatActivity() {

    // Declare UI elements
    private lateinit var questionText: TextView
    private lateinit var optionAButton: Button
    private lateinit var optionBButton: Button
    private lateinit var optionCButton: Button
    private lateinit var optionDButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        // Initialize UI elements
        questionText = findViewById(R.id.question_text)
        optionAButton = findViewById(R.id.button) // ID from your layout
        optionBButton = findViewById(R.id.button2) // ID from your layout
        optionCButton = findViewById(R.id.button3) // ID from your layout
        optionDButton = findViewById(R.id.button4) // ID from your layout

        // Load the question from Firestore
        loadQuestionFromFirestore("your_quiz_id", "your_question_id") // Replace with actual IDs

        // Set click listeners
        setOptionClickListeners()
    }

    private fun setOptionClickListeners() {
        optionAButton.setOnClickListener { selectOption(it as Button) }
        optionBButton.setOnClickListener { selectOption(it as Button) }
        optionCButton.setOnClickListener { selectOption(it as Button) }
        optionDButton.setOnClickListener { selectOption(it as Button) }
    }

    private fun selectOption(selectedButton: Button) {
        // Handle option selection logic here
        Toast.makeText(this, "You selected: ${selectedButton.text}", Toast.LENGTH_SHORT).show()
    }

    // Merged logic from loadQuestion.kt
    private fun loadQuestionFromFirestore(quizId: String, questionId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes").document(quizId).collection("questions").document(questionId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val question = document.getString("question") ?: "N/A"
                    val optionA = document.getString("option_a") ?: "N/A"
                    val optionB = document.getString("option_b") ?: "N/A"
                    val optionC = document.getString("option_c") ?: "N/A"
                    val optionD = document.getString("option_d") ?: "N/A"

                    // Update UI on the main thread
                    runOnUiThread {
                        questionText.text = question
                        optionAButton.text = optionA
                        optionBButton.text = optionB
                        optionCButton.text = optionC
                        optionDButton.text = optionD
                    }
                }
            }
            .addOnFailureListener { exception ->
                runOnUiThread {
                    Toast.makeText(this, "Failed to load question: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}