package com.trilinggo.kids.data.local

import android.content.Context
import android.content.SharedPreferences
import com.trilinggo.kids.data.model.User
import com.trilinggo.kids.util.PasswordHasher

/**
 * Mengelola data akun (register/login) dan sesi pengguna yang sedang aktif,
 * semuanya disimpan secara lokal di SharedPreferences (tanpa server/database).
 *
 * Struktur penyimpanan:
 *  - "accounts" prefs -> key = username, value = passwordHash
 *  - "session" prefs   -> key "current_user" = username yang sedang login
 */
class SessionManager(context: Context) {

    private val accountsPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_ACCOUNTS, Context.MODE_PRIVATE)

    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_SESSION, Context.MODE_PRIVATE)

    /** Mendaftarkan akun baru. Mengembalikan false jika username sudah ada. */
    fun register(username: String, rawPassword: String): Boolean {
        val cleanUsername = username.trim()
        if (accountsPrefs.contains(cleanUsername)) return false
        val hash = PasswordHasher.hash(rawPassword)
        accountsPrefs.edit().putString(cleanUsername, hash).apply()
        return true
    }

    /** Memvalidasi login. Jika berhasil, otomatis menyimpan sesi sebagai user aktif. */
    fun login(username: String, rawPassword: String): Boolean {
        val cleanUsername = username.trim()
        val storedHash = accountsPrefs.getString(cleanUsername, null) ?: return false
        val isValid = PasswordHasher.matches(rawPassword, storedHash)
        if (isValid) {
            sessionPrefs.edit().putString(KEY_CURRENT_USER, cleanUsername).apply()
        }
        return isValid
    }

    fun logout() {
        sessionPrefs.edit().remove(KEY_CURRENT_USER).apply()
    }

    fun isLoggedIn(): Boolean = getCurrentUsername() != null

    fun getCurrentUsername(): String? = sessionPrefs.getString(KEY_CURRENT_USER, null)

    fun getCurrentUser(): User? {
        val username = getCurrentUsername() ?: return null
        val hash = accountsPrefs.getString(username, null) ?: return null
        return User(username, hash)
    }

    companion object {
        private const val PREFS_ACCOUNTS = "trilinggo_accounts"
        private const val PREFS_SESSION = "trilinggo_session"
        private const val KEY_CURRENT_USER = "current_user"
    }
}
