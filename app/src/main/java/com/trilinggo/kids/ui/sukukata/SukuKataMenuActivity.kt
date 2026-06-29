package com.trilinggo.kids.ui.sukukata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.trilinggo.kids.R
import com.trilinggo.kids.data.model.ConsonantGroup
import com.trilinggo.kids.data.repository.LearningRepository
import com.trilinggo.kids.databinding.ActivitySukuKataMenuBinding
import com.trilinggo.kids.util.IntentExtras

/**
 * Halaman menu "Belajar Suku Kata": menampilkan grid pilihan "Vokal" (AIUEO)
 * dan semua konsonan B-Z. Setiap kartu yang diklik mengarah ke
 * BelajarVokalActivity (tampilan tablet berisi 5 suku kata grup tersebut).
 *
 * Materi kata-contoh berilustrasi (BA-BI, BA-TU, dst) ditampilkan terpisah
 * lewat tombol "Contoh Kata" -> BelajarSukuKataActivity.
 */
class SukuKataMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySukuKataMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySukuKataMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopBar()
        setupGrid()
    }

    private fun setupTopBar() {
        binding.topBar.tvGreeting.text = getString(R.string.greeting_hi, "Teman")
        binding.topBar.btnNav.setImageResource(R.drawable.ic_back_curved)
        binding.topBar.btnNav.setOnClickListener { finish() }
        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
    }

    private fun setupGrid() {
        val allGroups = mutableListOf(LearningRepository.getVokalGroup())
        allGroups.addAll(LearningRepository.getAllConsonantGroups())

        binding.rvKonsonan.layoutManager = GridLayoutManager(this, 3)
        binding.rvKonsonan.adapter = ConsonantAdapter(allGroups) { group ->
            openGroup(group)
        }
    }

    private fun openGroup(group: ConsonantGroup) {
        val intent = Intent(this, BelajarVokalActivity::class.java).apply {
            putExtra(IntentExtras.EXTRA_CONSONANT_LABEL, group.consonant)
            putStringArrayListExtra(IntentExtras.EXTRA_CONSONANT_SYLLABLES, ArrayList(group.syllables))
        }
        startActivity(intent)
    }
}
