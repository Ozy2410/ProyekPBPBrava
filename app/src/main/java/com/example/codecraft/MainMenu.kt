package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class DashboardActivity : AppCompatActivity() {

    private lateinit var usernameTextView: TextView
    private lateinit var userLevelTextView: TextView
    private lateinit var userScoreTextView: TextView
    private lateinit var profileAvatar: ImageView

    private lateinit var navHome: LinearLayout
    private lateinit var navLeaderboard: LinearLayout
    private lateinit var navBookmarks: LinearLayout
    private lateinit var navSettings: LinearLayout
    private lateinit var closeIcon: ImageView

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var userListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        usernameTextView = findViewById(R.id.username_dashboard)
        userLevelTextView = findViewById(R.id.user_level_dashboard)
        userScoreTextView = findViewById(R.id.user_score_dashboard)
        profileAvatar = findViewById(R.id.profile_avatar)

        setupNavigation()
        setupStaticQuizListeners()
    }

    override fun onStart() {
        super.onStart()
        setupUserListener()
    }

    override fun onStop() {
        super.onStop()
        userListener?.remove()
    }

    private fun setupUserListener() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userDocRef = firestore.collection("users").document(currentUser.uid)
        userListener = userDocRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Toast.makeText(this, "Error fetching user data.", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val displayName = snapshot.getString("displayName") ?: "Guest"
                val score = snapshot.getLong("score") ?: 0L
                val level = snapshot.getString("level") ?: "Beginner"

                usernameTextView.text = displayName
                userLevelTextView.text = level
                userScoreTextView.text = "$score Points"

            } else {
                usernameTextView.text = "Guest"
                userLevelTextView.text = "Beginner"
                userScoreTextView.text = "0 Points"
            }
        }
    }

    private fun setupStaticQuizListeners() {
        val integerMasterCard: CardView = findViewById(R.id.integer_master_card)
        val kingIfElseCard: CardView = findViewById(R.id.king_if_else_card)
        val proBooleanCard: CardView = findViewById(R.id.pro_boolean_card)
        val lordInheritanceCard: CardView = findViewById(R.id.lord_inheritance_card)

        val integerTopic = "Java Integers and primitive data types"
        val ifElseTopic = "Java if-else conditional statements"
        val booleanTopic = "Java boolean logic"
        val inheritanceTopic = "Java Inheritance and Polymorphism"

        integerMasterCard.setOnClickListener { startQuiz(integerTopic) }
        kingIfElseCard.setOnClickListener { startQuiz(ifElseTopic) }
        proBooleanCard.setOnClickListener { startQuiz(booleanTopic) }
        lordInheritanceCard.setOnClickListener { startQuiz(inheritanceTopic) }

        integerMasterCard.setOnLongClickListener { bookmarkQuiz("integer_master", "Integer Master", integerTopic); true }
        kingIfElseCard.setOnLongClickListener { bookmarkQuiz("king_if_else", "King If - Else", ifElseTopic); true }
        proBooleanCard.setOnLongClickListener { bookmarkQuiz("pro_boolean", "Pro Boolean", booleanTopic); true }
        lordInheritanceCard.setOnLongClickListener { bookmarkQuiz("lord_inheritance", "Lord Inheritance", inheritanceTopic); true }
    }

    private fun startQuiz(topic: String) {
        val intent = Intent(this, QuizQuestion::class.java)
        intent.putExtra("QUIZ_TOPIC", topic)
        startActivity(intent)
    }

    private fun bookmarkQuiz(quizId: String, quizTitle: String, topic: String) {
        val currentUser = auth.currentUser ?: return
        val bookmarkData = hashMapOf(
            "quizId" to quizId,
            "title" to quizTitle,
            "topic" to topic,
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