package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// Data class to hold user data for the leaderboard
data class LeaderboardUser(
    val displayName: String = "",
    val score: Long = 0,
    val level: String = "Beginner"
)

// Adapter for the RecyclerView
class LeaderboardAdapter(private val userList: List<LeaderboardUser>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankText: TextView = view.findViewById(R.id.rank_text)
        val playerNameText: TextView = view.findViewById(R.id.player_name_text)
        val levelTag: TextView = view.findViewById(R.id.level_tag)
        val scoreText: TextView = view.findViewById(R.id.player_score_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.rankText.text = "#${position + 1}"
        holder.playerNameText.text = user.displayName
        holder.levelTag.text = user.level
        holder.scoreText.text = user.score.toString()
    }

    override fun getItemCount() = userList.size
}


class LeaderboardActivity : AppCompatActivity() {

    private lateinit var closeIcon: ImageView
    private lateinit var navHome: LinearLayout
    private lateinit var navLeaderboard: LinearLayout
    private lateinit var navBookmarks: LinearLayout
    private lateinit var navSettings: LinearLayout

    private lateinit var leaderboardRecyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        closeIcon = findViewById(R.id.close_icon_leaderboard)
        navHome = findViewById(R.id.nav_home)
        navLeaderboard = findViewById(R.id.nav_leaderboard)
        navBookmarks = findViewById(R.id.nav_bookmarks)
        navSettings = findViewById(R.id.nav_settings)

        leaderboardRecyclerView = findViewById(R.id.leaderboard_recycler_view)
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(this)

        setupNavigation()
        fetchLeaderboardData()
    }

    private fun fetchLeaderboardData() {
        firestore.collection("users")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(50) // Limit to top 50 users
            .get()
            .addOnSuccessListener { documents ->
                val userList = documents.toObjects(LeaderboardUser::class.java)
                leaderboardRecyclerView.adapter = LeaderboardAdapter(userList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting leaderboard data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupNavigation() {
        closeIcon.setOnClickListener { finish() }

        navHome.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        navLeaderboard.setOnClickListener {
            Toast.makeText(this, "Already on Leaderboard", Toast.LENGTH_SHORT).show()
        }
        navBookmarks.setOnClickListener {
            startActivity(Intent(this, BookmarksActivity::class.java))
            finish()
        }
        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }
}