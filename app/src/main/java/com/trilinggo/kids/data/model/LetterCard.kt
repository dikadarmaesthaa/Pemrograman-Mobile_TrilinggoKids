package com.trilinggo.kids.data.model

/**
 * Satu kartu huruf, contoh: huruf = 'A', berisi versi besar dan kecil.
 */
data class LetterCard(
    val upper: String,
    val lower: String
) {
    /** Teks yang akan diucapkan oleh TextToSpeech, contoh "A" */
    fun speechText(): String = upper
}
