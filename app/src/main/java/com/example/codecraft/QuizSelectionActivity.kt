package com.example.codecraft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuizSelectionActivity : AppCompatActivity() {

    private lateinit var quizTopicsRecyclerView: RecyclerView
    private val quizTopics = listOf(
        QuizTopic("Java Integers", "A quiz on Java's integer types."),
        QuizTopic("Java Conditionals", "Test your knowledge of if-else statements."),
        QuizTopic("Java Booleans", "A quiz on boolean expressions and logic."),
        QuizTopic("Java Inheritance", "A quiz on inheritance and polymorphism.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_menu)

        quizTopicsRecyclerView = findViewById(R.id.quiz_topics_recycler_view)
        quizTopicsRecyclerView.layoutManager = LinearLayoutManager(this)
        quizTopicsRecyclerView.adapter = QuizTopicAdapter(quizTopics) { topic ->
            val intent = Intent(this, QuizQuestion::class.java).apply {
                putExtra("QUIZ_TOPIC", topic.title)
            }
            startActivity(intent)
        }
    }
}

data class QuizTopic(val title: String, val description: String)

class QuizTopicAdapter(
    private val topics: List<QuizTopic>,
    private val onClick: (QuizTopic) -> Unit
) : RecyclerView.Adapter<QuizTopicAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.quiz_topic_title)
        val description: TextView = view.findViewById(R.id.quiz_topic_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.title.text = topic.title
        holder.description.text = topic.description
        holder.itemView.setOnClickListener { onClick(topic) }
    }

    override fun getItemCount() = topics.size
}