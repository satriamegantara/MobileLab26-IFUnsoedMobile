# Getar - Pemantau Gempa Bumi BMKG

> Pantau aktivitas gempa bumi terkini di seluruh Indonesia secara cepat, akurat, dan real-time.

Aplikasi Android modern berbasis **Jetpack Compose** dan arsitektur **MVVM** yang menyajikan data resmi gempa bumi dari **BMKG (Badan Meteorologi, Klimatologi, dan Geofisika)**. Didesain dengan prinsip Material Design 3 untuk memberikan pengalaman pengguna yang responsif, adaptif (Light & Dark theme), serta andal dalam menangani konektivitas jaringan.

---

## Cuplikan Layar (Screenshots)

|         Home Screen (Light)          |          Detail Screen (Light)           |           Home Screen (Dark)            |                Error State                 |
| :----------------------------------: | :--------------------------------------: | :-------------------------------------: | :----------------------------------------: |
| ![Home Screen](screenshots/home.png) | ![Detail Screen](screenshots/detail.png) | ![Home Dark](screenshots/home_dark.png) | ![Error State](screenshots/error_dark.png) |

---

## Fitur Utama

1. **Daftar Gempa Terkini**: Menampilkan 15 data gempa bumi terbaru secara real-time langsung dari API BMKG.
2. **Pencarian Lokal Reaktif**: Menyaring data gempa berdasarkan nama wilayah (_case-insensitive_) secara instan tanpa melakukan request ulang ke server.
3. **Detail Komprehensif**: Menampilkan informasi lengkap magnitudo, waktu, koordinat, kedalaman, wilayah, serta status potensi tsunami.
4. **Indikator Keparahan Gempa (Severity Level)**: Lencana berkode warna dinamis (Ringan, Sedang, Kuat, Sangat Kuat) sesuai skala magnitudo.
5. **State Management Andal**: Penanganan transisi state yang mulus untuk kondisi _Loading_, _Success_, _Empty List_, dan _Error_ dengan tombol coba lagi (_Retry_).
6. **Dukungan Tema Terang & Gelap**: Mengadopsi palet warna kontras tinggi Material 3 yang nyaman di mata.

---

## Arsitektur Aplikasi

Aplikasi dibangun mengikuti panduan arsitektur resmi Android (**Clean Architecture & MVVM Pattern**) dengan pemisahan tanggung jawab (_separation of concerns_) yang ketat:

```text
       ┌────────────────────────┐
       │     UI Layer (View)    │  <- Jetpack Compose (HomeScreen, DetailScreen)
       └───────────┬────────────┘
                   │ Mengamati StateFlow & Mengirim Event Pengguna
                   ▼
       ┌────────────────────────┐
       │       ViewModel        │  <- HomeViewModel (StateFlow, combine, Coroutines)
       └───────────┬────────────┘
                   │ Mengambil Data
                   ▼
       ┌────────────────────────┐
       │    Repository Layer    │  <- GempaRepository
       └───────────┬────────────┘
                   │ Eksekusi Request & Mapping DTO ke Domain Model
                   ▼
       ┌────────────────────────┐
       │   Remote Data Source   │  <- Retrofit + Gson (ApiService)
       └────────────────────────┘
```

### Penjelasan Layer

- **Data Layer (`com.getar.app.data`)**:
  - `model`: Berisi DTO jaringan (`GempaResponseDto`) dan Domain Model murni (`Earthquake`). Berkas `Mapper.kt` bertugas mengonversi DTO ke Domain Model secara aman dari _null_.
  - `remote`: Konfigurasi Retrofit singleton (`RetrofitInstance`) dan kontrak endpoint (`ApiService`).
  - `repository`: `GempaRepository` mengabstraksi pemanggilan API dari ViewModel.
- **UI Layer (`com.getar.app.ui`)**:
  - `home`: `HomeScreen` (Stateful & Stateless composable) dan `HomeViewModel`.
  - `detail`: `DetailScreen` untuk visualisasi informasi gempa terpilih.
  - `component`: Komponen atomik yang dapat digunakan kembali (_reusable & stateless_) seperti `GetarTopBar`, `EarthquakeItem`, `MagnitudeBadge`, `SearchField`, dan `StateViews`.
  - `state`: Deklarasi `sealed interface UiState` (_Loading_, _Success_, _Error_).
  - `theme`: Sistem desain (Color, Theme, Type) dengan ekstensi kustom `SeverityColors`.
- **Navigation & Util**:
  - `navigation`: `GetarNavHost` dan deklarasi rute antarlayar.
  - `util`: Extension function Kotlin untuk pemformatan data, pemetaan severity, dan pemfilteran list.

### Struktur Paket

