package com.trilinggo.kids.ui.kuis

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.databinding.ActivityScoreBinding
import com.trilinggo.kids.ui.home.HomeActivity
import com.trilinggo.kids.util.IntentExtras

/**
 * Halaman "Score Kamu" yang tampil setelah kuis selesai (10 soal).
 * Menampilkan 10 bintang (terisi sesuai jumlah jawaban benar) dan skor
 * dalam format "x/10", sesuai desain referensi.
 *
 * Tombol share membuka share sheet Android bawaan, tombol retry mengulang
 * kuis pada level yang sama.
 */
class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra(IntentExtras.EXTRA_SCORE, 0)
        val total = intent.getIntExtra(IntentExtras.EXTRA_TOTAL, 10)
        val level = intent.getIntExtra(IntentExtras.EXTRA_LEVEL, 1)

        setupTopBar()
        populateStars(score, total)
        binding.tvScoreValue.text = getString(R.string.score_format, score, total)

        binding.btnShare.setOnClickListener { shareScore(score, total) }
        binding.btnRetry.setOnClickListener { retryQuiz(level) }
    }

    private fun setupTopBar() {
        binding.topBar.tvGreeting.text = getString(R.string.greeting_hi, "Teman")
        binding.topBar.btnNav.setImageResource(R.drawable.ic_home)
        binding.topBar.btnNav.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
    }

    private fun populateStars(score: Int, total: Int) {
        binding.starsGrid.removeAllViews()
        binding.starsGrid.columnCount = 5

        for (i in 1..total) {
            val star = ImageView(this).apply {
                setImageResource(if (i <= score) R.drawable.ic_star_filled else R.drawable.ic_star_empty)
            }
            val size = (40 * resources.displayMetrics.density).toInt()
            val params = GridLayout.LayoutParams().apply {
                width = size
                height = size
                setMargins(6, 6, 6, 6)
            }
            binding.starsGrid.addView(star, params)
        }
    }

    private fun shareScore(score: Int, total: Int) {
        val shareText = "Aku mendapat skor $score/$total di Kuis Suku Kata Trilinggo Kids! 🎉"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
    }

    private fun retryQuiz(level: Int) {
        val intent = Intent(this, KuisActivity::class.java).apply {
            putExtra(IntentExtras.EXTRA_LEVEL, level)
        }
        startActivity(intent)
        finish()
    }
}
