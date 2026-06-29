package com.trilinggo.kids.util

import java.security.MessageDigest

/**
 * Hashing password sederhana menggunakan SHA-256.
 * Catatan untuk tugas kuliah: ini lebih aman daripada menyimpan plain text,
 * namun untuk aplikasi produksi sungguhan sebaiknya gunakan salt + algoritma
 * khusus password seperti bcrypt/Argon2 (biasanya lewat backend, bukan di client).
 */
object PasswordHasher {

    fun hash(rawPassword: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(rawPassword.trim().toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun matches(rawPassword: String, hash: String): Boolean {
        return hash(rawPassword) == hash
    }
}
