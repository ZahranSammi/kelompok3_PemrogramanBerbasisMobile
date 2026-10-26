# Aplikasi Pencatatan Keuangan Pribadi (Android)

Aplikasi mobile Android berbasis **Kotlin** untuk pencatatan dan pengelolaan keuangan pribadi secara cerdas, otomatis, dan terstruktur. Menggunakan database lokal **SQLite** relasional (`pengguna`, `kategori`, `transaksi`) dan antarmuka modern bertema pastel **Lavender** yang responsif terhadap *display cutout* (kamera depan / punch-hole).

---
SI KEREN
## Fitur Utama

1. **Mencatat Pemasukan dan Pengeluaran**:
   - Pencatatan transaksi baru dengan tipe **Pemasukan (+)** atau **Pengeluaran (-)**.
   - Pilihan kategori dinamis sesuai jenis transaksi.
   - Pemilih tanggal transaksi (*DatePickerDialog*).
   - Edit transaksi dan hapus transaksi dengan dialog konfirmasi.
   - Filter cepat transaksi berdasarkan tipe (Semua, Pemasukan, Pengeluaran) dan per kategori.

2. **Mengelompokkan Transaksi**:
   - Layar **Select Category** dengan grid 4 kolom modern.
   - Kolom pencarian kategori instan (*Search for Categories*).
   - Tombol **+ Tambah Kategori** untuk menambah kategori kustom baru.
   - Agregasi pengelompokan transaksi per kategori lengkap dengan jumlah transaksi, total nominal, dan *progress bar* persentase.

3. **Menghitung Saldo Otomatis**:
   - Kartu **Spending Wallet** menghitung saldo secara otomatis:
     $$\text{Total Saldo} = \text{Saldo Awal Pengguna} + \text{Total Pemasukan} - \text{Total Pengeluaran}$$
   - Perhitungan selalu diperbarui secara real-time setiap kali ada transaksi yang ditambahkan, diubah, atau dihapus.
   - Hero dashboard menampilkan ringkasan pengeluaran bulan berjalan (*This Month Spend*).

4. **Menampilkan Laporan Bulanan (Analytics)**:
   - Navigasi bulan & tahun sebelumnya/berikutnya.
   - Grafik batang perbandingan 6 bulan (*Income vs Expense*) yang digambar khusus (*custom canvas* `MonthlyBarChartView`).
   - Kartu ganda rincian **Total Pemasukan (Income)** dan **Total Pengeluaran (Expenses)**.
   - Status arus kas bulanan (*Surplus* / *Defisit*), selisih bulanan, dan rasio pengeluaran.
   - Rincian daftar transaksi dan pengelompokan kategori khusus pada bulan yang dipilih.

5. **Pengelolaan Database & Multi-Pengguna**:
   - **Tabel `pengguna`**: Menyimpan data pengguna, email, dan saldo awal.
   - **Tabel `kategori`**: Menyimpan kategori pemasukan dan pengeluaran beserta ikon emoji dan warna.
   - **Tabel `transaksi`**: Menyimpan transaksi dengan *foreign key* ke `pengguna` dan `kategori`.
   - Dialog **Account & Spending Wallet** untuk mengatur profil, menyesuaikan saldo awal, atau membuat pengguna baru.

6. **Desain UI Lavender & Safe Insets**:
   - Latar belakang gradien *pastel lavender* modern.
   - Penanganan otomatis `WindowInsetsCompat` untuk *status bar*, kamera depan (*display cutout* / punch-hole), dan *gesture navigation bar*.
   - Navigasi bawah dengan tombol aksi bulat *dark navy* `+` di tengah.

---

## Struktur Database SQLite

```sql
-- 1. Tabel Pengguna
CREATE TABLE pengguna (
    id_pengguna INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_pengguna TEXT NOT NULL,
    email TEXT NOT NULL,
    saldo_awal REAL NOT NULL DEFAULT 0
);

-- 2. Tabel Kategori
CREATE TABLE kategori (
    id_kategori INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_kategori TEXT NOT NULL,
    tipe_kategori TEXT NOT NULL,
    ikon TEXT NOT NULL,
    warna TEXT NOT NULL
);

-- 3. Tabel Transaksi
CREATE TABLE transaksi (
    id_transaksi INTEGER PRIMARY KEY AUTOINCREMENT,
    id_pengguna INTEGER NOT NULL,
    id_kategori INTEGER NOT NULL,
    tipe_transaksi TEXT NOT NULL,
    jumlah REAL NOT NULL,
    tanggal TEXT NOT NULL,
    keterangan TEXT NOT NULL,
    FOREIGN KEY (id_pengguna) REFERENCES pengguna(id_pengguna) ON DELETE CASCADE,
    FOREIGN KEY (id_kategori) REFERENCES kategori(id_kategori) ON DELETE RESTRICT
);
```

---

## Spesifikasi Teknis

- **Bahasa**: Kotlin
- **Komponen UI**: Material Components, ConstraintLayout, RecyclerView, CoordinatorLayout, Custom Canvas View
- **Database**: SQLite (`android.database.sqlite.SQLiteOpenHelper`)
- **Min SDK**: API 33 (Android 13)
- **Target / Compile SDK**: API 37 (Android 15)
- **Build System**: Gradle 9.4.1 (Kotlin DSL)

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
   File APK hasil build dapat ditemukan di `app/build/outputs/apk/debug/app-debug.apk`.

3. **Jalankan Pengujian Unit**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Install & Jalankan di Perangkat / Emulator**:
   ```bash
   ./gradlew installDebug
   ```
