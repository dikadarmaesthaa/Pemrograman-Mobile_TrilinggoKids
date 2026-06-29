package com.trilinggo.kids.ui.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.data.local.SessionManager
import com.trilinggo.kids.databinding.ActivitySettingsBinding
import com.trilinggo.kids.ui.splash.SplashActivity

/**
 * Halaman pengaturan sederhana. Menampilkan username yang sedang login
 * dan tombol logout yang akan menghapus sesi lalu kembali ke SplashActivity.
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnBack.setOnClickListener { finish() }

        val username = sessionManager.getCurrentUsername()
        binding.tvUsernameLabel.text = if (username != null) {
            "Login sebagai: $username"
        } else {
            "Belum login"
        }
        binding.btnLogout.visibility = if (username != null) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(this, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
