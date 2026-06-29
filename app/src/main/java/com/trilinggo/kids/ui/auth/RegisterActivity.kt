package com.trilinggo.kids.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.data.local.SessionManager
import com.trilinggo.kids.databinding.ActivityRegisterBinding

/**
 * Halaman pendaftaran akun baru. Validasi dilakukan secara lokal:
 *  - Username & password tidak boleh kosong
 *  - Password & konfirmasi password harus sama
 *  - Username belum terdaftar sebelumnya
 *
 * Data akun disimpan di SharedPreferences melalui SessionManager (lihat
 * data/local/SessionManager.kt), TANPA server/database eksternal.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnDaftar.setOnClickListener { handleRegister() }

        binding.tvGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleRegister() {
        val username = binding.etUsername.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()
        val confirmPassword = binding.etConfirmPassword.text?.toString().orEmpty()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            toast(getString(R.string.error_field_required))
            return
        }

        if (password != confirmPassword) {
            toast(getString(R.string.error_password_mismatch))
            return
        }

        val success = sessionManager.register(username, password)
        if (!success) {
            toast(getString(R.string.error_user_exists))
            return
        }

        toast(getString(R.string.success_register))
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
