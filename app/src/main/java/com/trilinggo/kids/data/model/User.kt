package com.trilinggo.kids.data.model

/**
 * Merepresentasikan satu akun pengguna yang disimpan secara lokal.
 * Password disimpan dalam bentuk hash sederhana (lihat PasswordHasher),
 * BUKAN plain text, walau ini tetap bukan praktik keamanan tingkat produksi.
 */
data class User(
    val username: String,
    val passwordHash: String
)
