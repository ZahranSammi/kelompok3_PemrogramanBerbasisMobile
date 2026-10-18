# Aplikasi Profil Interaktif Kelompok 3 (Android Jetpack Compose)

Aplikasi mobile Android berbasis **Kotlin** dan **Jetpack Compose (Material 3)** untuk profil dan pengenalan anggota **Kelompok 3** pada mata kuliah Pengembangan Aplikasi Mobile (Pemrograman Berbasis Mobile).

Mengusung tema **Playful Daylight**, aplikasi ini menyajikan antarmuka terang, bersih, dan taktil dengan animasi pembuka pesawat kertas, objek prisma 3D interaktif yang dapat diputar, bagan struktur organisasi adaptif, serta lembar rincian biodata anggota.

---

## Fitur Utama

1. **Animasi Pembuka Pesawat Kertas (Origami Intro)**:
   - Saat aplikasi dibuka, tampil selembar kertas bertuliskan **"Kelompok 3"**.
   - Kertas melipat secara bertahap menjadi pesawat kertas 3D, lalu meluncur terbang melintasi layar sebelum membuka halaman beranda.
   - Dilengkapi tombol **"Lewati"** untuk navigasi langsung serta tombol putar ulang di header beranda.

2. **Objek 3D Interaktif (Pure Jetpack Compose)**:
   - Panggung 3D interaktif bertekstur taktil dengan perspektif `cameraDistance` dan rotasi sumbu X/Y.
   - Mendukung gestur sentuh putar (*drag-to-rotate*) dengan animasi pegas kembali (*spring physics*), tombol putar sudut 3D, dan reset posisi.
   - Tanpa ketergantungan file 3D eksternal; 100% *offline-first*.

3. **Bagan Struktur Organisasi Tim**:
   - Bagan hierarki interaktif dengan garis penghubung kurva *cubic Bezier* pada Compose `Canvas`.
   - Menempatkan Ketua Kelompok di puncak bagan dan anggota di bawahnya.
   - Tata letak responsif 3 tingkat: 1 kolom (< 340dp), 2 kolom (340dp - 699dp), dan 4 kolom (>= 700dp).

4. **Filter Peran Cepat**:
   - Filter chip: **Semua**, **Ketua**, dan **Anggota** untuk menyoroti kartu anggota pada bagan secara langsung.

5. **Detail Biodata Lengkap (Modal Bottom Sheet)**:
   - Ketuk kartu anggota untuk membuka lembar detail biodata beranimasi pegas.
   - Menampilkan foto asli anggota resolusi tinggi, Nama Lengkap, NIM, Program Studi, No. WhatsApp, dan Alamat Email.
   - Aksi interaktif: **Salin NIM**, **Hubungi WhatsApp** (membuka `wa.me`), dan **Kirim Email** (`mailto`).

6. **Panel Pengujian Status UI (Resilience)**:
   - Menyediakan pengalih status UI untuk menguji tampilan **Data Siap**, **Status Memuat**, **Status Kosong**, dan **Status Galat**.

---

## Data Anggota Kelompok 3

| No | Nama Lengkap | NIM | Jabatan | Program Studi |
|:--:|:---|:---:|:---:|:---|
| 1 | **Parisha Alea Rizkianu Tigusti** | 2024240002 | Ketua Kelompok | Sistem Informasi |
| 2 | **Wibi Dwi Andriyanto** | 2024240009 | Anggota | Sistem Informasi |
| 3 | **Dewi Anggreayani** | 2024240022 | Anggota | Sistem Informasi |
| 4 | **Faiza Fitri Farikhatusshifa** | 2024240016 | Anggota | Sistem Informasi |
| 5 | **Zahran Sammi** | 2024240018 | Anggota | Sistem Informasi |

---

## Spesifikasi Teknis

- **Bahasa**: Kotlin 2.2.10
- **Framework UI**: Jetpack Compose (Material 3)
- **Min SDK**: API 26 (Android 8.0)
- **Target / Compile SDK**: API 37 (Android 15)
- **Arsitektur**: MVVM (Model-View-ViewModel) dengan StateFlow & Unidirectional Data Flow (UDF)
- **Kompilasi Build**: Gradle 9.6.0 & AGP 9.4.1

---

## Cara Menjalankan Proyek

1. **Clone Repositori**:
   ```bash
   git clone https://github.com/ZahranSammi/kelompok3_PemrogramanBerbasisMobile.git
   cd kelompok3_PemrogramanBerbasisMobile
   ```

2. **Kompilasi & Build APK**:
   ```bash
   ./gradlew assembleDebug
   ```
   File APK hasil kompilasi berada di `app/build/outputs/apk/debug/app-debug.apk`.

3. **Jalankan Pengujian Unit**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Install ke Perangkat / Emulator**:
   ```bash
   ./gradlew installDebug
   ```