```text
com.getar.app
├── MainActivity.kt
├── data
│   ├── model
│   │   ├── Earthquake.kt
│   │   ├── GempaResponseDto.kt
│   │   └── Mapper.kt
│   ├── remote
│   │   ├── ApiService.kt
│   │   └── RetrofitInstance.kt
│   └── repository
│       └── GempaRepository.kt
├── navigation
│   ├── GetarNavHost.kt
│   └── Routes.kt
├── ui
│   ├── component
│   │   ├── EarthquakeItem.kt
│   │   ├── GetarTopBar.kt
│   │   ├── InfoRow.kt
│   │   ├── MagnitudeBadge.kt
│   │   ├── PotensiCard.kt
│   │   ├── SearchField.kt
│   │   └── StateViews.kt
│   ├── detail
│   │   └── DetailScreen.kt
│   ├── home
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── state
│   │   └── UiState.kt
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── util
    └── Extensions.kt
```

---

## Integrasi API BMKG

Aplikasi memanfaatkan Open Data API dari **BMKG Indonesia (TEWS - Tsunami Early Warning System)**:

- **Base URL**: `https://data.bmkg.go.id/`
- **Endpoint**: `DataMKG/TEWS/gempaterkini.json`
- **Metode HTTP**: `GET`
- **Format Data**: JSON

### Struktur Respons & Field yang Digunakan

```json
{
  "Infogempa": {
    "gempa": [
      {
        "Tanggal": "07 Okt 2026",
        "Jam": "12:30:15 WIB",
        "DateTime": "2026-10-07T05:30:15+00:00",
        "Coordinates": "-6.92,107.60",
        "Lintang": "6.92 LS",
        "Bujur": "107.60 BT",
        "Magnitude": "5.4",
        "Kedalaman": "10 km",
        "Wilayah": "Pusat gempa berada di darat 15 km barat daya Kab. Bandung",
        "Potensi": "Tidak berpotensi tsunami"
      }
    ]
  }
}
```

Field yang dipetakan ke dalam aplikasi: `Tanggal`, `Jam`, `Coordinates`, `Magnitude`, `Kedalaman`, `Wilayah`, dan `Potensi`.

---

## Spesifikasi Teknis & Lingkungan Pengembangan

- **Bahasa**: Kotlin `2.2.10`
- **Minimum SDK**: API 31 (Android 12)
- **Target / Compile SDK**: API 37
- **UI Toolkit**: Jetpack Compose (BOM `2026.02.01`) + Material 3
- **Pustaka Pihak Ketiga (Sesuai Aturan Ketat Tugas)**:
  - Retrofit (`2.11.0`) & Converter Gson (`2.11.0`)
  - Navigation Compose (`2.8.8`)
  - Lifecycle ViewModel Compose (`2.8.7`)
  - Material Icons Core (BOM)

### Cara Build & Menjalankan Aplikasi

1. Klon repositori ini:
   ```bash
   git clone https://github.com/satriamegantara/MobileLab26-IFUnsoedMobile.git
   ```
2. Buka proyek melalui **Android Studio**.
3. Tunggu proses **Gradle Sync** selesai.
4. Hubungkan perangkat fisik Android (aktifkan _USB Debugging_) atau jalankan Android Emulator (API 31+).
5. Klik tombol **Run 'app'** (`Shift + F10`).

### Cara Membuat Berkas APK Debug

Jalankan perintah Gradle Wrapper melalui terminal proyek:

```bash
# Windows PowerShell / CMD:
.\gradlew assembleDebug

# Linux / macOS:
./gradlew assembleDebug
```

Berkas APK yang dihasilkan akan berada di:
`app/build/outputs/apk/debug/app-debug.apk`

---

## Keputusan Desain (Design Rationale)

1. **Penamaan ("Getar")**:
   Nama yang singkat, lugas, dan kontekstual terhadap fenomena seismik di Indonesia.
2. **Palet Warna**:
   - Warna utama mengadopsi nuansa biru dongker/slate tepercaya khas pemantauan geofisika.
   - Menggunakan ekstensi khusus `SeverityColors` untuk lencana magnitudo:
     - **Hijau Sejuk** (< 5.0 SR): Ringan / Rendah risiko.
     - **Oranye/Kuning Hangat** (5.0 - 5.9 SR): Sedang / Waspada.
     - **Merah Oranye** (6.0 - 6.9 SR): Kuat / Perhatian tinggi.
     - **Merah Tua Berani** (>= 7.0 SR): Sangat Kuat / Bahaya.
3. **Tipografi**:
   Menggunakan font Google (_Plus Jakarta Sans_ untuk keterbacaan teks modern dan _JetBrains Mono_ untuk data teknis angka/koordinat) dengan modifikasi skala tipe Material 3 yang hierarkis dan mudah dipindai mata saat terjadi bencana.

---

## Identitas Pengembang

- **Nama**: Satria Megantara
- **NIM**: H1D024022
- **Shift**: I
- **Mata Kuliah**: Praktikum Pemrograman Mobile
- **Institusi**: Teknik Informatika, Universitas Jenderal Soedirman
- **Link Video**: https://youtu.be/CNsxDWbkRwo
