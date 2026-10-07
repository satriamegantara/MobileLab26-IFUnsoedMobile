# PRD — Getar: Katalog & Monitoring Gempa BMKG

| | |
|---|---|
| **Nama aplikasi** | **Getar** |
| **Tagline** | *Pantau gempa terkini, lebih sigap.* |
| **Mata kuliah** | Mobile Programming — Responsi Mobile I (Paket 1) |
| **Platform** | Android (min SDK 24, target SDK terbaru) |
| **Bahasa / UI** | Kotlin / Jetpack Compose + Material Design 3 |
| **Package name** | `com.getar.app` |
| **Versi dokumen** | 2.0 (detail) |

---

## Daftar Isi

1. Ringkasan Produk
2. Tujuan dan Metrik Keberhasilan
3. Ruang Lingkup
4. Pengguna dan Skenario
5. Identitas Aplikasi (Nama, Konsep, Logo)
6. Kebutuhan Fungsional
7. Kebutuhan Non-Fungsional
8. Alur Pengguna dan Navigasi
9. Spesifikasi Layar (Wireframe)
10. Design System (Warna, Tipografi, Bentuk, Spasi)
11. Spesifikasi Komponen Reusable
12. Arsitektur MVVM
13. Spesifikasi API dan Model Data
14. Implementasi Referensi (Kode)
15. Pemetaan Fitur Kotlin
16. Penanganan State dan Edge Case
17. Copywriting / Teks UI
18. Aksesibilitas
19. Rencana Pengujian
20. Dependensi dan Konfigurasi Proyek
21. Deliverables: README dan Video
22. Pemetaan Persyaratan Tugas ke Fitur
23. Kriteria Penerimaan (Definition of Done)
24. Risiko dan Mitigasi
25. Rencana Pengerjaan

---

## 1. Ringkasan Produk

Indonesia berada di zona cincin api, sehingga gempa terjadi cukup sering. **Getar** adalah aplikasi Android yang mengambil daftar gempa terkini dari REST API publik BMKG (tanpa API key), menampilkannya dalam daftar yang ringkas, memungkinkan pencarian berdasarkan wilayah, dan menyajikan detail lengkap tiap kejadian.

Selain fungsi produk, aplikasi ini adalah **wadah penerapan konsep Mobile Programming**: Kotlin idiomatik, Jetpack Compose, Material Design 3 dengan tema kustom, arsitektur MVVM, dan UI berbasis state.

## 2. Tujuan dan Metrik Keberhasilan

### 2.1 Tujuan produk
- Menyajikan informasi gempa terkini secara cepat dan mudah dipindai.
- Membantu pengguna menemukan gempa di wilayah tertentu.
- Menampilkan detail kejadian dan potensi dampak secara jelas.

### 2.2 Tujuan akademik
- Menerapkan data class, null safety, lambda, dan extension function.
- Menerapkan Compose: composable layout, lazy layout, reusable composable.
- Menerapkan MVVM dengan Repository, Retrofit, sealed `UiState`, dan `StateFlow`.
- Menerapkan `navigation-compose` untuk 2 layar.
- Memodifikasi `Color.kt`, `Theme.kt` (Light/Dark), dan `Type.kt` (≥ 2 override).

### 2.3 Metrik keberhasilan (terukur saat penilaian)

| Metrik | Target |
|---|---|
| Waktu dari buka aplikasi sampai daftar tampil (jaringan normal) | ≤ 3 detik |
| Waktu respons filter pencarian | Instan (< 100 ms, filter lokal) |
| Seluruh persyaratan wajib pada dokumen tugas | 100% terpenuhi |
| Crash saat rotasi layar / ganti tema / tanpa internet | 0 |
| Library di luar daftar yang diizinkan | 0 |

## 3. Ruang Lingkup

**Termasuk**
- 2 layar: Home dan Detail.
- Fetch data dari satu endpoint BMKG.
- Pencarian lokal berdasarkan wilayah.
- State Loading, Success, Error (+ tampilan kosong untuk hasil pencarian).
- Tema terang dan gelap dengan identitas visual sendiri.

**Tidak termasuk**
- Gambar, peta, shakemap (dilarang library image; tanpa gambar di daftar).
- Notifikasi push, widget, database/cache offline, login, favorit.
- Layar tambahan (settings, about, dll.).
- Pemanggilan API langsung dari Composable.
- Dynamic color (Material You) — dinonaktifkan agar identitas warna Getar konsisten.

## 4. Pengguna dan Skenario

| Persona | Deskripsi | Kebutuhan utama |
|---|---|---|
| **Warga umum** | Ingin tahu apakah ada gempa baru di sekitarnya | Daftar terbaru, magnitudo mudah dibaca, potensi tsunami jelas |
| **Pencari wilayah** | Memantau daerah tertentu (kampung halaman, lokasi kerja) | Pencarian cepat berdasarkan nama wilayah |
| **Penilai / dosen** | Menilai penerapan konsep kuliah | Kode rapi, arsitektur jelas, persyaratan terpenuhi |

**Skenario utama**
1. *Sebagai warga, saya membuka Getar untuk melihat gempa terbaru dan magnitudonya.*
2. *Sebagai pencari wilayah, saya mengetik "Maluku" dan hanya melihat gempa yang wilayahnya mengandung kata itu.*
3. *Sebagai warga, saya menekan satu gempa untuk melihat jam, koordinat, kedalaman, dan potensi tsunami.*
4. *Saat tidak ada internet, saya melihat pesan jelas dan bisa mencoba lagi.*

## 5. Identitas Aplikasi

### 5.1 Nama dan makna
**Getar** — kata Indonesia untuk *vibration/tremor*: singkat, mudah diingat, langsung berhubungan dengan gempa, dan mudah dilafalkan.

### 5.2 Konsep visual
**"Seismograf modern."** Tampilan bersih dengan aksen oranye terbakar (warna peringatan yang tidak seagresif merah) di atas latar krem hangat (light) atau cokelat-arang hangat (dark). Warna keparahan (severity) dipakai konsisten pada lencana magnitudo agar tingkat bahaya bisa dipindai sekilas.

### 5.3 Ikon aplikasi (adaptive icon, tanpa gambar bitmap dari library)
- Latar: `#C2410C` (primary).
- Foreground: garis zig-zag putih menyerupai gelombang seismograf (vector drawable `ic_launcher_foreground.xml`).
- Boleh memakai ikon bawaan template jika waktu terbatas; ikon tidak termasuk penilaian.

### 5.4 Judul pada TopAppBar
- Home: **"Getar"** (titleLarge, bold), subjudul kecil opsional "Gempa Terkini BMKG".
- Detail: **"Detail Gempa"** + ikon back.

