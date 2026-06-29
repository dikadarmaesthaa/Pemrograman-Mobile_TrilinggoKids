package com.trilinggo.kids.data.local

import android.content.Context

/**
 * Menyimpan skor terbaik kuis per level secara lokal.
 */
class ScoreManager(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_SCORES, Context.MODE_PRIVATE)

    fun saveBestScore(level: Int, score: Int, total: Int) {
        val key = keyFor(level)
        val best = prefs.getInt(key, -1)
        if (score > best) {
            prefs.edit().putInt(key, score).putInt(totalKeyFor(level), total).apply()
        }
    }

    fun getBestScore(level: Int): Int = prefs.getInt(keyFor(level), 0)

    private fun keyFor(level: Int) = "best_score_level_$level"
    private fun totalKeyFor(level: Int) = "total_level_$level"

    companion object {
        private const val PREFS_SCORES = "trilinggo_scores"
    }
}
