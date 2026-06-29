# Trilinggo Kids — Aplikasi Belajar Huruf & Suku Kata untuk Anak

Aplikasi Android (Kotlin) untuk belajar membaca: huruf A-Z, suku kata
per konsonan, dan kuis interaktif dengan sistem skor.

## Cara Menjalankan
1. Buka folder ini di Android Studio (File > Open).
2. Tunggu proses Gradle Sync selesai.
3. Jalankan di emulator atau device fisik (Run > Run 'app').

Lihat **TUTORIAL.md** (atau dokumen tutorial yang diberikan terpisah)
untuk panduan instalasi Android Studio dari awal dan penjelasan struktur
proyek secara rinci.

## Struktur Singkat
```
app/src/main/java/com/trilinggo/kids/
├── data/
│   ├── model/        -> data class (User, LetterCard, SyllableWord, QuizQuestion)
│   ├── local/         -> SharedPreferences (SessionManager, ScoreManager)
│   └── repository/    -> LearningRepository (sumber semua materi & soal kuis)
├── ui/
│   ├── splash/        -> Halaman pembuka
│   ├── auth/           -> Login & Register
│   ├── home/           -> Menu utama
│   ├── huruf/           -> Belajar Huruf A-Z
│   ├── sukukata/        -> Belajar Suku Kata (menu, vokal, per-kata)
│   ├── kuis/            -> Pilih Level, Kuis, Score
│   └── common/          -> Settings
└── util/                -> TtsHelper, PasswordHasher, IntentExtras
```

## Teknologi
- Kotlin + ViewBinding (tanpa Jetpack Compose, sesuai kurikulum dasar)
- SharedPreferences untuk akun & skor (tanpa server/database eksternal)
- Android TextToSpeech (TTS) bawaan untuk pengucapan huruf/suku kata
- Material Components untuk komponen input (TextInputLayout)
