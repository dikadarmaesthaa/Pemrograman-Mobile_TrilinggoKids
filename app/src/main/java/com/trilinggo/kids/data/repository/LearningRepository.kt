package com.trilinggo.kids.data.repository

import com.trilinggo.kids.R
import com.trilinggo.kids.data.model.ConsonantGroup
import com.trilinggo.kids.data.model.LetterCard
import com.trilinggo.kids.data.model.QuizQuestion
import com.trilinggo.kids.data.model.SyllableWord

/**
 * Sumber data statis untuk semua materi belajar: huruf A-Z, suku kata per
 * konsonan, kata contoh dengan ilustrasi, dan generator soal kuis acak per level.
 *
 * Di project nyata, data sebesar ini biasanya datang dari database (Room) atau
 * API. Untuk tugas ini, data statis di Kotlin sudah cukup dan lebih sederhana
 * untuk dipahami serta dinilai oleh dosen.
 */
object LearningRepository {

    private val vokal = listOf("a", "i", "u", "e", "o")

    /** 26 kartu huruf A-Z, masing-masing versi besar & kecil. */
    fun getAllLetters(): List<LetterCard> =
        ('A'..'Z').map { LetterCard(upper = it.toString(), lower = it.lowercaseChar().toString()) }

    /** Vokal A I U E O ditampilkan sebagai satu grup khusus (tanpa konsonan). */
    fun getVokalGroup(): ConsonantGroup =
        ConsonantGroup(consonant = "Vokal", syllables = listOf("A", "I", "U", "E", "O"))

    /** Semua grup konsonan asli Bahasa Indonesia (tanpa vokal), masing-masing x5 suku kata. */
    fun getAllConsonantGroups(): List<ConsonantGroup> {
        val vowelChars = setOf('A', 'I', 'U', 'E', 'O')
        val consonants = ('B'..'Z').filter { it !in vowelChars }
        return consonants.map { c ->
            val syllables = vokal.map { v -> "${c}${v}" }
                .map { it[0].uppercaseChar() + it.substring(1) } // Capitalize: "Ba"
            ConsonantGroup(consonant = c.toString(), syllables = syllables)
        }
    }

    /**
     * Kata-kata contoh dengan ilustrasi gambar, dipakai di halaman
     * "Belajar Suku Kata" (per kata) maupun sumber soal ImageQuestion di kuis.
     */
    fun getAllSyllableWords(): List<SyllableWord> = listOf(
        SyllableWord(listOf("A", "KU"), R.drawable.ic_word_aku),
        SyllableWord(listOf("BA", "BI"), R.drawable.ic_word_babi),
        SyllableWord(listOf("BA", "TU"), R.drawable.ic_word_batu),
        SyllableWord(listOf("DA", "DU"), R.drawable.ic_word_dadu),
        SyllableWord(listOf("MA", "TA"), R.drawable.ic_word_mata),
        SyllableWord(listOf("BO", "LA"), R.drawable.ic_word_aku),
        SyllableWord(listOf("BU", "KU"), R.drawable.ic_word_batu),
        SyllableWord(listOf("BA", "TA"), R.drawable.ic_word_aku),
        SyllableWord(listOf("GI", "GI"), R.drawable.ic_word_babi),
        SyllableWord(listOf("TO", "PI"), R.drawable.ic_word_batu)
    )

    /**
     * Membuat daftar soal kuis untuk level tertentu (1, 2, atau 3).
     * Level 1: hanya tipe AudioQuestion (dengarkan -> pilih pasangan suku kata).
     * Level 2: hanya tipe ImageQuestion (lihat gambar -> lengkapi suku kata).
     * Level 3: campuran kedua tipe, diacak.
     * Setiap level menghasilkan 10 soal (boleh berulang karena data dasar terbatas).
     */
    fun generateQuiz(level: Int): List<QuizQuestion> {
        val words = getAllSyllableWords()
        val audioQuestions = words.map { buildAudioQuestion(it) }
        val imageQuestions = words.map { buildImageQuestion(it) }

        val pool: List<QuizQuestion> = when (level) {
            1 -> audioQuestions
            2 -> imageQuestions
            else -> audioQuestions + imageQuestions
        }

        // Ulangi & acak sampai mendapat 10 soal supaya kuis tidak terlalu singkat
        val result = mutableListOf<QuizQuestion>()
        var i = 0
        val shuffledPool = pool.shuffled()
        while (result.size < TOTAL_QUESTIONS) {
            result.add(shuffledPool[i % shuffledPool.size])
            i++
        }
        return result.shuffled().take(TOTAL_QUESTIONS)
    }

    private fun buildAudioQuestion(word: SyllableWord): QuizQuestion.AudioQuestion {
        val correctText = word.displayText() // "DA - DU"
        val distractors = generateSyllableDistractors(word)
        val options = (listOf(correctText) + distractors).shuffled()
        val correctIndex = options.indexOf(correctText)
        return QuizQuestion.AudioQuestion(
            correctWord = word,
            options = options,
            correctAnswerIndex = correctIndex
        )
    }

    private fun buildImageQuestion(word: SyllableWord): QuizQuestion.ImageQuestion {
        val known = word.syllables.first()
        val correctSecond = word.syllables.last()
        val otherSyllables = vokal.flatMap { v ->
            listOf("T${v}", "M${v}", "D${v}").map { it[0].uppercaseChar() + it.substring(1) }
        }.filter { it != correctSecond }.distinct().shuffled().take(3)

        val options = (listOf(correctSecond) + otherSyllables).shuffled()
        val correctIndex = options.indexOf(correctSecond)
        return QuizQuestion.ImageQuestion(
            imageRes = word.illustrationRes,
            knownSyllable = known,
            options = options,
            correctAnswerIndex = correctIndex
        )
    }

    /** Membuat 3 opsi pengganggu yang masuk akal (kombinasi suku kata yang sama). */
    private fun generateSyllableDistractors(word: SyllableWord): List<String> {
        val s1 = word.syllables.getOrElse(0) { "DA" }
        val s2 = word.syllables.getOrElse(1) { "DU" }
        val correctText = word.displayText()

        // Kumpulan kandidat pengganggu yang masuk akal (kombinasi silang suku kata)
        val candidates = linkedSetOf(
            "$s1 - $s1",
            "$s2 - $s1",
            "$s2 - $s2",
            "$s1 - $s2".let { if (it == correctText) "$s2 - $s1" else it }
        ).filter { it != correctText }.toMutableList()

        // Fallback generik jika kandidat di atas masih kurang dari 3 (kasus tepi)
        val genericFallback = listOf("DA - DA", "DU - DA", "DO - DO", "BA - BA", "TA - TA", "MA - MA")
            .filter { it != correctText }

        var i = 0
        while (candidates.size < 3 && i < genericFallback.size) {
            if (genericFallback[i] !in candidates) candidates.add(genericFallback[i])
            i++
        }

        return candidates.shuffled().take(3)
    }

    const val TOTAL_QUESTIONS = 10
}
