package com.example.codecraft

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast // Hanya untuk demo UI
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizResultActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI
    private lateinit var closeIcon: ImageView
    private lateinit var quizTitleResult: TextView
    private lateinit var congratulationsText: TextView
    private lateinit var scoreValue: TextView
    private lateinit var pointValue: TextView
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_result)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi elemen UI
        closeIcon = findViewById(R.id.close_icon_quiz_result)
        quizTitleResult = findViewById(R.id.quiz_title_result)
        congratulationsText = findViewById(R.id.congratulations_text)
        scoreValue = findViewById(R.id.score_value)
        pointValue = findViewById(R.id.point_value)
        nextButton = findViewById(R.id.next_button_result)

        // --- Data dummy untuk demonstrasi UI ---
        // Di aplikasi nyata, nilai-nilai ini akan diterima dari hasil kuis sebenarnya
        val quizName = "Java Quiz"
        val userName = "Dewa Coding" // Nama pengguna yang login
        val userScore = "100%"
        val userPoints = "15"
        val passingScore = "100%"
        val passingPoint = "15"

        quizTitleResult.text = quizName
        congratulationsText.text = "Congratulation $userName!"
        scoreValue.text = userScore
        pointValue.text = userPoints
        findViewById<TextView>(R.id.passing_score_text).text = "Passing Score : $passingScore"
        findViewById<TextView>(R.id.passing_point_text).text = "Passing Point : $passingPoint"
        // ----------------------------------------

        // Set up click listeners
        closeIcon.setOnClickListener {
            Toast.makeText(this, "Close button clicked (UI Only)", Toast.LENGTH_SHORT).show()
            finish() // Menutup activity
        }

        nextButton.setOnClickListener {
            Toast.makeText(this, "Next button clicked (UI Only)", Toast.LENGTH_SHORT).show()
            // Contoh navigasi: kembali ke Dashboard
            // val intent = Intent(this, DashboardActivity::class.java)
            // startActivity(intent)
            // finish()
        }
    }
}