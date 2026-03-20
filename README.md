# 🐉 AzHua

> **Aplikasi Streaming Anime Android dengan Arsitektur Ekstensi Modular**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg?logo=kotlin)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg?logo=android)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-purple.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## ✨ Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| 🔌 **Sistem Ekstensi** | Tambah sumber anime via plugin modular (Anichin, dll) |
| 🎬 **Video Player** | ExoPlayer dengan dukungan HLS (.m3u8) & MP4 |
| 📚 **Riwayat Tonton** | Sinkronisasi progres otomatis ke Room Database |
| 🔖 **Bookmark** | Simpan anime favorit dengan kategori |
| 🔍 **Pencarian & Discover** | Jelajahi konten dari berbagai sumber |
| 🏛️ **Extension Store** | Download & install ekstensi langsung dari app |

## 📱 Screenshot

*(Screenshot akan ditambahkan segera)*

## 🏗️ Arsitektur

```
AzHua/
├── 🎨 app/                 # Aplikasi utama (UI + ViewModel)
├── 📋 core-contracts/      # Interface & model untuk ekstensi
├── 🔌 ext-anichin/         # Ekstensi contoh (sumber Anichin)
├── 📚 docs/                # Dokumentasi fase pengembangan
├── 🔐 keystore/            # Signing key untuk release
├── 📦 releases/            # APK hasil build
└── 🔧 scripts/             # Build automation
```

### Tech Stack

- **UI**: Jetpack Compose + Material 3
- **Arsitektur**: MVVM dengan StateFlow
- **Database**: Room (lokal)
- **Networking**: OkHttp + Gson
- **Player**: Media3 ExoPlayer
- **DI**: Manual (tanpa framework)

## 🚀 Cara Build

### Prerequisites
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17
- Android SDK 34

### Build Aplikasi Utama
```bash
# Debug build
./gradlew :app:assembleDebug

# Release build (tersigned)
./gradlew :app:assembleRelease
```

### Build Ekstensi
```bash
# Build ekstensi Anichin
./scripts/build-extension.sh

# Atau manual
./gradlew :ext-anichin:assembleRelease
```

### Output
- App: `app/build/outputs/apk/release/azhua-v*.apk`
- Ekstensi: `ext-anichin/build/outputs/apk/release/ext-anichin-v*.apk`

## 🔧 Setup Ekstensi Repository

1. Buat repository GitHub publik untuk ekstensi
2. Buat file `index.json`:
```json
[
  {
    "name": "Anichin",
    "pkg": "com.azhua.ext.anichin",
    "versionCode": 1,
    "versionName": "1.0.0",
    "lang": "id",
    "icon": "https://raw.githubusercontent.com/USER/REPO/main/icons/anichin.png",
    "apkUrl": "https://github.com/USER/REPO/releases/download/v1.0.0/ext-anichin-v1.0.0.apk"
  }
]
```
3. Update `REPO_URL` di `ExtensionManager.kt`

## 📖 Dokumentasi Pengembangan

| Fase | Dokumen | Deskripsi |
|------|---------|-----------|
| Fase 2 | [Paviliun Kitab](docs/FASE2_PAVILIUN_KITAB.md) | Sistem Repository Ekstensi |
| Fase 3 | [Gerbang Utama](docs/FASE3_GERBANG_UTAMA.md) | UI/UX Tema Kultivator |
| Fase 4 | [Penempaan Artefak](docs/FASE4_PENEMPAAN_ARTEFAK.md) | ExoPlayer Integration |
| Fase 5 | [Mata Ilahi](docs/FASE5_MATA_ILAHI.md) | Parsing & Web Scraping |
| Fase 6 | [Aula Cermin](docs/FASE6_AULA_CERMIN.md) | History & Bookmark |

## 🛡️ Keamanan

- Semua ekstensi diisolasi dalam proses terpisah
- FileProvider untuk sharing APK aman
- ProGuard/R8 untuk obfuscation (opsional)

## ⚠️ Disclaimer

Aplikasi ini dibuat untuk tujuan edukasi. Konten anime yang ditampilkan bergantung pada ekstensi yang diinstall. Pengguna bertanggung jawab atas kepatuhan terhadap hukum dan lisensi konten di wilayahnya.

## 📄 Lisensi

```
MIT License

Copyright (c) 2024-2025 AzHua Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

<p align="center">
  <i>Dibangun dengan 🐉 dan ☕ oleh komunitas AzHua</i>
</p>
