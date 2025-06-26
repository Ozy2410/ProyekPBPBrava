package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class QuizSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Use the correct layout file for this activity
        setContentView(R.layout.activity_quiz_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val quizTopic = intent.getStringExtra("QUIZ_TOPIC") ?: "General Java"

        // Initialize UI elements from the activity_quiz_menu.xml layout
        val progressText: TextView = findViewById(R.id.question_progress_title)
        val closeIcon: ImageView = findViewById(R.id.close_icon_quiz_selection)
        val questionGridLayout: GridLayout = findViewById(R.id.question_grid_layout)

        // Hide unnecessary elements from the original layout
        findViewById<View>(R.id.time_label_quiz_selection).visibility = View.GONE
        findViewById<View>(R.id.time_progress_bar_quiz_selection).visibility = View.GONE
        findViewById<View>(R.id.time_value_quiz_selection).visibility = View.GONE
        findViewById<View>(R.id.action_buttons_layout_quiz_selection).visibility = View.GONE

        // --- Adapt the UI to be a simple start screen ---
        progressText.text = quizTopic

        // Clear the grid of numbers that was in the original XML
        questionGridLayout.removeAllViews()
        questionGridLayout.alignmentMode = GridLayout.ALIGN_BOUNDS
        questionGridLayout.columnCount = 1 // Set grid to have a single column

        // Create a "Start Quiz" button programmatically
        val startButton = MaterialButton(this).apply {
            text = "Start Quiz"
            textSize = 20f
            layoutParams = GridLayout.LayoutParams().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                setMargins(16, 200, 16, 16) // Add margin to center it a bit

            }
            setOnClickListener {
                val intent = Intent(this@QuizSelectionActivity, QuizQuestion::class.java).apply {
                    putExtra("QUIZ_TOPIC", quizTopic)
                }
                startActivity(intent)
                finish()
            }
        }

        // Add the new button to the grid layout
        questionGridLayout.addView(startButton)

        closeIcon.setOnClickListener {
            finish()
        }
    }
}
