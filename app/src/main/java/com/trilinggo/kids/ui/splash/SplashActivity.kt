package com.trilinggo.kids.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.data.local.SessionManager
import com.trilinggo.kids.databinding.ActivitySplashBinding
import com.trilinggo.kids.ui.auth.LoginActivity
import com.trilinggo.kids.ui.auth.RegisterActivity
import com.trilinggo.kids.ui.home.HomeActivity

/**
 * Halaman pembuka aplikasi (welcome screen). Menampilkan judul "Trilinggo Kids"
 * beserta tombol Sign In (mendaftar) dan Login (masuk).
 *
 * Jika pengguna sudah pernah login sebelumnya (sesi tersimpan di SharedPreferences),
 * langsung diarahkan ke HomeActivity tanpa perlu login ulang.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            goToHome()
            return
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.trilinggo.kids.ui.common.SettingsActivity::class.java))
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
