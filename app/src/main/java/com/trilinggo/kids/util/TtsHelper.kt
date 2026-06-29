package com.trilinggo.kids.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Wrapper sederhana untuk Android TextToSpeech (TTS) bawaan, dipakai untuk
 * mengucapkan huruf atau suku kata ketika tombol mic ditekan.
 *
 * Cara pakai:
 *   val tts = TtsHelper(context)
 *   tts.speak("BA")
 *   ...
 *   override fun onDestroy() { tts.shutdown(); super.onDestroy() }
 */
class TtsHelper(context: Context) {

    private var textToSpeech: TextToSpeech? = null
    private var isReady = false

    init {
        textToSpeech = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("id", "ID"))
                isReady = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
                textToSpeech?.setSpeechRate(0.9f)
            }
        }
    }

    fun speak(text: String) {
        if (!isReady) return
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "trilinggo_utt_${text.hashCode()}")
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
