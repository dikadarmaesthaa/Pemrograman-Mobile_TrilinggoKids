package com.trilinggo.kids.data.model

import androidx.annotation.DrawableRes

/**
 * Representasi satu soal kuis suku kata.
 * Ada dua tipe soal sesuai desain:
 *  1. AudioQuestion -> dengarkan audio TTS, lalu pilih pasangan suku kata yang benar dari 4 opsi.
 *  2. ImageQuestion  -> lihat gambar + suku kata pertama yang sudah diketahui,
 *                       lalu lengkapi suku kata kedua dari 4 opsi.
 */
sealed class QuizQuestion {
    abstract val options: List<String>
    abstract val correctAnswerIndex: Int

    data class AudioQuestion(
        val correctWord: SyllableWord,
        override val options: List<String>, // contoh: ["DA - DU", "DA - DA", "DU - DA", "DO - DO"]
        override val correctAnswerIndex: Int
    ) : QuizQuestion()

    data class ImageQuestion(
        @DrawableRes val imageRes: Int,
        val knownSyllable: String,   // contoh "Ma"
        override val options: List<String>, // contoh: ["TA", "DU", "MA", "TI"]
        override val correctAnswerIndex: Int
    ) : QuizQuestion()
}
