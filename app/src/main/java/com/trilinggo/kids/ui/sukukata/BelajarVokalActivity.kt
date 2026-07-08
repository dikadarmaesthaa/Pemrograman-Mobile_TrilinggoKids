package com.trilinggo.kids.ui.sukukata

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.databinding.ActivityBelajarVokalBinding
import com.trilinggo.kids.util.IntentExtras
import com.trilinggo.kids.util.TtsHelper

/**
 * Halaman "Belajar Suku Kata" tampilan tablet, menunjukkan 5 suku kata
 * dari satu grup (vokal AIUEO, atau konsonan seperti Ba Bi Bu Be Bo)
 *
 * Mengetuk salah satu huruf akan mengucapkannya lewat TTS.
 * Tombol next mengarah ke halaman contoh kata berilustrasi.
 */
class BelajarVokalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBelajarVokalBinding
    private lateinit var tts: TtsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBelajarVokalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TtsHelper(this)

        val label = intent.getStringExtra(IntentExtras.EXTRA_CONSONANT_LABEL) ?: "Vokal"
        val syllables = intent.getStringArrayListExtra(IntentExtras.EXTRA_CONSONANT_SYLLABLES)
            ?: arrayListOf("A", "I", "U", "E", "O")

        setupTopBar(label)
        populateGrid(syllables)


    }

    private fun setupTopBar(label: String) {
        binding.topBar.tvGreeting.text = getString(R.string.greeting_hi, "Teman")
        binding.topBar.btnNav.setImageResource(R.drawable.ic_back_curved)
        binding.topBar.btnNav.setOnClickListener { finish() }
        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
        binding.tvBelajar2.text = label
    }

    private fun populateGrid(syllables: List<String>) {
        binding.gridLetters.removeAllViews()
        binding.gridLetters.columnCount = 2

        syllables.forEachIndexed { index, syllable ->
            val textView = TextView(this).apply {
                text = syllable
                textSize = 40f
                setTextColor(getColor(R.color.card_pink))
                typeface = android.graphics.Typeface.create("sans-serif-black", android.graphics.Typeface.BOLD)
                gravity = Gravity.CENTER
                setPadding(8, 16, 8, 16)
                setOnClickListener { tts.speak(syllable) }
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(index % 2, 1f)
                rowSpec = GridLayout.spec(index / 2)
            }
            // Suku kata terakhir (ke-5) ditampilkan di tengah, melebar 2 kolom (sesuai desain "O" sendirian)
            if (index == syllables.size - 1 && syllables.size % 2 != 0) {
                params.columnSpec = GridLayout.spec(0, 2, 1f)
            }
            binding.gridLetters.addView(textView, params)
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