## 6. Kebutuhan Fungsional

| ID | Kebutuhan | Layar | Prioritas |
|---|---|---|---|
| FR-01 | Mengambil data dari `GET https://data.bmkg.go.id/DataMKG/TEWS/gempaterkini.json` saat aplikasi dibuka | Home | Wajib |
| FR-02 | Menampilkan `Scaffold` dengan `TopAppBar` berjudul nama aplikasi | Home | Wajib |
| FR-03 | Search bar memfilter daftar secara lokal berdasarkan **Wilayah**, tidak case-sensitive, mengabaikan spasi di awal/akhir | Home | Wajib |
| FR-04 | Daftar memakai `LazyColumn` (bukan `Column` + scroll); tiap item menampilkan Tanggal, Magnitudo, Wilayah | Home | Wajib |
| FR-05 | Klik item membuka Detail untuk gempa yang dipilih | Home → Detail | Wajib |
| FR-06 | Detail menampilkan Tanggal, Jam, Coordinates, Magnitudo, Kedalaman, Wilayah, Potensi | Detail | Wajib |
| FR-07 | Detail memiliki tombol back di `TopAppBar` dan mendukung back sistem | Detail | Wajib |
| FR-08 | Menampilkan indikator loading selama request berjalan | Home | Wajib |
| FR-09 | Menampilkan pesan error + tombol **Coba Lagi** saat request gagal | Home | Wajib |
| FR-10 | Menampilkan tampilan kosong ("tidak ditemukan") saat filter tidak menghasilkan item | Home | Disarankan |
| FR-11 | Tombol hapus (×) pada search bar untuk mengosongkan query | Home | Disarankan |
| FR-12 | Lencana magnitudo berwarna sesuai tingkat keparahan | Home, Detail | Disarankan |
| FR-13 | Kartu Potensi pada Detail berubah warna: ada potensi tsunami vs tidak | Detail | Disarankan |
| FR-14 | Query pencarian dan posisi scroll bertahan saat kembali dari Detail dan saat rotasi | Home | Disarankan |
| FR-15 | Pull-to-refresh | Home | Opsional (jangan jika butuh library tambahan) |

## 7. Kebutuhan Non-Fungsional

| Kategori | Kebutuhan |
|---|---|
| **Library** | Hanya `retrofit`, `converter-gson`, `navigation-compose`, `lifecycle-viewmodel-compose` di luar dependensi default template Compose/AndroidX (activity-compose, material3, core-ktx, lifecycle-runtime-ktx) |
| **Gambar** | Tanpa gambar di daftar; dilarang Coil/Glide. Ikon memakai `Icons` bawaan atau vector drawable |
| **Izin** | `INTERNET` di `AndroidManifest.xml` |
| **Threading** | Network via coroutine (`viewModelScope`), tidak di main thread |
| **Performa** | `LazyColumn` dengan `key` stabil; hindari alokasi berat dalam composable |
| **Kompatibilitas** | Min SDK 24; mendukung portrait & landscape tanpa crash |
| **Tema** | Light dan Dark mengikuti sistem |
| **Keamanan** | HTTPS saja; tidak ada data sensitif/API key |
| **Kode** | Satu tanggung jawab per kelas; nama jelas; tanpa logika bisnis di composable |

## 8. Alur Pengguna dan Navigasi

```
[Launch] → HomeScreen (Loading)
              ├─ sukses → daftar gempa
              │     ├─ ketik di search → daftar terfilter (atau EmptyView)
              │     └─ klik item → DetailScreen(id) ── back ──► HomeScreen (state terjaga)
              └─ gagal  → ErrorView ── Coba Lagi ──► Loading
```

**Rute navigasi (`navigation-compose`)**

| Rute | Argumen | Layar |
|---|---|---|
| `home` | – | HomeScreen (start destination) |
| `detail/{id}` | `id: Int` (indeks gempa pada daftar asli) | DetailScreen |

Transisi: default `NavHost` (fade), tanpa animasi kustom agar sederhana.

## 9. Spesifikasi Layar (Wireframe)

### 9.1 Home Screen

```
┌──────────────────────────────────┐
│ Getar                            │  ← TopAppBar (primary container)
│ Gempa Terkini BMKG               │
├──────────────────────────────────┤
│ 🔍  Cari wilayah…              ✕ │  ← Search field (rounded 28dp)
├──────────────────────────────────┤
│ ┌────┐  Pusat gempa berada di    │
│ │5.4 │  laut 98 km BaratLaut     │  ← EarthquakeItem
│ └────┘  Tual-Maluku Tenggara     │
│         06 Okt 2026 • 14:22 WIB  │
├──────────────────────────────────┤
│ ┌────┐  …                        │
│ │5.1 │                           │
│ └────┘                           │
└──────────────────────────────────┘
```

**Aturan tampilan per state**

| State | Tampilan |
|---|---|
| Loading | Search field dinonaktifkan; `CircularProgressIndicator` di tengah + teks "Memuat data gempa…" |
| Success (ada hasil) | `LazyColumn` dengan item |
| Success (filter kosong) | `EmptyView`: ikon + "Tidak ada gempa untuk “{query}”" + tombol "Hapus pencarian" |
| Error | `ErrorView`: ikon + pesan + tombol "Coba Lagi" |

**Item daftar (EarthquakeItem)**
- Tinggi minimum 80dp, `ElevatedCard`/`Card` dengan sudut 16dp, padding 16dp, jarak antar item 8dp.
- Kiri: `MagnitudeBadge` 52dp (lingkaran, warna severity, teks magnitudo `Mono Bold`).
- Kanan: Wilayah (`titleMedium`, maks 2 baris, ellipsis), lalu Tanggal • Jam (`labelMedium`, `onSurfaceVariant`).
- Seluruh kartu dapat diklik (`onClick`), ripple bawaan.

### 9.2 Detail Screen

```
┌──────────────────────────────────┐
│ ←  Detail Gempa                  │
├──────────────────────────────────┤
│   ┌──────────────────────────┐   │
│   │          5.4             │   │  ← Hero (display mono, warna severity)
│   │     Magnitudo • SEDANG   │   │
│   └──────────────────────────┘   │
│                                  │
│   ┌──────────────────────────┐   │
│   │ ✓ Tidak berpotensi tsunami│  │  ← Kartu Potensi
│   └──────────────────────────┘   │
│                                  │
│   Tanggal       06 Okt 2026      │
│   Jam           14:22:10 WIB     │
│   Coordinates   -6.12, 128.45    │
│   Kedalaman     10 km            │
│   Wilayah       Pusat gempa …    │
└──────────────────────────────────┘
```

