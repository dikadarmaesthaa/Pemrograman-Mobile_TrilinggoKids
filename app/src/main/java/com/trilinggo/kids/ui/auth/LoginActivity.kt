package com.trilinggo.kids.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.trilinggo.kids.R
import com.trilinggo.kids.data.local.SessionManager
import com.trilinggo.kids.databinding.ActivityLoginBinding
import com.trilinggo.kids.ui.home.HomeActivity

/**
 * Halaman login. Memvalidasi username & password terhadap data yang
 * tersimpan di SharedPreferences (lihat SessionManager). Jika berhasil,
 * sesi disimpan dan pengguna diarahkan ke HomeActivity.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnMasuk.setOnClickListener { handleLogin() }

        binding.tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun handleLogin() {
        val username = binding.etUsername.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        if (username.isEmpty() || password.isEmpty()) {
            toast(getString(R.string.error_field_required))
            return
        }

        val success = sessionManager.login(username, password)
        if (!success) {
            toast(getString(R.string.error_login_failed))
            return
        }

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
