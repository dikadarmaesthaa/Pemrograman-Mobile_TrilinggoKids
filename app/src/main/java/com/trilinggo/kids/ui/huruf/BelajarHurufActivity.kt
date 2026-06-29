package com.trilinggo.kids.ui.huruf

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.data.repository.LearningRepository
import com.trilinggo.kids.databinding.ActivityBelajarHurufBinding
import com.trilinggo.kids.ui.home.HomeActivity
import com.trilinggo.kids.util.TtsHelper

/**
 * Halaman "Belajar Huruf": menampilkan flashcard huruf A-Z (besar & kecil).
 * Tombol mic akan mengucapkan huruf yang sedang ditampilkan menggunakan TTS.
 * Tombol kiri/kanan untuk berpindah antar huruf.
 */
class BelajarHurufActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBelajarHurufBinding
    private lateinit var tts: TtsHelper
    private val letters = LearningRepository.getAllLetters()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBelajarHurufBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TtsHelper(this)

        setupTopBar()
        showLetter(currentIndex)

        binding.btnMic.setOnClickListener {
            tts.speak(letters[currentIndex].speechText())
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showLetter(currentIndex)
            }
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < letters.size - 1) {
                currentIndex++
                showLetter(currentIndex)
            }
        }
    }

    private fun setupTopBar() {
        binding.topBar.tvGreeting.text = getString(R.string.greeting_hi, "Teman")
        binding.topBar.btnNav.setImageResource(R.drawable.ic_back_curved)
        binding.topBar.btnNav.setOnClickListener { finish() }
        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
    }

    private fun showLetter(index: Int) {
        val card = letters[index]
        binding.tvLetterUpper.text = card.upper
        binding.tvLetterLower.text = card.lower

        // Tombol prev disembunyikan di huruf pertama, agar sesuai desain awal (huruf A: hanya next)
        binding.btnPrev.visibility = if (index == 0) android.view.View.INVISIBLE else android.view.View.VISIBLE
        binding.btnNext.visibility = if (index == letters.size - 1) android.view.View.INVISIBLE else android.view.View.VISIBLE
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