- Konten dapat di-scroll (`verticalScroll`) agar aman di layar kecil/landscape.
- Label: `labelMedium` + `onSurfaceVariant`; nilai: `bodyLarge`. Koordinat dan kedalaman memakai font Mono.
- Kartu Potensi:
  - **Tidak berpotensi tsunami** → `tertiaryContainer` + ikon centang.
  - **Berpotensi tsunami / peringatan lain** → `errorContainer` + ikon peringatan.

## 10. Design System

> Prinsip: **kontras tinggi, hierarki jelas, warna hanya untuk makna.**

### 10.1 Palet warna Light (`lightColorScheme`)

| Peran | Hex | Catatan |
|---|---|---|
| primary | `#C2410C` | Oranye terbakar; kontras 5.2:1 terhadap putih |
| onPrimary | `#FFFFFF` | |
| primaryContainer | `#FFDBCB` | |
| onPrimaryContainer | `#3A0B00` | |
| secondary | `#475569` | Slate netral |
| onSecondary | `#FFFFFF` | |
| secondaryContainer | `#DDE3EA` | |
| onSecondaryContainer | `#111C28` | |
| tertiary | `#0F766E` | Teal (status aman) |
| onTertiary | `#FFFFFF` | |
| tertiaryContainer | `#BDF0E8` | |
| onTertiaryContainer | `#00201C` | |
| background | `#FFF8F5` | Krem hangat |
| onBackground | `#201A17` | |
| surface | `#FFF8F5` | |
| onSurface | `#201A17` | |
| surfaceVariant | `#F4DED5` | Latar search field/kartu |
| onSurfaceVariant | `#52443D` | |
| outline | `#85736C` | |
| outlineVariant | `#D8C2B9` | |
| error | `#BA1A1A` | |
| onError | `#FFFFFF` | |
| errorContainer | `#FFDAD6` | |
| onErrorContainer | `#410002` | |

### 10.2 Palet warna Dark (`darkColorScheme`)

| Peran | Hex |
|---|---|
| primary | `#FFB59A` |
| onPrimary | `#5B1A00` |
| primaryContainer | `#832700` |
| onPrimaryContainer | `#FFDBCB` |
| secondary | `#B9C7D6` |
| onSecondary | `#233140` |
| secondaryContainer | `#3A4857` |
| onSecondaryContainer | `#D5E3F3` |
| tertiary | `#82D5CB` |
| onTertiary | `#003733` |
| tertiaryContainer | `#00504A` |
| onTertiaryContainer | `#BDF0E8` |
| background | `#181210` |
| onBackground | `#EDE0DB` |
| surface | `#181210` |
| onSurface | `#EDE0DB` |
| surfaceVariant | `#52443D` |
| onSurfaceVariant | `#D8C2B9` |
| outline | `#A08D85` |
| outlineVariant | `#52443D` |
| error | `#FFB4AB` |
| onError | `#690005` |
| errorContainer | `#93000A` |
| onErrorContainer | `#FFDAD6` |

### 10.3 Warna keparahan (custom, di luar `ColorScheme`)

Kategori ditentukan dari nilai magnitudo (`toDoubleOrNull()`; jika null → "Tidak diketahui").

| Kategori | Rentang | Light | Dark | Teks di atas badge |
|---|---|---|---|---|
| Ringan | < 5.0 | `#2E7D32` | `#81C784` | putih (light) / `#0B2D0E` (dark) |
| Sedang | 5.0 – 5.9 | `#EF6C00` | `#FFB74D` | putih (light) / `#3A1D00` (dark) |
| Kuat | 6.0 – 6.9 | `#C62828` | `#EF9A9A` | putih (light) / `#410002` (dark) |
| Sangat kuat | ≥ 7.0 | `#6A1B9A` | `#CE93D8` | putih (light) / `#2A0038` (dark) |
| Tidak diketahui | – | `#6B7280` | `#9CA3AF` | putih / `#111827` |

> Daftar "gempa terkini" BMKG umumnya berisi gempa M ≥ 5, tetapi kategori "Ringan" tetap disediakan agar logika aman untuk data apa pun.

### 10.4 Tipografi

**Keluarga font** (file `.ttf` dimasukkan ke `res/font/`; tidak butuh library tambahan — jangan memakai `ui-text-google-fonts`)

| Peran | Font | Lisensi | Alasan |
|---|---|---|---|
| UI & teks | **Plus Jakarta Sans** (Regular 400, Medium 500, SemiBold 600, Bold 700) | OFL | Modern, terbaca baik, ramah teks Indonesia |
| Data numerik | **JetBrains Mono** (Medium 500, Bold 700) | OFL | Angka lebar tetap → magnitudo, koordinat, kedalaman rapi |

Nama berkas: `plus_jakarta_sans_regular.ttf`, `plus_jakarta_sans_medium.ttf`, `plus_jakarta_sans_semibold.ttf`, `plus_jakarta_sans_bold.ttf`, `jetbrains_mono_medium.ttf`, `jetbrains_mono_bold.ttf`.
*Cadangan jika font belum ditambahkan:* `FontFamily.SansSerif` dan `FontFamily.Monospace`.

**Skala tipografi (override pada `Typography`)** — persyaratan minimal 2 override terpenuhi dengan 8 override:

| Style M3 | Font | Weight | Size / Line height | Letter spacing | Dipakai untuk |
|---|---|---|---|---|---|
| `displayMedium` | Mono | Bold | 56 / 64 sp | 0 | Angka magnitudo besar di Detail |
| `headlineSmall` | Jakarta | Bold | 24 / 32 sp | 0 | Judul status (error/empty) |
| `titleLarge` | Jakarta | Bold | 22 / 28 sp | 0 | Judul TopAppBar |
| `titleMedium` | Jakarta | SemiBold | 16 / 24 sp | 0.15 sp | Wilayah pada item daftar |
| `bodyLarge` | Jakarta | Normal | 16 / 24 sp | 0.5 sp | Nilai pada Detail |
| `bodyMedium` | Jakarta | Normal | 14 / 20 sp | 0.25 sp | Teks pesan, hint search |
| `labelLarge` | Jakarta | Medium | 14 / 20 sp | 0.1 sp | Teks tombol |
| `labelMedium` | Jakarta | Medium | 12 / 16 sp | 0.5 sp | Tanggal, label field |
| *(custom)* `MonoValue` | Mono | Medium | 16 / 24 sp | 0 | Koordinat, kedalaman |
| *(custom)* `MonoBadge` | Mono | Bold | 18 / 24 sp | 0 | Angka pada lencana magnitudo |

### 10.5 Bentuk, spasi, elevasi

