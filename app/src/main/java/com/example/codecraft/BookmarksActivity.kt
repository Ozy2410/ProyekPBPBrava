package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// Data class now includes the topic
data class Bookmark(
    val quizId: String = "",
    val title: String = "",
    val topic: String = "" // <-- Field for the full topic description
)

// Adapter remains the same structurally
class BookmarksAdapter(
    private val bookmarks: MutableList<Bookmark>,
    private val onBookmarkClicked: (Bookmark) -> Unit,
    private val onUnbookmarkClicked: (Bookmark, Int) -> Unit
) : RecyclerView.Adapter<BookmarksAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.bookmark_title_text)
        val unbookmarkButton: ImageButton = view.findViewById(R.id.unbookmark_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bookmark, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.titleTextView.text = bookmark.title
        holder.itemView.contentDescription = "Bookmarked quiz: ${bookmark.title}. Tap to start."

        holder.itemView.setOnClickListener {
            onBookmarkClicked(bookmark)
        }

        holder.unbookmarkButton.setOnClickListener {
            onUnbookmarkClicked(bookmark, position)
        }
    }

    override fun getItemCount() = bookmarks.size
}


class BookmarksActivity : AppCompatActivity() {

    private lateinit var bookmarksRecyclerView: RecyclerView
    private lateinit var emptyBookmarksText: TextView
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var bookmarksAdapter: BookmarksAdapter
    private val bookmarksList = mutableListOf<Bookmark>()

    // Bottom Navigation elements
    private lateinit var navHome: LinearLayout
    private lateinit var navLeaderboard: LinearLayout
    private lateinit var navBookmarks: LinearLayout
    private lateinit var navSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        bookmarksRecyclerView = findViewById(R.id.bookmarks_recycler_view)
        emptyBookmarksText = findViewById(R.id.empty_bookmarks_text)

        setupRecyclerView()
        fetchBookmarks()
        setupNavigation() // This function call is now valid
    }

    private fun setupRecyclerView() {
        bookmarksAdapter = BookmarksAdapter(
            bookmarksList,
            onBookmarkClicked = { bookmark ->
                startQuizForBookmark(bookmark)
            },
            onUnbookmarkClicked = { bookmark, position ->
                unbookmarkQuiz(bookmark, position)
            }
        )
        bookmarksRecyclerView.layoutManager = LinearLayoutManager(this)
        bookmarksRecyclerView.adapter = bookmarksAdapter
    }

    private fun startQuizForBookmark(bookmark: Bookmark) {
        if (bookmark.topic.isNotBlank()) {
            val intent = Intent(this, QuizQuestion::class.java).apply {
                putExtra("QUIZ_TOPIC", bookmark.topic)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "This bookmarked quiz is outdated. Please re-bookmark it.", Toast.LENGTH_LONG).show()
        }
    }

    private fun unbookmarkQuiz(bookmark: Bookmark, position: Int) {
        val currentUser = auth.currentUser ?: return

        firestore.collection("users").document(currentUser.uid)
            .collection("bookmarks").document(bookmark.quizId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "'${bookmark.title}' Unbookmarked!", Toast.LENGTH_SHORT).show()
                bookmarksList.removeAt(position)
                bookmarksAdapter.notifyItemRemoved(position)
                bookmarksAdapter.notifyItemRangeChanged(position, bookmarksList.size)
                checkEmptyState()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchBookmarks() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to view bookmarks", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        firestore.collection("users").document(currentUser.uid)
            .collection("bookmarks")
            .orderBy("bookmarkedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                bookmarksList.clear()
                bookmarksList.addAll(documents.toObjects(Bookmark::class.java))
                bookmarksAdapter.notifyDataSetChanged()
                checkEmptyState()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching bookmarks: ${e.message}", Toast.LENGTH_SHORT).show()
                checkEmptyState()
            }
    }

    private fun checkEmptyState() {
        if (bookmarksList.isEmpty()) {
            emptyBookmarksText.visibility = View.VISIBLE
            bookmarksRecyclerView.visibility = View.GONE
        } else {
            emptyBookmarksText.visibility = View.GONE
            bookmarksRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupNavigation() {
        navHome = findViewById(R.id.nav_home)
        navLeaderboard = findViewById(R.id.nav_leaderboard)
        navBookmarks = findViewById(R.id.nav_bookmarks)
        navSettings = findViewById(R.id.nav_settings)

        navHome.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        navLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        navBookmarks.setOnClickListener {
            Toast.makeText(this, "You are already on the Bookmarks screen", Toast.LENGTH_SHORT).show()
        }

        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}