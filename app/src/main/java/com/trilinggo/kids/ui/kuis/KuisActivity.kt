package com.trilinggo.kids.ui.kuis

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.data.local.ScoreManager
import com.trilinggo.kids.data.model.QuizQuestion
import com.trilinggo.kids.data.repository.LearningRepository
import com.trilinggo.kids.databinding.ActivityKuisBinding
import com.trilinggo.kids.util.IntentExtras
import com.trilinggo.kids.util.TtsHelper

/**
 * Halaman kuis suku kata. Menangani dua tipe soal:
 *  1. AudioQuestion: tombol mic mengucapkan suku kata, pengguna pilih jawaban dari 4 opsi teks.
 *  2. ImageQuestion: gambar + suku kata pertama ditampilkan, pengguna lengkapi suku kata kedua.
 *
 * Setelah memilih jawaban, kartu jawaban berubah warna (oranye saat dipilih),
 * lalu muncul dialog feedback "Jawaban Kamu Benar/Salah" sesuai desain referensi.
 * Setelah 10 soal, pindah ke ScoreActivity.
 */
class KuisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKuisBinding
    private lateinit var tts: TtsHelper
    private lateinit var scoreManager: ScoreManager

    private var level = 1
    private lateinit var questions: List<QuizQuestion>
    private var currentQuestionIndex = 0
    private var score = 0
    private var hasAnswered = false

    private val answerButtons by lazy {
        listOf(binding.btnAnswer0, binding.btnAnswer1, binding.btnAnswer2, binding.btnAnswer3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TtsHelper(this)
        scoreManager = ScoreManager(this)

        level = intent.getIntExtra(IntentExtras.EXTRA_LEVEL, 1)
        questions = LearningRepository.generateQuiz(level)

        setupTopBar()
        showQuestion(currentQuestionIndex)

        binding.btnMicQuestion.setOnClickListener {
            val question = questions[currentQuestionIndex]
            if (question is QuizQuestion.AudioQuestion) {
                tts.speak(question.correctWord.speechText())
            }
        }

        binding.btnNextQuestion.setOnClickListener { goToNextQuestion() }
        binding.btnFeedbackNext.setOnClickListener { goToNextQuestion() }
    }

    private fun setupTopBar() {
        binding.topBar.tvGreeting.text = getString(R.string.greeting_hi, "Teman")
        binding.topBar.btnNav.setImageResource(R.drawable.ic_back_curved)
        binding.topBar.btnNav.setOnClickListener { finish() }
        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
    }

    private fun showQuestion(index: Int) {
        hasAnswered = false
        resetAnswerButtons()
        binding.btnNextQuestion.visibility = View.INVISIBLE
        hideFeedback()

        val question = questions[index]
        when (question) {
            is QuizQuestion.AudioQuestion -> showAudioQuestion(question)
            is QuizQuestion.ImageQuestion -> showImageQuestion(question)
        }

        bindAnswerOptions(question.options) { selectedIndex ->
            onAnswerSelected(selectedIndex, question.correctAnswerIndex)
        }
    }

    private fun showAudioQuestion(@Suppress("UNUSED_PARAMETER") question: QuizQuestion.AudioQuestion) {
        binding.btnMicQuestion.visibility = View.VISIBLE
        binding.tvDengarkan.visibility = View.VISIBLE
        binding.ivQuestionImage.visibility = View.GONE
        binding.partialSyllableRow.visibility = View.GONE
    }

    private fun showImageQuestion(question: QuizQuestion.ImageQuestion) {
        binding.btnMicQuestion.visibility = View.GONE
        binding.tvDengarkan.visibility = View.GONE
        binding.ivQuestionImage.visibility = View.VISIBLE
        binding.ivQuestionImage.setImageResource(question.imageRes)
        binding.partialSyllableRow.visibility = View.VISIBLE
        binding.tvKnownSyllable.text = question.knownSyllable
        binding.tvBlankSyllable.text = "•••"
    }

    private fun bindAnswerOptions(options: List<String>, onSelected: (Int) -> Unit) {
        answerButtons.forEachIndexed { index, button ->
            button.text = options.getOrElse(index) { "" }
            button.setBackgroundResource(R.drawable.bg_answer_default)
            button.setOnClickListener { onSelected(index) }
        }
    }

    private fun resetAnswerButtons() {
        answerButtons.forEach { it.setBackgroundResource(R.drawable.bg_answer_default) }
    }

    private fun onAnswerSelected(selectedIndex: Int, correctIndex: Int) {
        if (hasAnswered) return
        hasAnswered = true

        val isCorrect = selectedIndex == correctIndex
        if (isCorrect) score++

        // Tandai jawaban yang dipilih: oranye jika sedang dievaluasi,
        // lalu langsung tampilkan warna benar/salah sesuai desain referensi.
        answerButtons[selectedIndex].setBackgroundResource(
            if (isCorrect) R.drawable.bg_answer_correct else R.drawable.bg_answer_wrong
        )
        if (!isCorrect) {
            answerButtons[correctIndex].setBackgroundResource(R.drawable.bg_answer_correct)
        }
        answerButtons.forEach { it.isClickable = false }

        showFeedback(isCorrect)
        binding.btnNextQuestion.visibility = View.VISIBLE
    }

    private fun showFeedback(isCorrect: Boolean) {
        binding.dimOverlay.visibility = View.VISIBLE
        binding.feedbackCard.visibility = View.VISIBLE
        binding.ivFeedbackIcon.setImageResource(
            if (isCorrect) R.drawable.ic_correct_circle else R.drawable.ic_wrong_circle
        )
        binding.tvFeedbackText.text = getString(
            if (isCorrect) R.string.jawaban_benar else R.string.jawaban_salah
        )
    }

    private fun hideFeedback() {
        binding.dimOverlay.visibility = View.GONE
        binding.feedbackCard.visibility = View.GONE
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion(currentQuestionIndex)
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        scoreManager.saveBestScore(level, score, questions.size)
        val intent = Intent(this, ScoreActivity::class.java).apply {
            putExtra(IntentExtras.EXTRA_SCORE, score)
            putExtra(IntentExtras.EXTRA_TOTAL, questions.size)
            putExtra(IntentExtras.EXTRA_LEVEL, level)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