| Token | Nilai |
|---|---|
| Shape small / medium / large | 8dp / 12dp / 16dp |
| Search field | `RoundedCornerShape(28.dp)` |
| Spasi dasar | kelipatan 4dp: 4, 8, 12, 16, 24, 32 |
| Padding layar | 16dp horizontal |
| Jarak antar item daftar | 8dp |
| Elevasi kartu | 1dp (light), 0dp + outline (dark) |
| Ukuran badge magnitudo | 52dp (daftar), tipografi display pada Detail |
| Tinggi minimum target sentuh | 48dp |

### 10.6 Aturan tema
- Mengikuti `isSystemInDarkTheme()`.
- `dynamicColor = false`.
- Status bar & navigation bar edge-to-edge (`enableEdgeToEdge()`), ikon menyesuaikan tema.
- Warna dibaca dari `MaterialTheme.colorScheme`; **dilarang hardcode `Color(...)` di composable**. Warna severity diakses lewat `MaterialTheme.severity` (extension property, lihat bagian 14).

## 11. Spesifikasi Komponen Reusable

| Komponen | Parameter | Keterangan |
|---|---|---|
| `GetarTopBar` | `title: String`, `onBack: (() -> Unit)? = null`, `subtitle: String? = null` | `TopAppBar` M3; ikon back tampil hanya jika `onBack != null` |
| `SearchField` | `query: String`, `onQueryChange: (String) -> Unit`, `enabled: Boolean` | `OutlinedTextField` bersudut bulat, ikon search + tombol clear, `singleLine`, `ImeAction.Search` |
| `MagnitudeBadge` | `magnitude: String?`, `size: Dp = 52.dp` | Lingkaran berwarna severity, teks `MonoBadge` |
| `EarthquakeItem` | `earthquake: Earthquake`, `onClick: (Earthquake) -> Unit` | Kartu item daftar |
| `InfoRow` | `label: String`, `value: String`, `mono: Boolean = false` | Baris label–nilai di Detail |
| `PotensiCard` | `potensi: String?` | Kartu hijau/merah sesuai potensi tsunami |
| `LoadingView` | `message: String` | Progress + teks, terpusat |
| `ErrorView` | `message: String`, `onRetry: () -> Unit` | Ikon, pesan, tombol retry |
| `EmptyView` | `query: String`, `onClear: () -> Unit` | Hasil pencarian kosong |

Semua komponen: stateless, menerima `modifier: Modifier = Modifier` sebagai parameter pertama opsional, dan punya `@Preview` (light & dark).

## 12. Arsitektur MVVM

```
┌────────────────────────── UI Layer ──────────────────────────┐
│  MainActivity → GetarNavHost                                 │
│     HomeScreen / DetailScreen  (Composable, stateless-ish)   │
│        ▲ collectAsStateWithLifecycle*      │ event (lambda)  │
└────────┼───────────────────────────────────┼─────────────────┘
         │ StateFlow<UiState>, query         ▼
┌────────┴────────────────── ViewModel ───────────────────────┐
│  HomeViewModel (viewModelScope, combine, retry)              │
└────────┬─────────────────────────────────────────────────────┘
         ▼
┌────────┴──────────────── Data Layer ─────────────────────────┐
│  GempaRepository → ApiService (Retrofit) → BMKG              │
│  Model: GempaResponseDto → Mapper → Earthquake               │
└──────────────────────────────────────────────────────────────┘
```
\* `collectAsStateWithLifecycle` ada di `lifecycle-runtime-compose` (di luar daftar library). Gunakan **`collectAsState()`** bawaan Compose agar tetap sesuai batasan.

**Struktur paket**

```
com.getar.app
├── data
│   ├── model        GempaResponseDto.kt, Earthquake.kt, Mapper.kt
│   ├── remote       ApiService.kt, RetrofitInstance.kt
│   └── repository   GempaRepository.kt
├── ui
│   ├── theme        Color.kt, Theme.kt, Type.kt, Shape.kt
│   ├── state        UiState.kt
│   ├── component    GetarTopBar, SearchField, MagnitudeBadge, EarthquakeItem,
│   │                InfoRow, PotensiCard, LoadingView, ErrorView, EmptyView
│   ├── home         HomeScreen.kt, HomeViewModel.kt
│   └── detail       DetailScreen.kt
├── navigation       Routes.kt, GetarNavHost.kt
├── util             Extensions.kt   (extension functions)
└── MainActivity.kt
```

**Aturan layer**

| Layer | Boleh | Tidak boleh |
|---|---|---|
| Composable | Menampilkan state, memanggil lambda event | Memanggil Retrofit/Repository, menyimpan logika bisnis |
| ViewModel | Menyimpan state, memanggil Repository, filter | Mereferensikan `Context`/composable |
| Repository | Memanggil `ApiService`, memetakan DTO → model, membungkus hasil | Mengetahui UI |
| ApiService | Mendefinisikan endpoint | Logika lain |

**Berbagi data antar layar:** `HomeViewModel` dibuat di scope `NavHost`/Activity (`viewModel()` di level `GetarNavHost`) lalu diteruskan ke `DetailScreen`, yang mencari gempa berdasarkan `id`. Dengan begitu, Detail tidak melakukan request ulang.

## 13. Spesifikasi API dan Model Data

| | |
|---|---|
| Base URL | `https://data.bmkg.go.id/` |
| Endpoint | `GET DataMKG/TEWS/gempaterkini.json` |
| Auth | Tidak ada |
| Root | `Infogempa.gempa[]` |

**Contoh bentuk respons (format umum; verifikasi dengan respons asli saat implementasi)**

```json
{
  "Infogempa": {
    "gempa": [
      {
        "Tanggal": "06 Okt 2026",
        "Jam": "14:22:10 WIB",
        "DateTime": "2026-10-06T07:22:10+00:00",
        "Coordinates": "-6.12,128.45",
        "Lintang": "6.12 LS",
        "Bujur": "128.45 BT",
        "Magnitude": "5.4",
        "Kedalaman": "10 km",
        "Wilayah": "Pusat gempa berada di laut ...",
        "Potensi": "Gempa ini tidak berpotensi tsunami"
      }
    ]
  }
}
```

**Pemetaan field**

| Field JSON | Tipe DTO | Properti model | Digunakan di |
|---|---|---|---|
| `Tanggal` | `String?` | `tanggal` | Home, Detail |
| `Jam` | `String?` | `jam` | Home (opsional), Detail |
| `Coordinates` | `String?` | `coordinates` | Detail |
| `Magnitude` | `String?` | `magnitude` (+ `magnitudeValue: Double?`) | Home, Detail |
| `Kedalaman` | `String?` | `kedalaman` | Detail |
| `Wilayah` | `String?` | `wilayah` | Home, Detail |
| `Potensi` | `String?` | `potensi` | Detail |

