package com.example.codecraft

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizSelectionActivity : AppCompatActivity() {

    // Deklarasi variabel untuk elemen UI
    private lateinit var closeIcon: ImageView
    private lateinit var questionProgressTitle: TextView
    private lateinit var questionGridLayout: GridLayout
    private lateinit var timeProgressBar: ProgressBar
    private lateinit var timeValue: TextView

    // Tombol aksi bawah (sama seperti sebelumnya)
    private lateinit var answerButton: Button
    private lateinit var audienceButton: Button
    private lateinit var flagButton: Button
    private lateinit var skipButton: Button

    // Data dummy untuk demo UI
    private val totalQuestions = 15
    private var currentQuestionNumber = 5 // Sesuai screenshot
    private val totalTimePerQuestionMillis: Long = 10000 // 10 detik per pertanyaan untuk demo
    private var countDownTimer: CountDownTimer? = null

    // Status dummy untuk setiap pertanyaan (0=belum dikerjakan, 1=dikerjakan, 2=aktif)
    private val questionStatus = MutableList(totalQuestions) { 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi elemen UI
        closeIcon = findViewById(R.id.close_icon_quiz_selection)
        questionProgressTitle = findViewById(R.id.question_progress_title)
        questionGridLayout = findViewById(R.id.question_grid_layout)
        timeProgressBar = findViewById(R.id.time_progress_bar_quiz_selection)
        timeValue = findViewById(R.id.time_value_quiz_selection)

        answerButton = findViewById(R.id.answer_button_quiz_selection)
        audienceButton = findViewById(R.id.audience_button_quiz_selection)
        flagButton = findViewById(R.id.flag_button_quiz_selection)
        skipButton = findViewById(R.id.skip_button_quiz_selection)

        // Set up click listeners
        closeIcon.setOnClickListener {
            Toast.makeText(this, "Close button clicked (UI Only)", Toast.LENGTH_SHORT).show()
            countDownTimer?.cancel()
            finish()
        }

        answerButton.setOnClickListener {
            Toast.makeText(this, "Answer button clicked (UI Only)", Toast.LENGTH_SHORT).show()
        }
        audienceButton.setOnClickListener {
            Toast.makeText(this, "Audience lifeline clicked (UI Only)", Toast.LENGTH_SHORT).show()
        }
        flagButton.setOnClickListener {
            Toast.makeText(this, "Flag question clicked (UI Only)", Toast.LENGTH_SHORT).show()
        }
        skipButton.setOnClickListener {
            Toast.makeText(this, "Skip question clicked (UI Only)", Toast.LENGTH_SHORT).show()
            // Simulasikan pindah ke pertanyaan berikutnya
            currentQuestionNumber = (currentQuestionNumber % totalQuestions) + 1
            updateUI()
        }

        // Inisialisasi status dummy untuk beberapa pertanyaan
        questionStatus[0] = 1 // Pertanyaan 1 sudah dijawab
        questionStatus[1] = 1 // Pertanyaan 2 sudah dijawab
        questionStatus[4] = 2 // Pertanyaan 5 adalah pertanyaan aktif saat ini (indeks 4)

        // Update UI awal
        updateUI()
        startTimer() // Mulai timer
    }

    private fun updateUI() {
        questionProgressTitle.text = "Question $currentQuestionNumber/$totalQuestions"
        populateQuestionGrid()
    }

    private fun populateQuestionGrid() {
        questionGridLayout.removeAllViews() // Bersihkan grid sebelumnya

        val columnCount = questionGridLayout.columnCount
        val rowCount = (totalQuestions + columnCount - 1) / columnCount // Hitung jumlah baris yang dibutuhkan

        questionGridLayout.rowCount = rowCount // Sesuaikan jumlah baris

        for (i in 1..totalQuestions) {
            val button = Button(this, null, 0, R.style.QuestionGridButtonStyle)
            button.text = i.toString()

            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT // Atau 0 jika ingin square dengan ratio
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // 1f agar proporsional
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)

            // Calculate margin dynamically based on screen density if needed, or use fixed dp
            val margin = (resources.displayMetrics.density * 8).toInt() // 8dp margin
            params.setMargins(margin, margin, margin, margin)


            // Set warna berdasarkan status pertanyaan
            when (questionStatus[i - 1]) { // i-1 karena list 0-indexed
                1 -> { // Sudah dijawab
                    button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.answered_question_color)
                    button.setTextColor(ContextCompat.getColor(this, android.R.color.white)) // Atau warna lain
                }
                2 -> { // Pertanyaan aktif saat ini
                    button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.current_question_color)
                    button.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                }
                else -> { // Belum dikerjakan
                    button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_grey_question_box) // Warna abu-abu terang default
                    button.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                }
            }

            // Atur agar tombol nomor 5 menggunakan background ungu dari screenshot
            if (i == 5) {
                button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.purple_darker)
                button.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            }


            button.setOnClickListener {
                Toast.makeText(this, "Question $i Clicked! (UI Only)", Toast.LENGTH_SHORT).show()
                // Di sini Anda bisa mengarahkan pengguna ke QuizQuestionActivity
                // dengan membawa data pertanyaan ke-i
                // val intent = Intent(this, QuizQuestionActivity::class.java)
                // intent.putExtra("question_number", i)
                // startActivity(intent)
            }
            questionGridLayout.addView(button, params)
        }
    }


    private fun startTimer() {
        countDownTimer?.cancel()
        timeProgressBar.max = totalTimePerQuestionMillis.toInt()
        timeProgressBar.progress = totalTimePerQuestionMillis.toInt()

        countDownTimer = object : CountDownTimer(totalTimePerQuestionMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000.0
                timeValue.text = String.format("%.2f", seconds)
                timeProgressBar.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                timeValue.text = "0.00"
                timeProgressBar.progress = 0
                Toast.makeText(this@QuizSelectionActivity, "Time's up! (UI Only)", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}