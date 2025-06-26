package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var closeIcon: ImageView

    // Bottom Navigation elements
    private lateinit var navHome: LinearLayout
    private lateinit var navLeaderboard: LinearLayout
    private lateinit var navBookmarks: LinearLayout
    private lateinit var navSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        closeIcon = findViewById(R.id.close_icon_leaderboard)

        // Initialize bottom navigation elements
        navHome = findViewById(R.id.nav_home)
        navLeaderboard = findViewById(R.id.nav_leaderboard)
        navBookmarks = findViewById(R.id.nav_bookmarks)
        navSettings = findViewById(R.id.nav_settings)

        // Set up click listeners
        closeIcon.setOnClickListener {
            finish()
        }

        // --- Populate dummy leaderboard data ---

        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_1), "#1", "Ahmad Fauzi Ramadhan", "Expert")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_2), "#2", "Faiz Raihan", "Expert")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_3), "#3", "Sofyan Albiansyah", "Expert")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_4), "#4", "Raden Faiz", "Advanced")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_5), "#5", "Lorem Ipsum", "Advanced")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_6), "#6", "Lorem Ipsum", "Advanced")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_7), "#7", "Lorem Ipsum", "Intermediate")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_8), "#8", "Lorem Ipsum", "Intermediate")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_9), "#9", "Lorem Ipsum", "Intermediate")
        populateLeaderboardEntry(findViewById(R.id.leaderboard_entry_10), "#10", "Lorem Ipsum", "Intermediate")



        navHome.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java) // Navigate to Dashboard
            startActivity(intent)
            finish()
        }
        navLeaderboard.setOnClickListener {
            Toast.makeText(this, "Leaderboard clicked (already here)", Toast.LENGTH_SHORT).show()
        }
        navBookmarks.setOnClickListener {
            val intent = Intent(this, BookmarksActivity::class.java)
            startActivity(intent)
            finish()
        }
        navSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Helper function to populate an individual leaderboard entry
    private fun populateLeaderboardEntry(entryView: View, rank: String, playerName: String, levelTag: String) {
        entryView.findViewById<TextView>(R.id.rank_text).text = rank
        entryView.findViewById<TextView>(R.id.player_name_text).text = playerName
        entryView.findViewById<TextView>(R.id.level_tag).text = levelTag
    }
}