> Gson mengabaikan null-safety Kotlin; maka seluruh field DTO dibuat **nullable dengan default `null`**, dan model UI diberi nilai fallback (`"-"`) lewat mapper.

## 14. Implementasi Referensi (Kode)

> Kerangka acuan, bukan kode final; sesuaikan nama dan detail saat implementasi.

### 14.1 `Color.kt`

```kotlin
package com.getar.app.ui.theme

import androidx.compose.ui.graphics.Color

// Light
val GetarPrimaryLight = Color(0xFFC2410C)
val GetarOnPrimaryLight = Color(0xFFFFFFFF)
val GetarPrimaryContainerLight = Color(0xFFFFDBCB)
val GetarOnPrimaryContainerLight = Color(0xFF3A0B00)
val GetarSecondaryLight = Color(0xFF475569)
val GetarOnSecondaryLight = Color(0xFFFFFFFF)
val GetarSecondaryContainerLight = Color(0xFFDDE3EA)
val GetarOnSecondaryContainerLight = Color(0xFF111C28)
val GetarTertiaryLight = Color(0xFF0F766E)
val GetarOnTertiaryLight = Color(0xFFFFFFFF)
val GetarTertiaryContainerLight = Color(0xFFBDF0E8)
val GetarOnTertiaryContainerLight = Color(0xFF00201C)
val GetarBackgroundLight = Color(0xFFFFF8F5)
val GetarOnBackgroundLight = Color(0xFF201A17)
val GetarSurfaceLight = Color(0xFFFFF8F5)
val GetarOnSurfaceLight = Color(0xFF201A17)
val GetarSurfaceVariantLight = Color(0xFFF4DED5)
val GetarOnSurfaceVariantLight = Color(0xFF52443D)
val GetarOutlineLight = Color(0xFF85736C)
val GetarOutlineVariantLight = Color(0xFFD8C2B9)
val GetarErrorLight = Color(0xFFBA1A1A)
val GetarOnErrorLight = Color(0xFFFFFFFF)
val GetarErrorContainerLight = Color(0xFFFFDAD6)
val GetarOnErrorContainerLight = Color(0xFF410002)

// Dark
val GetarPrimaryDark = Color(0xFFFFB59A)
val GetarOnPrimaryDark = Color(0xFF5B1A00)
val GetarPrimaryContainerDark = Color(0xFF832700)
val GetarOnPrimaryContainerDark = Color(0xFFFFDBCB)
val GetarSecondaryDark = Color(0xFFB9C7D6)
val GetarOnSecondaryDark = Color(0xFF233140)
val GetarSecondaryContainerDark = Color(0xFF3A4857)
val GetarOnSecondaryContainerDark = Color(0xFFD5E3F3)
val GetarTertiaryDark = Color(0xFF82D5CB)
val GetarOnTertiaryDark = Color(0xFF003733)
val GetarTertiaryContainerDark = Color(0xFF00504A)
val GetarOnTertiaryContainerDark = Color(0xFFBDF0E8)
val GetarBackgroundDark = Color(0xFF181210)
val GetarOnBackgroundDark = Color(0xFFEDE0DB)
val GetarSurfaceDark = Color(0xFF181210)
val GetarOnSurfaceDark = Color(0xFFEDE0DB)
val GetarSurfaceVariantDark = Color(0xFF52443D)
val GetarOnSurfaceVariantDark = Color(0xFFD8C2B9)
val GetarOutlineDark = Color(0xFFA08D85)
val GetarOutlineVariantDark = Color(0xFF52443D)
val GetarErrorDark = Color(0xFFFFB4AB)
val GetarOnErrorDark = Color(0xFF690005)
val GetarErrorContainerDark = Color(0xFF93000A)
val GetarOnErrorContainerDark = Color(0xFFFFDAD6)

// Severity (custom)
val SeverityMinorLight = Color(0xFF2E7D32);    val SeverityMinorDark = Color(0xFF81C784)
val SeverityModerateLight = Color(0xFFEF6C00); val SeverityModerateDark = Color(0xFFFFB74D)
val SeverityStrongLight = Color(0xFFC62828);   val SeverityStrongDark = Color(0xFFEF9A9A)
val SeverityMajorLight = Color(0xFF6A1B9A);    val SeverityMajorDark = Color(0xFFCE93D8)
val SeverityUnknownLight = Color(0xFF6B7280);  val SeverityUnknownDark = Color(0xFF9CA3AF)
```

### 14.2 `Type.kt`

```kotlin
package com.getar.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.getar.app.R

val JakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_medium, FontWeight.Medium),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold),
)

val GetarTypography = Typography(
    displayMedium = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold,
        fontSize = 56.sp, lineHeight = 64.sp, letterSpacing = 0.sp),
    headlineSmall = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = 0.sp),
    titleLarge = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.Bold,
        fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp),
    titleMedium = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    bodyLarge = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    labelLarge = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontFamily = JakartaSans, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)

// Gaya tambahan untuk data numerik
val MonoValue = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Medium,
    fontSize = 16.sp, lineHeight = 24.sp)
val MonoBadge = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold,
    fontSize = 18.sp, lineHeight = 24.sp)
```

### 14.3 `Theme.kt`

