package com.trilinggo.kids.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.data.local.SessionManager
import com.trilinggo.kids.databinding.ActivityHomeBinding
import com.trilinggo.kids.ui.common.SettingsActivity
import com.trilinggo.kids.ui.huruf.BelajarHurufActivity
import com.trilinggo.kids.ui.kuis.PilihLevelActivity
import com.trilinggo.kids.ui.sukukata.SukuKataMenuActivity

/**
 * Halaman utama setelah login: menampilkan sapaan "Hi {username}!" dan
 * 3 kartu menu utama (Huruf, Suku Kata, Kuis).
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupTopBar()
        setupMenuCards()
    }

    private fun setupTopBar() {
        val username = sessionManager.getCurrentUsername() ?: "Teman"
        binding.topBar.tvGreeting.text = getString(com.trilinggo.kids.R.string.greeting_hi, username)

        // Di Home, ikon kedua adalah Home itu sendiri jadi disembunyikan
        binding.topBar.btnNav.visibility = android.view.View.INVISIBLE

        binding.topBar.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupMenuCards() {
        binding.cardHuruf.setOnClickListener {
            startActivity(Intent(this, BelajarHurufActivity::class.java))
        }
        binding.cardSukuKata.setOnClickListener {
            startActivity(Intent(this, SukuKataMenuActivity::class.java))
        }
        binding.cardKuis.setOnClickListener {
            startActivity(Intent(this, PilihLevelActivity::class.java))
        }
    }
}
