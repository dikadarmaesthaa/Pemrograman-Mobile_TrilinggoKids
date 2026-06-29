package com.trilinggo.kids.data.model

import androidx.annotation.DrawableRes

/**
 * Satu kata contoh yang dipecah menjadi suku kata, dengan ilustrasi.
 * Contoh: kata "BABI" -> suku kata ["BA", "BI"], ilustrasi gambar babi.
 */
data class SyllableWord(
    val syllables: List<String>,
    @DrawableRes val illustrationRes: Int
) {
    fun displayText(): String = syllables.joinToString(" - ")
    fun speechText(): String = syllables.joinToString("")
    val fullWord: String get() = syllables.joinToString("")
}

/**
 * Satu konsonan dengan 5 variasi vokal, contoh konsonan "B" -> Ba, Bi, Bu, Be, Bo
 */
data class ConsonantGroup(
    val consonant: String,
    val syllables: List<String> // contoh: ["Ba", "Bi", "Bu", "Be", "Bo"]
)