```kotlin
package com.getar.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = GetarPrimaryLight, onPrimary = GetarOnPrimaryLight,
    primaryContainer = GetarPrimaryContainerLight, onPrimaryContainer = GetarOnPrimaryContainerLight,
    secondary = GetarSecondaryLight, onSecondary = GetarOnSecondaryLight,
    secondaryContainer = GetarSecondaryContainerLight, onSecondaryContainer = GetarOnSecondaryContainerLight,
    tertiary = GetarTertiaryLight, onTertiary = GetarOnTertiaryLight,
    tertiaryContainer = GetarTertiaryContainerLight, onTertiaryContainer = GetarOnTertiaryContainerLight,
    background = GetarBackgroundLight, onBackground = GetarOnBackgroundLight,
    surface = GetarSurfaceLight, onSurface = GetarOnSurfaceLight,
    surfaceVariant = GetarSurfaceVariantLight, onSurfaceVariant = GetarOnSurfaceVariantLight,
    outline = GetarOutlineLight, outlineVariant = GetarOutlineVariantLight,
    error = GetarErrorLight, onError = GetarOnErrorLight,
    errorContainer = GetarErrorContainerLight, onErrorContainer = GetarOnErrorContainerLight,
)

private val DarkColors = darkColorScheme(
    primary = GetarPrimaryDark, onPrimary = GetarOnPrimaryDark,
    primaryContainer = GetarPrimaryContainerDark, onPrimaryContainer = GetarOnPrimaryContainerDark,
    secondary = GetarSecondaryDark, onSecondary = GetarOnSecondaryDark,
    secondaryContainer = GetarSecondaryContainerDark, onSecondaryContainer = GetarOnSecondaryContainerDark,
    tertiary = GetarTertiaryDark, onTertiary = GetarOnTertiaryDark,
    tertiaryContainer = GetarTertiaryContainerDark, onTertiaryContainer = GetarOnTertiaryContainerDark,
    background = GetarBackgroundDark, onBackground = GetarOnBackgroundDark,
    surface = GetarSurfaceDark, onSurface = GetarOnSurfaceDark,
    surfaceVariant = GetarSurfaceVariantDark, onSurfaceVariant = GetarOnSurfaceVariantDark,
    outline = GetarOutlineDark, outlineVariant = GetarOutlineVariantDark,
    error = GetarErrorDark, onError = GetarOnErrorDark,
    errorContainer = GetarErrorContainerDark, onErrorContainer = GetarOnErrorContainerDark,
)

@Immutable
data class SeverityColors(
    val minor: Color, val moderate: Color, val strong: Color,
    val major: Color, val unknown: Color,
)

private val LightSeverity = SeverityColors(
    SeverityMinorLight, SeverityModerateLight, SeverityStrongLight,
    SeverityMajorLight, SeverityUnknownLight,
)
private val DarkSeverity = SeverityColors(
    SeverityMinorDark, SeverityModerateDark, SeverityStrongDark,
    SeverityMajorDark, SeverityUnknownDark,
)

val LocalSeverityColors = staticCompositionLocalOf { LightSeverity }

/** Akses: MaterialTheme.severity.moderate */
val MaterialTheme.severity: SeverityColors
    @Composable get() = LocalSeverityColors.current

private val GetarShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
)

@Composable
fun GetarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSeverityColors provides if (darkTheme) DarkSeverity else LightSeverity
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = GetarTypography,
            shapes = GetarShapes,
            content = content,
        )
    }
}
```

### 14.4 Model, mapper, extension

```kotlin
// GempaResponseDto.kt
data class GempaResponseDto(@SerializedName("Infogempa") val infogempa: InfogempaDto? = null)
data class InfogempaDto(@SerializedName("gempa") val gempa: List<GempaDto>? = null)
data class GempaDto(
    @SerializedName("Tanggal") val tanggal: String? = null,
    @SerializedName("Jam") val jam: String? = null,
    @SerializedName("Coordinates") val coordinates: String? = null,
    @SerializedName("Magnitude") val magnitude: String? = null,
    @SerializedName("Kedalaman") val kedalaman: String? = null,
    @SerializedName("Wilayah") val wilayah: String? = null,
    @SerializedName("Potensi") val potensi: String? = null,
)

// Earthquake.kt  (model untuk UI)
data class Earthquake(
    val id: Int, val tanggal: String, val jam: String, val coordinates: String,
    val magnitude: String, val kedalaman: String, val wilayah: String, val potensi: String,
) {
    val magnitudeValue: Double? get() = magnitude.toDoubleOrNull()
}

// Mapper.kt
fun GempaDto.toEarthquake(id: Int) = Earthquake(
    id = id,
    tanggal = tanggal ?: "-", jam = jam ?: "-", coordinates = coordinates ?: "-",
    magnitude = magnitude ?: "-", kedalaman = kedalaman ?: "-",
    wilayah = wilayah ?: "-", potensi = potensi ?: "-",
)

// Extensions.kt
fun List<Earthquake>.filterByWilayah(query: String): List<Earthquake> {
    val q = query.trim()
    return if (q.isEmpty()) this else filter { it.wilayah.contains(q, ignoreCase = true) }
}

fun Earthquake.hasTsunamiPotential(): Boolean =
    potensi.contains("tsunami", ignoreCase = true) &&
        !potensi.contains("tidak", ignoreCase = true)

enum class Severity { MINOR, MODERATE, STRONG, MAJOR, UNKNOWN }

fun Double?.toSeverity(): Severity = when {
    this == null -> Severity.UNKNOWN
    this < 5.0 -> Severity.MINOR
    this < 6.0 -> Severity.MODERATE
    this < 7.0 -> Severity.STRONG
    else -> Severity.MAJOR
}
```

### 14.5 API, Repository, UiState

```kotlin
interface ApiService {
    @GET("DataMKG/TEWS/gempaterkini.json")
    suspend fun getGempaTerkini(): GempaResponseDto
}

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://data.bmkg.go.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class GempaRepository(private val api: ApiService = RetrofitInstance.api) {
    suspend fun getGempaTerkini(): List<Earthquake> =
        api.getGempaTerkini().infogempa?.gempa.orEmpty()
            .mapIndexed { index, dto -> dto.toEarthquake(index) }
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val data: List<Earthquake>) : UiState
    data class Error(val message: String) : UiState
}
```

### 14.6 `HomeViewModel`

```kotlin
class HomeViewModel(
    private val repository: GempaRepository = GempaRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /** Daftar yang sudah difilter (hanya bermakna saat Success). */
    val visibleList: StateFlow<List<Earthquake>> =
        combine(_uiState, _query) { state, q ->
            (state as? UiState.Success)?.data?.filterByWilayah(q).orEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                UiState.Success(repository.getGempaTerkini())
            } catch (e: IOException) {
                UiState.Error("Tidak dapat terhubung. Periksa koneksi internet Anda.")
            } catch (e: HttpException) {
                UiState.Error("Server BMKG bermasalah (kode ${e.code()}).")
            } catch (e: Exception) {
                UiState.Error("Terjadi kesalahan: ${e.localizedMessage ?: "tidak diketahui"}")
            }
        }
    }

    fun onQueryChange(newQuery: String) { _query.value = newQuery }

    fun findById(id: Int): Earthquake? =
        (uiState.value as? UiState.Success)?.data?.firstOrNull { it.id == id }
}
```

### 14.7 Navigasi

```kotlin
object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{id}"
    fun detail(id: Int) = "detail/$id"
}

@Composable
fun GetarNavHost() {
    val navController = rememberNavController()
    val viewModel: HomeViewModel = viewModel()   // scope Activity, dibagi ke dua layar

    NavHost(navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(viewModel = viewModel,
                onItemClick = { navController.navigate(Routes.detail(it.id)) })
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            DetailScreen(earthquake = viewModel.findById(id),
                onBack = { navController.popBackStack() })
        }
    }
}
```

