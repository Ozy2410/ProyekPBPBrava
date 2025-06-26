package com.example.codecraft

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

// Data class for a single quiz question (can be used for Firestore serialization)
data class QuizQuestionItem(
    val question: String = "",
    val options: List<String> = listOf(),
    val correctAnswer: String = ""
)

class QuizQuestion : AppCompatActivity(), View.OnClickListener {

    private lateinit var questionText: TextView
    private lateinit var progressText: TextView
    private lateinit var optionButtons: List<Button>

    private var quizQuestions: List<QuizQuestionItem> = listOf()
    private var currentQuestionIndex = 0
    private var score = 0

    // Firebase and Session variables
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var currentSessionId: String? = null
    private lateinit var quizTopic: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        questionText = findViewById(R.id.question_text)
        progressText = findViewById(R.id.question_progress_text)
        optionButtons = listOf(
            findViewById(R.id.button),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4)
        )

        optionButtons.forEach { it.setOnClickListener(this) }

        quizTopic = intent.getStringExtra("QUIZ_TOPIC") ?: "General Java"
        generateQuiz(quizTopic)
    }

    private fun generateQuiz(topic: String) {
        questionText.text = "Generating quiz on $topic..."
        setButtonsEnabled(false)

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.apiKey,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prompt = "Generate a 5-question multiple-choice quiz about '$topic'. The output must be a valid JSON object. Each question must have a 'question' string, an array of 4 'options', and a 'correctAnswer' string that exactly matches one of the options. Example format: {\"questions\": [{\"question\": \"...\", \"options\": [\"...\"], \"correctAnswer\": \"...\"}]}"
                val response = generativeModel.generateContent(prompt)
                parseQuizResponse(response.text ?: "")
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@QuizQuestion, "Error generating quiz: ${e.message}", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun parseQuizResponse(responseText: String) {
        try {
            val jsonObject = JSONObject(responseText)
            val questionsArray = jsonObject.getJSONArray("questions")
            val parsedQuestions = mutableListOf<QuizQuestionItem>()
            for (i in 0 until questionsArray.length()) {
                val questionObject = questionsArray.getJSONObject(i)
                val question = questionObject.getString("question")
                val optionsArray = questionObject.getJSONArray("options")
                val options = (0 until optionsArray.length()).map { optionsArray.getString(it) }
                val correctAnswer = questionObject.getString("correctAnswer")
                parsedQuestions.add(QuizQuestionItem(question, options, correctAnswer))
            }
            quizQuestions = parsedQuestions

            // Now, save this session to Firestore
            createQuizSessionInFirestore()

        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(this@QuizQuestion, "Error parsing quiz data. Please try again.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun createQuizSessionInFirestore() {
        val currentUser = auth.currentUser ?: return

        val sessionData = hashMapOf(
            "userId" to currentUser.uid,
            "topic" to quizTopic,
            "questions" to quizQuestions.map { mapOf("question" to it.question, "options" to it.options, "correctAnswer" to it.correctAnswer) },
            "userAnswers" to listOf<String>(),
            "score" to 0,
            "createdAt" to Timestamp.now()
        )

        firestore.collection("quiz_sessions")
            .add(sessionData)
            .addOnSuccessListener { documentReference ->
                currentSessionId = documentReference.id
                runOnUiThread {
                    loadQuestion() // Start the quiz only after the session is created
                }
            }
            .addOnFailureListener { e ->
                runOnUiThread {
                    Toast.makeText(this, "Failed to create quiz session: ${e.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < quizQuestions.size) {
            resetButtonColors()
            setButtonsEnabled(true)
            val questionItem = quizQuestions[currentQuestionIndex]
            questionText.text = questionItem.question
            progressText.text = "Question ${currentQuestionIndex + 1}/${quizQuestions.size}"
            optionButtons.forEachIndexed { index, button ->
                button.text = questionItem.options.getOrNull(index) ?: ""
            }
        } else {
            // Quiz finished
            val intent = Intent(this, QuizResultActivity::class.java).apply {
                putExtra("USER_SCORE", score)
                putExtra("TOTAL_QUESTIONS", quizQuestions.size)
                putExtra("SESSION_ID", currentSessionId) // Pass the session ID
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(v: View?) {
        setButtonsEnabled(false)
        val clickedButton = v as? Button ?: return
        val selectedAnswer = clickedButton.text.toString()
        val correctAnswer = quizQuestions[currentQuestionIndex].correctAnswer

        if (selectedAnswer == correctAnswer) {
            score++
            clickedButton.setBackgroundColor(Color.GREEN)
        } else {
            clickedButton.setBackgroundColor(Color.RED)
            optionButtons.find { it.text == correctAnswer }?.setBackgroundColor(Color.GREEN)
        }

        saveAnswerToFirestore(selectedAnswer)

        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 2000)
    }

    private fun saveAnswerToFirestore(answer: String) {
        if (currentSessionId == null) return

        val sessionRef = firestore.collection("quiz_sessions").document(currentSessionId!!)
        sessionRef.update("userAnswers", FieldValue.arrayUnion(answer))
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        optionButtons.forEach { it.isEnabled = enabled }
    }

    private fun resetButtonColors() {
        optionButtons.forEach { it.setBackgroundColor(Color.parseColor("#D1C4E9")) }
    }
}
