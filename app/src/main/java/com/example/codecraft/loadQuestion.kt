// Inside QuizQuestionActivity or a ViewModel
// This is pseudo-code for the Gemini API call
// You'd need actual API client setup (e.g., Retrofit, Ktor) and API key handling

fun fetchQuestionFromGemini(topic: String, difficulty: String) {
    // Show loading spinner

    // Example Gemini request structure (this would be built by your API client)
    val prompt = "Generate a multiple-choice question about $topic for an $difficulty level, with 4 options and one correct answer. Format as JSON: {\"question\": \"\", \"options\": [], \"correctAnswer\": \"\"}."

    // Dummy async call - replace with actual API call
    Thread {
        try {
            // Simulate network delay
            Thread.sleep(2000)
            // Simulate Gemini response
            val geminiResponseJson = """
                {
                    "question": "Apa fungsi utama dari Garbage Collector di Java?",
                    "options": [
                        "Mengelola memori secara manual",
                        "Menghapus objek yang tidak lagi digunakan",
                        "Membuat instance objek baru",
                        "Melakukan kompilasi kode Java"
                    ],
                    "correctAnswer": "Menghapus objek yang tidak lagi digunakan"
                }
            """.trimIndent()

            // Parse JSON (e.g., using Gson or kotlinx.serialization)
            // val quizQuestion = Gson().fromJson(geminiResponseJson, QuizQuestion::class.java)

            // Update UI on main thread
            runOnUiThread {
                // For demonstration, let's just set the question.
                // In a real app, you'd load the full QuizQuestion object.
                questionText.text = "Apa fungsi utama dari Garbage Collector di Java?"
                optionAText.text = "Mengelola memori secara manual"
                optionBText.text = "Menghapus objek yang tidak lagi digunakan"
                optionCText.text = "Membuat instance objek baru"
                optionDText.text = "Melakukan kompilasi kode Java"
                // Hide loading spinner
            }
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(this, "Failed to load question: ${e.message}", Toast.LENGTH_LONG).show()
                // Hide loading spinner
            }
        }
    }.start()
}

// Call this function when you need a new question:
// fetchQuestionFromGemini("Java", "intermediate")