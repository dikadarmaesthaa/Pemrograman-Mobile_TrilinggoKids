package com.trilinggo.kids.ui.sukukata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.data.repository.LearningRepository
import com.trilinggo.kids.databinding.ActivityBelajarSukuKataBinding
import com.trilinggo.kids.util.TtsHelper

/**
 * Halaman "Belajar Suku Kata" dengan ilustrasi per kata, contoh:
 * gambar babi + teks "BA - BI", gambar dadu + teks "DA - DU", dst.
 * Sesuai dengan contoh pada desain referensi Canva.
 */
class BelajarSukuKataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBelajarSukuKataBinding
    private lateinit var tts: TtsHelper
    private val words = LearningRepository.getAllSyllableWords()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBelajarSukuKataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TtsHelper(this)

        setupTopBar()
        showWord(currentIndex)

        binding.btnMic.setOnClickListener {
            tts.speak(words[currentIndex].speechText())
        }

        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showWord(currentIndex)
            }
        }

        binding.btnNext.setOnClickListener {
            if (currentIndex < words.size - 1) {
                currentIndex++
                showWord(currentIndex)
            } else {
                finish()
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

    private fun showWord(index: Int) {
        val word = words[index]
        binding.ivIllustration.setImageResource(word.illustrationRes)
        binding.tvSyllable.text = word.displayText()

        binding.btnPrev.visibility = if (index == 0) android.view.View.INVISIBLE else android.view.View.VISIBLE
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
