package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var usernameTextView: TextView
    private lateinit var profileAvatar: ImageView
    private lateinit var dailyTaskProgressBar: ProgressBar

    // Navigation
    private lateinit var navHome: LinearLayout
    private lateinit var navLeaderboard: LinearLayout
    private lateinit var navBookmarks: LinearLayout
    private lateinit var navSettings: LinearLayout
    private lateinit var closeIcon: ImageView

    // Firebase
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Initialize views
        usernameTextView = findViewById(R.id.username_dashboard)
        profileAvatar = findViewById(R.id.profile_avatar)
        dailyTaskProgressBar = findViewById(R.id.daily_task_progress_bar)

        setupUser()
        setupNavigation()
        setupStaticQuizListeners()
    }

    private fun setupUser() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        usernameTextView.text = currentUser.email ?: "Guest"
    }

    private fun setupStaticQuizListeners() {
        // Find each CardView by its ID from the XML layout
        val integerMasterCard: CardView = findViewById(R.id.integer_master_card)
        val kingIfElseCard: CardView = findViewById(R.id.king_if_else_card)
        val proBooleanCard: CardView = findViewById(R.id.pro_boolean_card)
        val lordInheritanceCard: CardView = findViewById(R.id.lord_inheritance_card)

        // Set OnClick (tap) listeners to start the quiz
        integerMasterCard.setOnClickListener { startQuiz("Java Integers and primitive data types") }
        kingIfElseCard.setOnClickListener { startQuiz("Java if-else conditional statements") }
        proBooleanCard.setOnClickListener { startQuiz("Java boolean logic") }
        lordInheritanceCard.setOnClickListener { startQuiz("Java Inheritance and Polymorphism") }

        // Set OnLongClick (hold) listeners to bookmark the quiz
        integerMasterCard.setOnLongClickListener {
            bookmarkQuiz("integer_master", "Integer Master")
            true
        }
        kingIfElseCard.setOnLongClickListener {
            bookmarkQuiz("king_if_else", "King If - Else")
            true
        }
        proBooleanCard.setOnLongClickListener {
            bookmarkQuiz("pro_boolean", "Pro Boolean")
            true
        }
        lordInheritanceCard.setOnLongClickListener {
            bookmarkQuiz("lord_inheritance", "Lord Inheritance")
            true
        }
    }

    private fun startQuiz(topic: String) {
        val intent = Intent(this, QuizQuestion::class.java)
        intent.putExtra("QUIZ_TOPIC", topic)
        startActivity(intent)
    }

    private fun bookmarkQuiz(quizId: String, quizTitle: String) {
        val currentUser = auth.currentUser ?: return
        val bookmarkData = hashMapOf(
            "quizId" to quizId,
            "title" to quizTitle,
            "bookmarkedAt" to Timestamp.now()
        )
        firestore.collection("users").document(currentUser.uid)
            .collection("bookmarks").document(quizId)
            .set(bookmarkData)
            .addOnSuccessListener {
                Toast.makeText(this, "'$quizTitle' Bookmarked!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupNavigation() {
        navHome = findViewById(R.id.nav_home)
        navLeaderboard = findViewById(R.id.nav_leaderboard)
        navBookmarks = findViewById(R.id.nav_bookmarks)
        navSettings = findViewById(R.id.nav_settings)
        closeIcon = findViewById(R.id.close_icon_dashboard)

        profileAvatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        closeIcon.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        navHome.setOnClickListener {
            Toast.makeText(this, "You are already on the Home screen", Toast.LENGTH_SHORT).show()
        }
        navLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }
        navBookmarks.setOnClickListener {
            startActivity(Intent(this, BookmarksActivity::class.java))
        }
        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
