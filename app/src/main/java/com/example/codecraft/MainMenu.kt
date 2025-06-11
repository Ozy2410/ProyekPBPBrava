package com.example.codecraft

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast // For demonstration toasts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {

    private lateinit var closeIcon: ImageView
    private lateinit var dailyTaskProgressBar: ProgressBar

    // Bottom Navigation elements
    private lateinit var navHome: LinearLayout
    private lateinit var navLeaderboard: LinearLayout
    private lateinit var navBookmarks: LinearLayout
    private lateinit var navSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        closeIcon = findViewById(R.id.close_icon_dashboard)
        dailyTaskProgressBar = findViewById(R.id.daily_task_progress_bar)

        // Set initial progress for the daily task
        dailyTaskProgressBar.progress = 5 // Example value

        // Initialize bottom navigation elements
        navHome = findViewById(R.id.nav_home)
        navLeaderboard = findViewById(R.id.nav_leaderboard)
        navBookmarks = findViewById(R.id.nav_bookmarks)
        navSettings = findViewById(R.id.nav_settings)


        // Set up click listeners
        closeIcon.setOnClickListener {
            // Handle close action, e.g., navigate back or exit app
            finish()
        }

        // Example click listeners for quiz cards (you'd have more for each quiz)
        findViewById<CardView>(R.id.daily_task_card).setOnClickListener {
            Toast.makeText(this, "Daily Task Clicked!", Toast.LENGTH_SHORT).show()
            // Start Daily Task Quiz activity
            // val intent = Intent(this, DailyTaskQuizActivity::class.java)
            // startActivity(intent)
        }

        findViewById<CardView>(R.id.integer_master_card).setOnClickListener {
            Toast.makeText(this, "Integer Master Quiz Clicked!", Toast.LENGTH_SHORT).show()
        }
        findViewById<CardView>(R.id.king_if_else_card).setOnClickListener {
            Toast.makeText(this, "King If-Else Quiz Clicked!", Toast.LENGTH_SHORT).show()
        }
        findViewById<CardView>(R.id.pro_boolean_card).setOnClickListener {
            Toast.makeText(this, "Pro Boolean Quiz Clicked!", Toast.LENGTH_SHORT).show()
        }
        findViewById<CardView>(R.id.lord_inheritance_card).setOnClickListener {
            Toast.makeText(this, "Lord Inheritance Quiz Clicked!", Toast.LENGTH_SHORT).show()
        }

        findViewById<CardView>(R.id.language_quiz_card).setOnClickListener {
            Toast.makeText(this, "Language Challenge Clicked!", Toast.LENGTH_SHORT).show()
        }
        findViewById<CardView>(R.id.exam_quiz_card).setOnClickListener {
            Toast.makeText(this, "Exam Challenge Clicked!", Toast.LENGTH_SHORT).show()
        }


        // Set up Bottom Navigation listeners (replace with actual navigation logic)
        navHome.setOnClickListener {
            Toast.makeText(this, "Home clicked (already here)", Toast.LENGTH_SHORT).show()
        }
        navLeaderboard.setOnClickListener {
            Toast.makeText(this, "Leaderboard clicked", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, LeaderboardActivity::class.java)
            // startActivity(intent)
        }
        navBookmarks.setOnClickListener {
            Toast.makeText(this, "Bookmarks clicked", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, BookmarksActivity::class.java)
            // startActivity(intent)
        }
        navSettings.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            // val intent = Intent(this, SettingsActivity::class.java)
            // startActivity(intent)
        }
    }
}