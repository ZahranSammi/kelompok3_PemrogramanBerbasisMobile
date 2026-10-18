# Product Requirements Document (PRD): Aplikasi Profil Interaktif Kelompok 3

## 1. Ringkasan Eksekutif & Tujuan Produk

Aplikasi Profil Interaktif Kelompok 3 adalah aplikasi mobile berbasis Android yang dibangun menggunakan **Kotlin** dan **Jetpack Compose** untuk memenuhi tugas mata kuliah pengembangan aplikasi mobile. Aplikasi ini menyajikan pengenalan anggota Kelompok 3 secara visual, interaktif, dan menyenangkan (*Fun & Light Theme*) melalui kombinasi teks identitas kelompok, animasi 3D interaktif, serta bagan struktur anggota yang dapat diklik untuk menampilkan detail profil masing-masing anggota.

### Tujuan Utama
- Menampilkan identitas **Kelompok 3** secara kreatif melalui antarmuka modern berbasis Material Design 3 yang ceria dan terang (*Fun & Light*).
- Mengintegrasikan elemen **animasi 3D** pada halaman utama untuk meningkatkan daya tarik visual dan interaktivitas.
- Menyajikan **bagan struktur anggota kelompok** yang jelas dan responsif, di mana pengguna dapat mengetuk (*tap*) setiap node anggota untuk membuka rincian biodata lengkap.
- Menerapkan praktik terbaik arsitektur pengembangan Android modern menggunakan **Kotlin**, **Jetpack Compose**, state management berbasis **ViewModel & StateFlow**, serta animasi deklaratif Compose.

## 2. Target Pengguna & Konteks Penggunaan

| Kategori Pengguna | Deskripsi & Kebutuhan |
| :--- | :--- |
| **Dosen & Asisten Pengajar** | Menilai kelengkapan fitur, kualitas antarmuka (UI/UX), kelancaran animasi 3D, struktur kode Jetpack Compose, dan kejelasan informasi anggota Kelompok 3. |
| **Rekan Mahasiswa** | Melihat profil anggota Kelompok 3 dengan pengalaman navigasi yang seru, interaktif, dan tidak membosankan. |
| **Anggota Kelompok 3** | Menggunakan aplikasi sebagai portofolio mini dan sarana demonstrasi kemampuan implementasi UI deklaratif di Android. |

## 3. Ruang Lingkup Fitur (Functional Requirements)

| ID Fitur | Nama Fitur | Prioritas | Deskripsi Fungsional | Kriteria Penerimaan (Acceptance Criteria) |
| :--- | :--- | :--- | :--- | :--- |
| **FR-01** | **Header Identitas Kelompok 3** | P0 (Wajib) | Menampilkan badge dan judul utama "Kelompok 3" beserta subjudul singkat atau slogan kelompok di bagian atas halaman pertama. | Header tampil proporsional di bagian atas layar dengan tipografi yang ekspresif dan animasi masuk (*fade/slide-in*) saat aplikasi dibuka. |
| **FR-02** | **Area Animasi 3D Interaktif** | P0 (Wajib) | Menampilkan objek atau maskot 3D di bawah header Kelompok 3 yang bergerak secara otomatis (*idle floating/rotation*) dan merespons sentuhan pengguna (*touch-to-spin* atau *bounce*). | Objek 3D tampil halus (minimal 60 FPS), mendukung interaksi sentuh (rotasi atau efek membal), dan tidak menghalangi keterbacaan bagan di bawahnya. |
| **FR-03** | **Bagan Struktur Anggota (Interactive Chart)** | P0 (Wajib) | Menampilkan bagan hierarki atau jejaring berisikan kartu singkat (*node*) nama dan avatar/ilustrasi setiap anggota Kelompok 3 yang dihubungkan dengan garis konektor visual. | Setiap node menampilkan nama panggilan/lengkap dan peran singkat. Garis penghubung digambar secara presisi antar-node menggunakan Compose `Canvas`. |
| **FR-04** | **Interaksi Klik Node ke Detail Anggota** | P0 (Wajib) | Saat pengguna menekan salah satu node nama anggota pada bagan, aplikasi menampilkan detail data lengkap anggota tersebut (melalui *Modal Bottom Sheet* ekspansif atau *Shared Element Transition* ke layar detail). | Mengetuk node memicu efek animasi tekan (*scale bounce*) lalu membuka tampilan detail berisi data lengkap anggota tanpa *lag*. |
| **FR-05** | **Tampilan Data Lengkap Anggota** | P0 (Wajib) | Menampilkan atribut lengkap anggota: Nama Lengkap, NIM, Peran di Kelompok, Program Studi/Kelas, Hobi/Minat, Motto/Quote, serta tautan kontak/media sosial. | Seluruh atribut data tampil rapi dalam kartu berdesain *fun & light*, dilengkapi tombol tutup/kembali dan aksi salin NIM atau buka tautan profil. |
| **FR-06** | **Filter & Highlight Peran pada Bagan** | P1 (Opsional) | Menyediakan *chip* filter cepat untuk menyoroti anggota berdasarkan peran (misal: Ketua, UI/UX, Developer) langsung pada bagan. | Saat chip dipilih, node yang sesuai memberikan efek *glow/pulse* pada bagan. |

## 4. Alur Pengguna (User Flow)

1. **Peluncuran Aplikasi (Launch & Hero State)**
   - Pengguna membuka aplikasi dan langsung disambut oleh **Halaman Utama (Home Screen)** bertema terang (*Light Mode*) dengan aksen warna pastel dan cerah.
   - Bagian atas menampilkan judul **"Kelompok 3"** diikuti oleh komponen **Animasi 3D** yang berputar lembut di tengah area hero.