### 14.8 `HomeScreen` (inti logika tampilan)

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, onItemClick: (Earthquake) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val list by viewModel.visibleList.collectAsState()

    Scaffold(topBar = { GetarTopBar(title = "Getar", subtitle = "Gempa Terkini BMKG") }) { padding ->
        Column(Modifier.padding(padding)) {
            SearchField(query, viewModel::onQueryChange, enabled = uiState is UiState.Success)
            when (val state = uiState) {
                is UiState.Loading -> LoadingView("Memuat data gempa…")
                is UiState.Error -> ErrorView(state.message, onRetry = viewModel::load)
                is UiState.Success ->
                    if (list.isEmpty()) EmptyView(query) { viewModel.onQueryChange("") }
                    else LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(list, key = { it.id }) { item ->
                            EarthquakeItem(item, onClick = onItemClick)
                        }
                    }
            }
        }
    }
}
```

### 14.9 `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
<application
    android:label="Getar"
    android:theme="@style/Theme.Getar" ... />
```

## 15. Pemetaan Fitur Kotlin

| Fitur | Penerapan |
|---|---|
| **Data class** | `GempaResponseDto`, `InfogempaDto`, `GempaDto`, `Earthquake`, `SeverityColors`, `UiState.Success/Error` |
| **Null safety** | Field DTO nullable; `?.`, `?:`, `orEmpty()`, `toDoubleOrNull()`, `as?` |
| **Lambda** | `onItemClick: (Earthquake) -> Unit`, `onQueryChange`, `onRetry`, `filter { }`, `mapIndexed { }` |
| **Extension function** | `List<Earthquake>.filterByWilayah()`, `Earthquake.hasTsunamiPotential()`, `Double?.toSeverity()`, `GempaDto.toEarthquake()`, `MaterialTheme.severity` |
| **Sealed interface** | `UiState` |
| **Coroutines/Flow** | `viewModelScope`, `StateFlow`, `combine`, `stateIn` |

## 16. Penanganan State dan Edge Case

| Kasus | Perilaku |
|---|---|
| Tanpa internet | `IOException` → Error: "Tidak dapat terhubung…" + Coba Lagi |
| HTTP error (4xx/5xx) | `HttpException` → Error dengan kode |
| JSON tak terduga / field null | Field nullable + fallback `"-"`; `infogempa?.gempa.orEmpty()` mencegah crash |
| Daftar kosong dari API | Success dengan daftar kosong → EmptyView (pesan "Belum ada data gempa") |
| Query tidak cocok | EmptyView "Tidak ada gempa untuk “{query}”" |
| Magnitudo bukan angka | Badge abu-abu (severity UNKNOWN) |
| Rotasi layar | State di ViewModel bertahan; `rememberLazyListState` menjaga posisi scroll |
| Retry saat Loading | Tombol retry hanya muncul di state Error (hindari request ganda) |
| Detail dengan id tak valid | Tampilkan EmptyView/ErrorView ringan + tombol back |
| Back dari Detail | Kembali ke Home dengan query dan scroll tetap |

## 17. Copywriting / Teks UI

| Konteks | Teks |
|---|---|
| Judul Home | Getar |
| Subjudul | Gempa Terkini BMKG |
| Hint search | Cari wilayah, mis. "Maluku" |
| Loading | Memuat data gempa… |
| Error koneksi | Tidak dapat terhubung. Periksa koneksi internet Anda. |
| Tombol retry | Coba Lagi |
| Kosong (filter) | Tidak ada gempa untuk “{query}” |
| Tombol hapus filter | Hapus pencarian |
| Judul Detail | Detail Gempa |
| Label Detail | Tanggal · Jam · Coordinates · Magnitudo · Kedalaman · Wilayah · Potensi |
| Content description back | Kembali |
| Content description clear | Hapus pencarian |

Gunakan `strings.xml` untuk semua teks statis (hindari hardcode di composable).

## 18. Aksesibilitas

- Kontras teks terhadap latar ≥ 4.5:1 (palet di atas dirancang memenuhi ini).
- Target sentuh ≥ 48dp.
- `contentDescription` pada semua ikon interaktif; ikon dekoratif memakai `null`.
- Status tidak hanya dibedakan lewat warna: lencana severity selalu disertai angka, kartu Potensi disertai ikon dan teks.
- Ukuran teks memakai `sp` agar mengikuti pengaturan font sistem.
- Urutan fokus/TalkBack: TopBar → Search → daftar.

## 19. Rencana Pengujian

### 19.1 Uji manual

| # | Skenario | Hasil yang diharapkan |
|---|---|---|
| 1 | Buka aplikasi dengan internet | Loading → daftar tampil |
| 2 | Buka aplikasi dengan mode pesawat | Error + Coba Lagi; setelah internet menyala, retry berhasil |
| 3 | Ketik wilayah yang ada | Daftar terfilter |
| 4 | Ketik wilayah yang tidak ada | EmptyView |
| 5 | Ketik huruf besar/kecil campur | Hasil sama (case-insensitive) |
| 6 | Hapus query (tombol ×) | Daftar penuh kembali |
| 7 | Klik item | Detail menampilkan 7 field benar |
| 8 | Back dari Detail | Home dengan query & scroll tetap |
| 9 | Rotasi layar di Home dan Detail | Tidak crash, state bertahan |
| 10 | Ganti ke dark mode | Warna sesuai palet dark, kontras terbaca |
| 11 | Perbesar font sistem | Layout tidak terpotong |
| 12 | Cek tanpa Coil/Glide di `build.gradle` | Terpenuhi |

### 19.2 Uji unit (opsional, nilai tambah)
- `filterByWilayah` (query kosong, huruf besar/kecil, spasi).
- `toSeverity` (batas 5.0, 6.0, 7.0, null).
- `hasTsunamiPotential` (dua kalimat Potensi umum).
- Catatan: dependensi tes (`junit`) tidak termasuk batasan library produksi, tetapi tetap konfirmasikan ke asisten praktikum bila ragu.

## 20. Dependensi dan Konfigurasi Proyek

```kotlin
// build.gradle.kts (module) — contoh; sesuaikan versi dengan Compose BOM proyek
dependencies {
    // Default template Compose
    implementation(platform("androidx.compose:compose-bom:<versi>"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:<versi>")
    implementation("androidx.core:core-ktx:<versi>")

    // Library yang diizinkan
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.navigation:navigation-compose:<versi>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:<versi>")
}
```

| Item | Nilai |
|---|---|
| Min SDK | 24 |
| Kotlin / AGP | Versi dari template Android Studio terbaru |
| Icons | Hanya `Icons.Default`/`Icons.AutoMirrored` dari `material-icons-core` (sudah termasuk material3); hindari `material-icons-extended` |
| Font | Berkas `.ttf` di `res/font/` |
| Cleartext traffic | Tidak diperlukan (HTTPS) |

## 21. Deliverables: README dan Video

### 21.1 Isi `README.md`
1. Judul, logo/ikon, tagline.
2. **Screenshot 2 layar** (Home dan Detail; disarankan tambahan: dark mode, error state).
3. **Fitur** — daftar fitur (daftar, search, loading/error, detail, tema light/dark).
4. **Arsitektur** — diagram MVVM + penjelasan layer dan struktur paket.
5. **API** — base URL, endpoint, struktur, field yang dipakai.
6. **Teknis** — versi Kotlin, min SDK, library, cara build/run, cara membuat APK debug.
7. Catatan keputusan desain (nama, warna, tipografi).
8. Identitas pembuat (nama, NIM, kelas/shift).

### 21.2 APK debug
`Build > Build Bundle(s) / APK(s) > Build APK(s)` → `app/build/outputs/apk/debug/app-debug.apk`; lampirkan di GitHub Releases atau folder repo.

### 21.3 Naskah video penjelasan kode (fokus kode, bukan demo)

| Menit | Topik | Yang ditunjukkan |
|---|---|---|
| 0:00–0:30 | Pembukaan | Nama aplikasi, tujuan, struktur proyek |
| 0:30–1:30 | Kotlin | Data class, null safety, lambda, extension function |
| 1:30–3:00 | Networking | `ApiService`, `RetrofitInstance`, `Repository`, DTO, mapper, izin INTERNET |
| 3:00–4:30 | MVVM & state | `UiState`, `HomeViewModel`, `StateFlow`, `combine` untuk search, retry |
| 4:30–6:30 | UI Compose | `Scaffold`, `LazyColumn`, komponen reusable, `when(uiState)` |
| 6:30–7:30 | Tema | `Color.kt`, `Theme.kt` (light/dark), `Type.kt` (override) |
| 7:30–8:30 | Navigasi | `NavHost`, rute `detail/{id}`, berbagi ViewModel |
| 8:30–9:00 | Penutup | Ringkasan dan hal yang dipelajari |

## 22. Pemetaan Persyaratan Tugas ke Fitur

| Persyaratan tugas | Dipenuhi oleh |
|---|---|
| Kotlin: data class, null safety, lambda, extension | Bagian 14 & 15 |
| Jetpack Compose, composable layout, lazy layout, reusable composable | `HomeScreen`, `LazyColumn`, komponen bagian 11 |
| Material 3 Theme & Typography; modifikasi `Color.kt`, `Theme.kt`, `Type.kt` (≥ 2 override) | Bagian 10 & 14.1–14.3 (8 override) |
| `Scaffold` + `TopAppBar` | `GetarTopBar` di kedua layar |
| `LazyColumn` saja; data dari API; Tanggal, Magnitudo, Wilayah; tanpa gambar | `EarthquakeItem`, FR-04 |
| State-driven UI: search lokal, loading, error | `HomeViewModel`, FR-03/08/09 |
| Networking BMKG tanpa API key; Retrofit + gson; izin INTERNET | Bagian 13, 14.5, 14.9 |
| Library hanya 4 yang diizinkan | Bagian 20 |
| MVVM + Repository + sealed UiState + StateFlow; tanpa API di Composable | Bagian 12, 14.5–14.6 |
| Maks 2 screen via `navigation-compose` | Bagian 8, 14.7 |
| Home: judul, search bar, daftar; klik → Detail | Bagian 9.1 |
| Detail: 7 field + tombol back | Bagian 9.2, FR-06/07 |
| GitHub + README + APK debug + video | Bagian 21 |

## 23. Kriteria Penerimaan (Definition of Done)

**Fungsional**
- [ ] Data gempa tampil dari API BMKG.
- [ ] Search memfilter lokal berdasarkan Wilayah (case-insensitive).
- [ ] State Loading, Error (dengan retry), Success, dan Empty tampil benar.
- [ ] Klik item membuka Detail; 7 field lengkap; tombol back berfungsi.

**Teknis**
- [ ] Hanya 2 screen via `navigation-compose`.
- [ ] `LazyColumn` dipakai untuk daftar; tanpa gambar.
- [ ] MVVM dengan Repository, sealed `UiState`, `StateFlow`.
- [ ] Tidak ada panggilan API di Composable.
- [ ] `Color.kt`, `Theme.kt` (light/dark), `Type.kt` (≥ 2 override) dimodifikasi; `Scaffold` + `TopAppBar` dipakai.
- [ ] Tidak ada library di luar daftar izin; tidak ada Coil/Glide.
- [ ] Izin `INTERNET` ada di manifest.
- [ ] Tidak ada warna hardcode di composable (semua dari tema).

**Pengumpulan**
- [ ] Repo GitHub publik + `README.md` lengkap (screenshot 2 screen).
- [ ] APK debug tersedia.
- [ ] Video penjelasan kode diunggah.
- [ ] Tautan GitHub dan video dikirim lewat form ≤ 24 jam setelah shift.

## 24. Risiko dan Mitigasi

| Risiko | Dampak | Mitigasi |
|---|---|---|
| Struktur/field JSON berbeda dari dugaan | Parsing gagal | Cek respons asli di browser; DTO nullable; `@SerializedName` |
| Gson mengisi `null` pada tipe non-null | Crash `NullPointerException` | Semua field DTO nullable + mapper |
| Font belum tersedia di perangkat/proyek | Build error | Siapkan berkas `.ttf` dulu; cadangan `FontFamily.SansSerif/Monospace` |
| Kebiasaan memakai `collectAsStateWithLifecycle` | Melanggar batasan library | Pakai `collectAsState()` |
| Waktu terbatas saat shift offline | Fitur tidak selesai | Ikuti urutan pengerjaan; fitur "Disarankan" dikerjakan terakhir |
| Berbagi ViewModel antar layar keliru | Detail kosong | `viewModel()` di level `NavHost`; fallback jika id tidak ditemukan |
| API BMKG lambat/down saat penilaian | Tampil error | Error state dirancang baik; siapkan screenshot & rekaman |

## 25. Rencana Pengerjaan

| Tahap | Pekerjaan | Perkiraan |
|---|---|---|
| 1 | Setup proyek, dependensi, izin INTERNET, aset font | 15 menit |
| 2 | DTO, model, mapper, `ApiService`, Retrofit, Repository | 25 menit |
| 3 | `UiState`, `HomeViewModel` (load, retry, query, filter) | 25 menit |
| 4 | Tema: `Color.kt`, `Type.kt`, `Theme.kt` | 20 menit |
| 5 | Komponen reusable + `HomeScreen` | 40 menit |
| 6 | `DetailScreen` + `NavHost` | 25 menit |
| 7 | Uji manual (daftar bagian 19.1), perbaikan | 20 menit |
| 8 | Build APK, screenshot, README | 20 menit |
| 9 | Rekam video penjelasan kode, unggah, kumpul form | 30 menit |

*Perkiraan bersifat indikatif; sesuaikan dengan durasi shift.*
