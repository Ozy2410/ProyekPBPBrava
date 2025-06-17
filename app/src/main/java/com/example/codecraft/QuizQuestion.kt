package com.example.codecraft

import android.os.Bundle
import android.os.CountDownTimer // Untuk simulasi timer UI
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizQuestionActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI
    private lateinit var closeIcon: ImageView
    private lateinit var questionProgressText: TextView
    private lateinit var questionText: TextView
    private lateinit var timeProgressBar: ProgressBar
    private lateinit var timeValue: TextView

    private lateinit var optionACard: CardView
    private lateinit var optionB: CardView
    private lateinit var optionC: Card lateinit var optionD: CardView

    private lateinit var optionAText: TextView
    private lateinit var optionBText: TextView
    private lateinit var optionCText: TextView
    private lateinit var optionDText: TextView

    private lateinit var answerButton: Button
    private lateinit var audienceButton: Button
    private lateinit var flagButton: Button
    private lateinit var skipButton: Button

    // Data dummy untuk demo UI
    private var currentQuestionIndex = 4 // Asumsi ini pertanyaan ke-5 (indeks 4)
    private val totalQuestions = 15
    private val totalTimePerQuestionMillis: Long = 10000 // 10 detik per pertanyaan untuk demo
    private var countDownTimer: CountDownTimer? = null

    // Struktur data untuk pertanyaan (hanya untuk simulasi UI)
    data class QuizQuestion(
        val question: String,
        val options: List<String>,
        val currentProgress: Int, // e.g., 5
        val totalProgress: Int // e.g., 15
    )

    // Daftar pertanyaan dummy
    private val dummyQuestions = listOf(
        QuizQuestion(
            "Apa itu algoritma?",
            listOf("Resep masakan", "Urutan langkah penyelesaian masalah", "Bahasa pemrograman", "Alat musik"),
            1, 15
        ),
        QuizQuestion(
            "Struktur data yang mengikuti prinsip LIFO adalah...",
            listOf("Queue", "Linked List", "Stack", "Tree"),
            2, 15
        ),
        QuizQuestion(
            "Bahasa pemrograman yang sering digunakan untuk AI adalah?",
            listOf("C++", "Java", "Python", "PHP"),
            3, 15
        ),
        QuizQuestion(
            "Apa kepanjangan dari CPU?",
            listOf("Central Processing Unit", "Computer Personal Unit", "Control Power Unit", "Core Program Utility"),
            4, 15
        ),
        QuizQuestion(
            "Java Adalah bahasa pemrograman yang dikembangkan oleh siapa?",
            listOf("Microsoft", "Google", "Sun Microsystems", "Apple"),
            5, 15 // Ini adalah pertanyaan ke-5 sesuai screenshot
        ),
        QuizQuestion(
            "Bagaimana cara mendeklarasikan variabel di Kotlin?",
            listOf("var atau val", "dim atau set", "let atau const", "create atau new"),
            6, 15
        )
        // Tambahkan lebih banyak pertanyaan dummy hingga 15 jika diperlukan untuk simulasi
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_question)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi elemen UI dengan ID yang ada di XML
        closeIcon = findViewById(R.id.close_icon_quiz)
        questionProgressText = findViewById(R.id.question_progress_text)
        questionText = findViewById(R.id.question_text)
        timeProgressBar = findViewById(R.id.time_progress_bar)
        timeValue = findViewById(R.id.time_value)

        optionACard = findViewById(R.id.option_a_card)
        optionB = findViewById(R.id.option_b_card)
        optionC = findViewById(R.id.option_c_card)
        optionD = findViewById(R.id.option_d_card)

        optionAText = findViewById(R.id.option_a_text)
        optionBText = findViewById(R.id.option_b_text)
        optionCText = findViewById(R.id.option_c_text) // Pastikan ID ini benar di XML
        optionDText = findViewById(R.id.option_d_text) // Pastikan ID ini benar di XML

        answerButton = findViewById(R.id.answer_button)
        audienceButton = findViewById(R.id.audience_button)
        flagButton = findViewById(R.id.flag_button)
        skipButton = findViewById(R.id.skip_button)

        // Muat pertanyaan pertama (sesuai indeks currentQuestionIndex)
        loadQuestion(currentQuestionIndex)

        // Set up click listeners untuk elemen UI (hanya demo Toast)
        closeIcon.setOnClickListener {
            Toast.makeText(this, "Close button clicked (UI Only)", Toast.LENGTH_SHORT).show()
            countDownTimer?.cancel() // Hentikan timer saat keluar
            finish() // Menutup activity
        }

        optionACard.setOnClickListener { handleOptionClick("A") }
        optionB.setOnClickListener { handleOptionClick("B") }
        optionC.setOnClickListener { handleOptionClick("C") }
        optionD.setOnClickListener { handleOptionClick("D") }

        answerButton.setOnClickListener {
            Toast.makeText(this, "Answer button clicked (UI Only)", Toast.LENGTH_SHORT).show()
            // Di sini Anda bisa memicu logika "jawab" atau "lanjutkan"
            // loadNextQuestion() // Contoh: langsung ke pertanyaan berikutnya
        }

        audienceButton.setOnClickListener {
            Toast.makeText(this, "Audience lifeline clicked (UI Only)", Toast.LENGTH_SHORT).show()
            // Implementasi UI untuk lifeline audience
        }

        flagButton.setOnClickListener {
            Toast.makeText(this, "Flag question clicked (UI Only)", Toast.LENGTH_SHORT).show()
            // Implementasi UI untuk flag pertanyaan
        }

        skipButton.setOnClickListener {
            Toast.makeText(this, "Skip question clicked (UI Only)", Toast.LENGTH_SHORT).show()
            // Implementasi UI untuk skip pertanyaan
            loadNextQuestion() // Contoh: langsung ke pertanyaan berikutnya
        }
    }

    // Fungsi untuk memuat data pertanyaan ke UI
    private fun loadQuestion(index: Int) {
        if (index < 0 || index >= dummyQuestions.size) {
            Toast.makeText(this, "Kuis Selesai! (UI Only)", Toast.LENGTH_LONG).show()
            countDownTimer?.cancel()
            finish() // Kembali atau navigasi ke layar hasil
            return
        }

        val questionData = dummyQuestions[index]
        questionProgressText.text = "Question ${questionData.currentProgress}/${questionData.totalProgress}"
        questionText.text = questionData.question

        optionAText.text = questionData.options[0]
        optionBText.text = questionData.options[1]
        optionCText.text = questionData.options[2]
        optionDText.text = questionData.options[3]

        startTimer() // Mulai timer untuk pertanyaan baru
    }

    // Fungsi untuk menangani klik opsi jawaban
    private fun handleOptionClick(optionLabel: String) {
        countDownTimer?.cancel() // Hentikan timer saat opsi dipilih
        Toast.makeText(this, "Option $optionLabel Clicked (UI Only)", Toast.LENGTH_SHORT).show()

        // Di sini Anda bisa mengubah warna latar belakang CardView untuk menandakan pilihan
        // Contoh:
        // when (optionLabel) {
        //     "A" -> optionACard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple_darker))
        //     // ...
        // }

        // Biasanya setelah memilih, akan ada jeda lalu lanjut ke pertanyaan berikutnya
        // Untuk demo UI, langsung ke pertanyaan berikutnya
        // loadNextQuestion()
    }

    // Fungsi untuk memuat pertanyaan berikutnya
    private fun loadNextQuestion() {
        currentQuestionIndex++
        loadQuestion(currentQuestionIndex)
    }

    // Fungsi untuk memulai timer
    private fun startTimer() {
        countDownTimer?.cancel() // Pastikan timer sebelumnya dihentikan
        timeProgressBar.max = totalTimePerQuestionMillis.toInt()
        timeProgressBar.progress = totalTimePerQuestionMillis.toInt()

        countDownTimer = object : CountDownTimer(totalTimePerQuestionMillis, 100) { // Update setiap 100ms
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000.0
                timeValue.text = String.format("%.2f", seconds)
                timeProgressBar.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                timeValue.text = "0.00"
                timeProgressBar.progress = 0
                Toast.makeText(this@QuizQuestionActivity, "Waktu Habis! (UI Only)", Toast.LENGTH_SHORT).show()
                // Otomatis ke pertanyaan berikutnya jika waktu habis
                loadNextQuestion()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel() // Pastikan timer dihentikan saat Activity dihancurkan
    }
}