2. **Eksplorasi Bagan Kelompok (Chart Exploration)**
   - Di bawah area animasi 3D, pengguna melihat **Bagan Struktur Kelompok 3** yang memuat node setiap anggota (Ketua Kelompok di puncak bagan dan anggota lainnya terhubung di bawahnya melalui garis konektor).
   - Pengguna dapat berinteraksi dengan animasi 3D (menggeser untuk memutar objek 3D atau mengetuk untuk efek lompatan) maupun menggulir layar untuk melihat seluruh bagan.
3. **Melihat Detail Data Anggota (Member Detail Inspection)**
   - Pengguna mengetuk salah satu kotak/node nama anggota pada bagan.
   - Node yang diklik memberikan umpan balik visual (*bouncy scale animation*) dan membuka panel/halaman **Detail Anggota**.
   - Pengguna membaca informasi lengkap anggota (Foto/Avatar, Nama Lengkap, NIM, Peran, Hobi, Quote, Media Sosial).
   - Pengguna menutup detail (geser ke bawah pada *Bottom Sheet* atau menekan tombol kembali) untuk kembali ke bagan utama.

## 5. Spesifikasi Teknis & Arsitektur

### Stack Teknologi
- **Bahasa Pemrograman**: Kotlin (versi 2.0+)
- **UI Framework**: Jetpack Compose (Material 3)
- **Minimum SDK**: API 26 (Android 8.0) | **Target SDK**: API 35 (Android 15)
- **Arsitektur**: MVVM (Model-View-ViewModel) dengan Unidirectional Data Flow (UDF)
- **Opsi Implementasi Animasi 3D**:
  - **Opsi A (Native 3D Model `.glb`/`.gltf`)**: Menggunakan library `io.github.sceneview:sceneview` berbasis Google Filament untuk merender model 3D bertekstur *clay/toy* yang dapat diputar dengan gestur jari.
  - **Opsi B (Lottie 3D Render)**: Menggunakan `com.airbnb.android:lottie-compose` dengan aset animasi vektor/3D bergaya *claymorphism* yang ringan dan tidak membutuhkan *shader* GPU berat.
  - **Opsi C (Pure Compose 3D Gyro/Touch Card)**: Menggunakan `Modifier.graphicsLayer { rotationX; rotationY; cameraDistance }` dikombinasikan dengan `Canvas` berlapis untuk efek paralaks 3D murni tanpa library eksternal.

### Struktur Model Data (Kotlin Data Class)

```kotlin
package com.kelompok3.profileapp.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class MemberProfile(
    val id: String,
    val fullName: String,
    val nickName: String,
    val nim: String,
    val role: String,
    val isLeader: Boolean = false,
    val major: String,
    val hobby: String,
    val funFact: String,
    val quote: String,
    val instagramHandle: String,
    val githubUrl: String,
    val accentColorHex: Long,
    @DrawableRes val avatarRes: Int
)
```

## 6. Kebutuhan Non-Fungsional (Non-Functional Requirements)

- **Performa Render & Animasi**: Seluruh transisi antar-state, garis bagan `Canvas`, dan animasi 3D harus berjalan mulus pada 60 FPS tanpa *dropped frames* yang terlihat.
- **Responsivitas Layar**: Tata letak bagan wajib mendukung berbagai ukuran layar ponsel Android (mulai dari layar kompak 5.5 inci hingga layar besar 6.8 inci) dengan dukungan *vertical scroll* yang halus.
- **Ketersediaan Offline (Offline-First)**: Seluruh data anggota kelompok, aset gambar/avatar, dan file model 3D disimpan secara lokal di dalam *bundle* aplikasi (`assets/` dan `res/drawable/`) sehingga aplikasi dapat berjalan penuh tanpa koneksi internet saat demonstrasi tugas di kelas.
- **Keterbacaan & Kontras (Accessibility)**: Meskipun menggunakan tema *Fun & Light* dengan warna-warna ceria, teks utama di atas latar belakang terang wajib mempertahankan rasio kontras minimal WCAG AA (4.5:1) agar tetap mudah dibaca pada proyektor kelas maupun layar ponsel.

## 7. Struktur Direktori Proyek

```text
app/src/main/java/com/kelompok3/profileapp/
├── MainActivity.kt
├── data/
│   └── Kelompok3Repository.kt       // Sumber data statis anggota Kelompok 3
├── model/
│   └── MemberProfile.kt             // Data class struktur biodata anggota
├── ui/
│   ├── components/
│   │   ├── Hero3DSection.kt         // Komponen animasi 3D interaktif + Header Kelompok 3
│   │   ├── OrgChartSection.kt       // Bagan interaktif dengan garis Canvas & node anggota
│   │   ├── MemberNodeCard.kt        // Kartu nama singkat pada bagan
│   │   └── MemberDetailSheet.kt     // Modal Bottom Sheet / Detail Screen biodata anggota
│   ├── screens/
│   │   ├── HomeScreen.kt            // Halaman utama aplikasi
│   │   └── HomeViewModel.kt         // State holder (selectedMember, animationState)
│   └── theme/
│       ├── Color.kt                 // Palet warna Fun & Light
│       ├── Type.kt                  // Konfigurasi font rounded (Plus Jakarta Sans / Fredoka)
│       └── Theme.kt                 // Konfigurasi MaterialTheme LightColorScheme
```
