package com.trilinggo.kids.ui.kuis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.databinding.ActivityPilihLevelBinding
import com.trilinggo.kids.util.IntentExtras

/**
 * Halaman "Pilih Level" untuk kuis suku kata. Ada 3 level:
 *  - Level 1: soal tipe dengarkan audio
 *  - Level 2: soal tipe lihat gambar + lengkapi suku kata
 *  - Level 3: campuran kedua tipe (lebih menantang)
 */
class PilihLevelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPilihLevelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopBar()

        binding.cardLevel1.setOnClickListener { startQuiz(2) }
        binding.cardLevel2.setOnClickListener { startQuiz(1) }
    }

    private fun setupTopBar() {
        binding.topBar.tvGreeting.text = getString(R.string.greeting_hi, "Teman")
        binding.topBar.btnNav.setImageResource(R.drawable.ic_home)
        binding.topBar.btnNav.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.home.HomeActivity::class.java))
            finish()
        }
        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
    }

    private fun startQuiz(level: Int) {
        val intent = Intent(this, KuisActivity::class.java).apply {
            putExtra(IntentExtras.EXTRA_LEVEL, level)
        }
        startActivity(intent)
    }
}
