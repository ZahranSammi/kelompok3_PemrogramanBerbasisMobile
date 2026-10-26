package com.example.aplikasipencatatankeuanganpribadi.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aplikasipencatatankeuanganpribadi.model.DataBulananChart
import com.example.aplikasipencatatankeuanganpribadi.model.Kategori
import com.example.aplikasipencatatankeuanganpribadi.model.LaporanBulanan
import com.example.aplikasipencatatankeuanganpribadi.model.Pengguna
import com.example.aplikasipencatatankeuanganpribadi.model.RingkasanKategori
import com.example.aplikasipencatatankeuanganpribadi.model.RingkasanSaldo
import com.example.aplikasipencatatankeuanganpribadi.model.TipeTransaksi
import com.example.aplikasipencatatankeuanganpribadi.model.Transaksi
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils
import java.util.Calendar
import java.util.Locale

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "keuangan_pribadi.db"
        const val DATABASE_VERSION = 2

        // Tabel Pengguna
        const val TABLE_PENGGUNA = "pengguna"
        const val COL_ID_PENGGUNA = "id_pengguna"
        const val COL_NAMA_PENGGUNA = "nama_pengguna"
        const val COL_EMAIL = "email"
        const val COL_SALDO_AWAL = "saldo_awal"

        // Tabel Kategori
        const val TABLE_KATEGORI = "kategori"
        const val COL_ID_KATEGORI = "id_kategori"
        const val COL_NAMA_KATEGORI = "nama_kategori"
        const val COL_TIPE_KATEGORI = "tipe_kategori"
        const val COL_IKON = "ikon"
        const val COL_WARNA = "warna"

        // Tabel Transaksi
        const val TABLE_TRANSAKSI = "transaksi"
        const val COL_ID_TRANSAKSI = "id_transaksi"
        const val COL_FK_PENGGUNA = "id_pengguna"
        const val COL_FK_KATEGORI = "id_kategori"
        const val COL_TIPE_TRANSAKSI = "tipe_transaksi"
        const val COL_JUMLAH = "jumlah"
        const val COL_TANGGAL = "tanggal"
        const val COL_KETERANGAN = "keterangan"
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTablePengguna = """
            CREATE TABLE $TABLE_PENGGUNA (
                $COL_ID_PENGGUNA INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAMA_PENGGUNA TEXT NOT NULL,
                $COL_EMAIL TEXT NOT NULL,
                $COL_SALDO_AWAL REAL NOT NULL DEFAULT 0
            )
        """.trimIndent()

        val createTableKategori = """
            CREATE TABLE $TABLE_KATEGORI (
                $COL_ID_KATEGORI INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAMA_KATEGORI TEXT NOT NULL,
                $COL_TIPE_KATEGORI TEXT NOT NULL,
                $COL_IKON TEXT NOT NULL,
                $COL_WARNA TEXT NOT NULL
            )
        """.trimIndent()

        val createTableTransaksi = """
            CREATE TABLE $TABLE_TRANSAKSI (
                $COL_ID_TRANSAKSI INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FK_PENGGUNA INTEGER NOT NULL,
                $COL_FK_KATEGORI INTEGER NOT NULL,
                $COL_TIPE_TRANSAKSI TEXT NOT NULL,
                $COL_JUMLAH REAL NOT NULL,
                $COL_TANGGAL TEXT NOT NULL,
                $COL_KETERANGAN TEXT NOT NULL,
                FOREIGN KEY ($COL_FK_PENGGUNA) REFERENCES $TABLE_PENGGUNA($COL_ID_PENGGUNA) ON DELETE CASCADE,
                FOREIGN KEY ($COL_FK_KATEGORI) REFERENCES $TABLE_KATEGORI($COL_ID_KATEGORI) ON DELETE RESTRICT
            )
        """.trimIndent()

        db.execSQL(createTablePengguna)
        db.execSQL(createTableKategori)
        db.execSQL(createTableTransaksi)

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Melindungi data keuangan pengguna dari penghapusan massal saat schema di-upgrade (OWASP A04 Insecure Design)
        // Hindari DROP TABLE pada lingkungan produksi. Migrasi dilakukan bertahap di sini.
        if (oldVersion < 2) {
            // Migrasi masa lalu jika ada
        }
    }

    private fun escapeLikePattern(input: String): String {
        return input.replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
    }

    private val dateFormatRegex = Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    private val emailFormatRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val maxFinancialAmount = 1_000_000_000_000.0 // 1 Triliun Rupiah
    private val maxTextLength = 255

    private fun seedInitialData(db: SQLiteDatabase) {
        // 1. Seed Pengguna Default
        val userValues = ContentValues().apply {
            put(COL_NAMA_PENGGUNA, "Pengguna Utama")
            put(COL_EMAIL, "pengguna@keuanganpribadi.id")
            put(COL_SALDO_AWAL, 1500000.0)
        }
        val idPenggunaDefault = db.insert(TABLE_PENGGUNA, null, userValues)

        // 2. Seed Kategori (Ikon & Warna sesuai tema kartu modern)
        val daftarKategoriAwal = listOf(
            Kategori(namaKategori = "Gaji Bulanan", tipeKategori = TipeTransaksi.PEMASUKAN, ikon = "💵", warnaHex = "#8B5CF6"),
            Kategori(namaKategori = "Bonus & Tunjangan", tipeKategori = TipeTransaksi.PEMASUKAN, ikon = "🎁", warnaHex = "#10B981"),
            Kategori(namaKategori = "Usaha / Freelance", tipeKategori = TipeTransaksi.PEMASUKAN, ikon = "💼", warnaHex = "#3B82F6"),
            Kategori(namaKategori = "Investasi", tipeKategori = TipeTransaksi.PEMASUKAN, ikon = "📈", warnaHex = "#6366F1"),
            Kategori(namaKategori = "Belanja", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🛍️", warnaHex = "#10B981"),
            Kategori(namaKategori = "Perjalanan", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "✈️", warnaHex = "#06B6D4"),
            Kategori(namaKategori = "Transportasi", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🚗", warnaHex = "#3B82F6"),
            Kategori(namaKategori = "Rumah", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🏠", warnaHex = "#8B5CF6"),
            Kategori(namaKategori = "Asuransi", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🛡️", warnaHex = "#0EA5E9"),
            Kategori(namaKategori = "Pendidikan", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "📖", warnaHex = "#6366F1"),
            Kategori(namaKategori = "Makanan", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🍽️", warnaHex = "#F59E0B"),
            Kategori(namaKategori = "Internet", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "📶", warnaHex = "#8B5CF6"),
            Kategori(namaKategori = "Air & Listrik", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "💧", warnaHex = "#3B82F6"),
            Kategori(namaKategori = "Sewa / Kos", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🔑", warnaHex = "#F97316"),
            Kategori(namaKategori = "Olahraga / Gym", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🏋️", warnaHex = "#F59E0B"),
            Kategori(namaKategori = "Langganan", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🔔", warnaHex = "#A855F7"),
            Kategori(namaKategori = "Liburan", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "🌴", warnaHex = "#10B981"),
            Kategori(namaKategori = "Lainnya", tipeKategori = TipeTransaksi.PENGELUARAN, ikon = "✨", warnaHex = "#64748B")
        )

        val mapKategoriId = mutableMapOf<String, Long>()
        for (kat in daftarKategoriAwal) {
            val cv = ContentValues().apply {
                put(COL_NAMA_KATEGORI, kat.namaKategori)
                put(COL_TIPE_KATEGORI, kat.tipeKategori.name)
                put(COL_IKON, kat.ikon)
                put(COL_WARNA, kat.warnaHex)
            }
            val idKat = db.insert(TABLE_KATEGORI, null, cv)
            mapKategoriId[kat.namaKategori] = idKat
        }

        data class SeedTx(
            val offsetBulan: Int,
            val namaKategori: String,
            val tipe: TipeTransaksi,
            val jumlah: Double,
            val hari: String,
            val keterangan: String
        )

        // Seed transaksi untuk bulan berjalan dan beberapa bulan sebelumnya (agar grafik 6 bulan tampil kaya)
        val contohTransaksi = listOf(
            // Bulan berjalan (offset 0)
            SeedTx(0, "Gaji Bulanan", TipeTransaksi.PEMASUKAN, 6500000.0, "01", "Gaji Bulanan Kantor"),
            SeedTx(0, "Usaha / Freelance", TipeTransaksi.PEMASUKAN, 1500000.0, "05", "Proyek Desain Aplikasi"),
            SeedTx(0, "Langganan", TipeTransaksi.PENGELUARAN, 75000.0, "15", "Spotify & Cloud Subscription"),
            SeedTx(0, "Internet", TipeTransaksi.PENGELUARAN, 350000.0, "14", "Tagihan WiFi & Data Bulanan"),
            SeedTx(0, "Belanja", TipeTransaksi.PENGELUARAN, 480000.0, "13", "Belanja Kebutuhan Bulanan"),
            SeedTx(0, "Makanan", TipeTransaksi.PENGELUARAN, 320000.0, "12", "Makan Siang & Kopi"),
            // 1 bulan lalu
            SeedTx(-1, "Gaji Bulanan", TipeTransaksi.PEMASUKAN, 6500000.0, "01", "Gaji Bulanan"),
            SeedTx(-1, "Belanja", TipeTransaksi.PENGELUARAN, 2400000.0, "10", "Belanja Elektronik & Rumah"),
            SeedTx(-1, "Makanan", TipeTransaksi.PENGELUARAN, 950000.0, "18", "Konsumsi Bulanan"),
            // 2 bulan lalu
            SeedTx(-2, "Gaji Bulanan", TipeTransaksi.PEMASUKAN, 6500000.0, "01", "Gaji Bulanan"),
            SeedTx(-2, "Bonus & Tunjangan", TipeTransaksi.PEMASUKAN, 1200000.0, "15", "Bonus Kinerja"),
            SeedTx(-2, "Perjalanan", TipeTransaksi.PENGELUARAN, 1850000.0, "20", "Tiket & Perjalanan Dinas"),
            // 3 bulan lalu
            SeedTx(-3, "Gaji Bulanan", TipeTransaksi.PEMASUKAN, 6200000.0, "01", "Gaji Bulanan"),
            SeedTx(-3, "Rumah", TipeTransaksi.PENGELUARAN, 2100000.0, "08", "Perawatan Rumah"),
            // 4 bulan lalu
            SeedTx(-4, "Gaji Bulanan", TipeTransaksi.PEMASUKAN, 6200000.0, "01", "Gaji Bulanan"),
            SeedTx(-4, "Pendidikan", TipeTransaksi.PENGELUARAN, 1600000.0, "11", "Kursus & Buku"),
            // 5 bulan lalu
            SeedTx(-5, "Gaji Bulanan", TipeTransaksi.PEMASUKAN, 6000000.0, "01", "Gaji Bulanan"),
            SeedTx(-5, "Transportasi", TipeTransaksi.PENGELUARAN, 1400000.0, "14", "Servis Kendaraan & BBM")
        )

        for (tx in contohTransaksi) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, tx.offsetBulan)
            val tahun = cal.get(Calendar.YEAR)
            val bulan = cal.get(Calendar.MONTH) + 1
            val prefixBulan = String.format(Locale.US, "%04d-%02d", tahun, bulan)

            val idKat = mapKategoriId[tx.namaKategori] ?: continue
            val cv = ContentValues().apply {
                put(COL_FK_PENGGUNA, idPenggunaDefault)
                put(COL_FK_KATEGORI, idKat)
                put(COL_TIPE_TRANSAKSI, tx.tipe.name)
                put(COL_JUMLAH, tx.jumlah)
                put(COL_TANGGAL, "$prefixBulan-${tx.hari}")
                put(COL_KETERANGAN, tx.keterangan)
            }
            db.insert(TABLE_TRANSAKSI, null, cv)
        }
    }

    // ==================== OPERASI TABEL PENGGUNA ====================

    fun getSemuaPengguna(): List<Pengguna> {
        val list = mutableListOf<Pengguna>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PENGGUNA,
            null,
            null,
            null,
            null,
            null,
            "$COL_ID_PENGGUNA ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Pengguna(
                        idPengguna = it.getLong(it.getColumnIndexOrThrow(COL_ID_PENGGUNA)),
                        namaPengguna = it.getString(it.getColumnIndexOrThrow(COL_NAMA_PENGGUNA)),
                        email = it.getString(it.getColumnIndexOrThrow(COL_EMAIL)),
                        saldoAwal = it.getDouble(it.getColumnIndexOrThrow(COL_SALDO_AWAL))
                    )
                )
            }
        }
        return list
    }

    fun getPenggunaById(idPengguna: Long): Pengguna? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PENGGUNA,
            null,
            "$COL_ID_PENGGUNA = ?",
            arrayOf(idPengguna.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return Pengguna(
                    idPengguna = it.getLong(it.getColumnIndexOrThrow(COL_ID_PENGGUNA)),
                    namaPengguna = it.getString(it.getColumnIndexOrThrow(COL_NAMA_PENGGUNA)),
                    email = it.getString(it.getColumnIndexOrThrow(COL_EMAIL)),
                    saldoAwal = it.getDouble(it.getColumnIndexOrThrow(COL_SALDO_AWAL))
                )
            }
        }
        return null
    }

    fun tambahPengguna(nama: String, email: String, saldoAwal: Double): Long {
        val namaBersih = nama.trim()
        val emailBersih = email.trim()
        if (namaBersih.isBlank() || namaBersih.length > maxTextLength) return -1L
        if (emailBersih.isNotBlank() && !emailFormatRegex.matches(emailBersih)) return -1L
        if (!saldoAwal.isFinite() || saldoAwal < 0 || saldoAwal > maxFinancialAmount) return -1L

        return try {
            val db = writableDatabase
            val cv = ContentValues().apply {
                put(COL_NAMA_PENGGUNA, namaBersih)
                put(COL_EMAIL, if (emailBersih.isNotBlank()) emailBersih else "pengguna@keuanganpribadi.id")
                put(COL_SALDO_AWAL, saldoAwal)
            }
            db.insert(TABLE_PENGGUNA, null, cv)
        } catch (_: Exception) {
            -1L
        }
    }

    fun updatePengguna(pengguna: Pengguna): Int {
        val namaBersih = pengguna.namaPengguna.trim()
        val emailBersih = pengguna.email.trim()
        if (namaBersih.isBlank() || namaBersih.length > maxTextLength) return 0
        if (emailBersih.isNotBlank() && !emailFormatRegex.matches(emailBersih)) return 0
        if (!pengguna.saldoAwal.isFinite() || pengguna.saldoAwal < 0 || pengguna.saldoAwal > maxFinancialAmount) return 0

        return try {
            val db = writableDatabase
            val cv = ContentValues().apply {
                put(COL_NAMA_PENGGUNA, namaBersih)
                put(COL_EMAIL, if (emailBersih.isNotBlank()) emailBersih else "pengguna@keuanganpribadi.id")
                put(COL_SALDO_AWAL, pengguna.saldoAwal)
            }
            db.update(
                TABLE_PENGGUNA,
                cv,
                "$COL_ID_PENGGUNA = ?",
                arrayOf(pengguna.idPengguna.toString())
            )
        } catch (_: Exception) {
            0
        }
    }

    // ==================== OPERASI TABEL KATEGORI ====================

    fun getSemuaKategori(filterTipe: TipeTransaksi? = null, kataKunci: String? = null): List<Kategori> {
        val list = mutableListOf<Kategori>()
        val db = readableDatabase
        val conditions = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (filterTipe != null) {
            conditions.add("$COL_TIPE_KATEGORI = ?")
            args.add(filterTipe.name)
        }
        if (!kataKunci.isNullOrBlank()) {
            conditions.add("$COL_NAMA_KATEGORI LIKE ? ESCAPE '\\'")
            args.add("%${escapeLikePattern(kataKunci.trim())}%")
        }

        val selection = if (conditions.isNotEmpty()) conditions.joinToString(" AND ") else null
        val selectionArgs = if (args.isNotEmpty()) args.toTypedArray() else null

        val cursor = db.query(
            TABLE_KATEGORI,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_ID_KATEGORI ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Kategori(
                        idKategori = it.getLong(it.getColumnIndexOrThrow(COL_ID_KATEGORI)),
                        namaKategori = it.getString(it.getColumnIndexOrThrow(COL_NAMA_KATEGORI)),
                        tipeKategori = TipeTransaksi.fromString(
                            it.getString(it.getColumnIndexOrThrow(COL_TIPE_KATEGORI))
                        ),
                        ikon = it.getString(it.getColumnIndexOrThrow(COL_IKON)),
                        warnaHex = it.getString(it.getColumnIndexOrThrow(COL_WARNA))
                    )
                )
            }
        }
        return list
    }

    fun tambahKategori(
        namaKategori: String,
        tipeKategori: TipeTransaksi,
        ikon: String,
        warnaHex: String
    ): Long {
        val namaBersih = namaKategori.trim()
        if (namaBersih.isBlank() || namaBersih.length > 50) return -1L
        val warnaBersih = if (warnaHex.matches(Regex("^#[0-9a-fA-F]{6}$"))) warnaHex else "#8B5CF6"
        val ikonBersih = ikon.trim().take(4).ifBlank { if (tipeKategori == TipeTransaksi.PEMASUKAN) "💰" else "🛍️" }

        return try {
            val db = writableDatabase
            val cv = ContentValues().apply {
                put(COL_NAMA_KATEGORI, namaBersih)
                put(COL_TIPE_KATEGORI, tipeKategori.name)
                put(COL_IKON, ikonBersih)
                put(COL_WARNA, warnaBersih)
            }
            db.insert(TABLE_KATEGORI, null, cv)
        } catch (_: Exception) {
            -1L
        }
    }

    // ==================== OPERASI TABEL TRANSAKSI ====================

    fun tambahTransaksi(
        idPengguna: Long,
        idKategori: Long,
        tipeTransaksi: TipeTransaksi,
        jumlah: Double,
        tanggal: String,
        keterangan: String
    ): Long {
        if (!jumlah.isFinite() || jumlah <= 0 || jumlah > maxFinancialAmount) return -1L
        if (!dateFormatRegex.matches(tanggal)) return -1L
        val ketBersih = keterangan.trim().take(maxTextLength)

        return try {
            val db = writableDatabase
            val cv = ContentValues().apply {
                put(COL_FK_PENGGUNA, idPengguna)
                put(COL_FK_KATEGORI, idKategori)
                put(COL_TIPE_TRANSAKSI, tipeTransaksi.name)
                put(COL_JUMLAH, jumlah)
                put(COL_TANGGAL, tanggal)
                put(COL_KETERANGAN, ketBersih)
            }
            db.insert(TABLE_TRANSAKSI, null, cv)
        } catch (_: Exception) {
            -1L
        }
    }

    fun updateTransaksi(
        idTransaksi: Long,
        idKategori: Long,
        tipeTransaksi: TipeTransaksi,
        jumlah: Double,
        tanggal: String,
        keterangan: String
    ): Int {
        if (!jumlah.isFinite() || jumlah <= 0 || jumlah > maxFinancialAmount) return 0
        if (!dateFormatRegex.matches(tanggal)) return 0
        val ketBersih = keterangan.trim().take(maxTextLength)

        return try {
            val db = writableDatabase
            val cv = ContentValues().apply {
                put(COL_FK_KATEGORI, idKategori)
                put(COL_TIPE_TRANSAKSI, tipeTransaksi.name)
                put(COL_JUMLAH, jumlah)
                put(COL_TANGGAL, tanggal)
                put(COL_KETERANGAN, ketBersih)
            }
            db.update(
                TABLE_TRANSAKSI,
                cv,
                "$COL_ID_TRANSAKSI = ?",
                arrayOf(idTransaksi.toString())
            )
        } catch (_: Exception) {
            0
        }
    }

    fun hapusTransaksi(idTransaksi: Long): Int {
        return try {
            val db = writableDatabase
            db.delete(
                TABLE_TRANSAKSI,
                "$COL_ID_TRANSAKSI = ?",
                arrayOf(idTransaksi.toString())
            )
        } catch (_: Exception) {
            0
        }
    }

    fun getDaftarTransaksi(
        idPengguna: Long,
        filterTipe: TipeTransaksi? = null,
        filterIdKategori: Long? = null,
        kataKunci: String? = null,
        prefixBulan: String? = null
    ): List<Transaksi> {
        val list = mutableListOf<Transaksi>()
        val db = readableDatabase

        val conditions = mutableListOf("t.$COL_FK_PENGGUNA = ?")
        val args = mutableListOf(idPengguna.toString())

        if (filterTipe != null) {
            conditions.add("t.$COL_TIPE_TRANSAKSI = ?")
            args.add(filterTipe.name)
        }
        if (filterIdKategori != null && filterIdKategori > 0) {
            conditions.add("t.$COL_FK_KATEGORI = ?")
            args.add(filterIdKategori.toString())
        }
        if (!kataKunci.isNullOrBlank()) {
            conditions.add("(t.$COL_KETERANGAN LIKE ? ESCAPE '\\' OR k.$COL_NAMA_KATEGORI LIKE ? ESCAPE '\\')")
            val pattern = "%${escapeLikePattern(kataKunci.trim())}%"
            args.add(pattern)
            args.add(pattern)
        }
        if (!prefixBulan.isNullOrBlank()) {
            conditions.add("t.$COL_TANGGAL LIKE ? ESCAPE '\\'")
            args.add("${escapeLikePattern(prefixBulan)}%")
        }

        val whereClause = conditions.joinToString(" AND ")
        val sql = """
            SELECT 
                t.$COL_ID_TRANSAKSI,
                t.$COL_FK_PENGGUNA,
                t.$COL_FK_KATEGORI,
                k.$COL_NAMA_KATEGORI,
                k.$COL_IKON,
                k.$COL_WARNA,
                t.$COL_TIPE_TRANSAKSI,
                t.$COL_JUMLAH,
                t.$COL_TANGGAL,
                t.$COL_KETERANGAN
            FROM $TABLE_TRANSAKSI t
            INNER JOIN $TABLE_KATEGORI k ON t.$COL_FK_KATEGORI = k.$COL_ID_KATEGORI
            WHERE $whereClause
            ORDER BY t.$COL_TANGGAL DESC, t.$COL_ID_TRANSAKSI DESC
        """.trimIndent()

        val cursor = db.rawQuery(sql, args.toTypedArray())
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Transaksi(
                        idTransaksi = it.getLong(0),
                        idPengguna = it.getLong(1),
                        idKategori = it.getLong(2),
                        namaKategori = it.getString(3),
                        ikonKategori = it.getString(4),
                        warnaKategori = it.getString(5),
                        tipeTransaksi = TipeTransaksi.fromString(it.getString(6)),
                        jumlah = it.getDouble(7),
                        tanggal = it.getString(8),
                        keterangan = it.getString(9)
                    )
                )
            }
        }
        return list
    }

    // ==================== PERHITUNGAN SALDO OTOMATIS ====================

    fun hitungRingkasanSaldo(idPengguna: Long): RingkasanSaldo {
        val db = readableDatabase
        val pengguna = getPenggunaById(idPengguna)
        val saldoAwal = pengguna?.saldoAwal ?: 0.0

        var totalPemasukan = 0.0
        var totalPengeluaran = 0.0

        val sql = """
            SELECT $COL_TIPE_TRANSAKSI, SUM($COL_JUMLAH)
            FROM $TABLE_TRANSAKSI
            WHERE $COL_FK_PENGGUNA = ?
            GROUP BY $COL_TIPE_TRANSAKSI
        """.trimIndent()

        val cursor = db.rawQuery(sql, arrayOf(idPengguna.toString()))
        cursor.use {
            while (it.moveToNext()) {
                val tipe = TipeTransaksi.fromString(it.getString(0))
                val total = it.getDouble(1)
                if (tipe == TipeTransaksi.PEMASUKAN) {
                    totalPemasukan = total
                } else {
                    totalPengeluaran = total
                }
            }
        }

        return RingkasanSaldo(
            saldoAwal = saldoAwal,
            totalPemasukan = totalPemasukan,
            totalPengeluaran = totalPengeluaran
        )
    }

    // ==================== PENGELOMPOKAN TRANSAKSI PER KATEGORI ====================

    fun getPengelompokanKategori(
        idPengguna: Long,
        filterTipe: TipeTransaksi? = null,
        prefixBulan: String? = null
    ): List<RingkasanKategori> {
        val db = readableDatabase
        val conditions = mutableListOf("t.$COL_FK_PENGGUNA = ?")
        val args = mutableListOf(idPengguna.toString())

        if (filterTipe != null) {
            conditions.add("t.$COL_TIPE_TRANSAKSI = ?")
            args.add(filterTipe.name)
        }
        if (!prefixBulan.isNullOrBlank()) {
            conditions.add("t.$COL_TANGGAL LIKE ? ESCAPE '\\'")
            args.add("${escapeLikePattern(prefixBulan)}%")
        }

        val whereClause = conditions.joinToString(" AND ")
        val sql = """
            SELECT 
                k.$COL_ID_KATEGORI,
                k.$COL_NAMA_KATEGORI,
                k.$COL_IKON,
                k.$COL_WARNA,
                k.$COL_TIPE_KATEGORI,
                COUNT(t.$COL_ID_TRANSAKSI) AS jumlah_tx,
                SUM(t.$COL_JUMLAH) AS total_nominal
            FROM $TABLE_TRANSAKSI t
            INNER JOIN $TABLE_KATEGORI k ON t.$COL_FK_KATEGORI = k.$COL_ID_KATEGORI
            WHERE $whereClause
            GROUP BY k.$COL_ID_KATEGORI
            ORDER BY total_nominal DESC
        """.trimIndent()

        data class RawGroup(
            val idKategori: Long,
            val namaKategori: String,
            val ikon: String,
            val warna: String,
            val tipe: TipeTransaksi,
            val count: Int,
            val total: Double
        )

        val rawList = mutableListOf<RawGroup>()
        var totalPemasukanGroup = 0.0
        var totalPengeluaranGroup = 0.0

        val cursor = db.rawQuery(sql, args.toTypedArray())
        cursor.use {
            while (it.moveToNext()) {
                val tipe = TipeTransaksi.fromString(it.getString(4))
                val total = it.getDouble(6)
                if (tipe == TipeTransaksi.PEMASUKAN) {
                    totalPemasukanGroup += total
                } else {
                    totalPengeluaranGroup += total
                }
                rawList.add(
                    RawGroup(
                        idKategori = it.getLong(0),
                        namaKategori = it.getString(1),
                        ikon = it.getString(2),
                        warna = it.getString(3),
                        tipe = tipe,
                        count = it.getInt(5),
                        total = total
                    )
                )
            }
        }

        return rawList.map { item ->
            val pembagi = if (item.tipe == TipeTransaksi.PEMASUKAN) {
                totalPemasukanGroup
            } else {
                totalPengeluaranGroup
            }
            val persen = if (pembagi > 0) ((item.total / pembagi) * 100.0).toFloat() else 0f
            RingkasanKategori(
                idKategori = item.idKategori,
                namaKategori = item.namaKategori,
                ikonKategori = item.ikon,
                warnaKategori = item.warna,
                tipeKategori = item.tipe,
                jumlahTransaksi = item.count,
                totalNominal = item.total,
                persentase = persen
            )
        }
    }

    // ==================== LAPORAN BULANAN & GRAFIK 6 BULAN ====================

    fun getLaporanBulanan(idPengguna: Long, tahun: Int, bulan: Int): LaporanBulanan {
        val prefixBulan = FormatUtils.formatPrefixBulan(tahun, bulan)
        val daftarTransaksi = getDaftarTransaksi(
            idPengguna = idPengguna,
            prefixBulan = prefixBulan
        )

        var totalPemasukan = 0.0
        var totalPengeluaran = 0.0
        for (tx in daftarTransaksi) {
            if (tx.tipeTransaksi == TipeTransaksi.PEMASUKAN) {
                totalPemasukan += tx.jumlah
            } else {
                totalPengeluaran += tx.jumlah
            }
        }

        val pengelompokan = getPengelompokanKategori(
            idPengguna = idPengguna,
            prefixBulan = prefixBulan
        )

        return LaporanBulanan(
            tahun = tahun,
            bulan = bulan,
            totalPemasukan = totalPemasukan,
            totalPengeluaran = totalPengeluaran,
            jumlahTransaksi = daftarTransaksi.size,
            pengelompokanKategori = pengelompokan,
            daftarTransaksi = daftarTransaksi
        )
    }

    fun getTrenEnamBulan(
        idPengguna: Long,
        tahunAkhir: Int,
        bulanAkhir: Int
    ): List<DataBulananChart> {
        val hasil = mutableListOf<DataBulananChart>()
        val db = readableDatabase

        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, tahunAkhir)
            set(Calendar.MONTH, bulanAkhir - 1)
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MONTH, -5)
        }

        for (i in 0 until 6) {
            val thn = cal.get(Calendar.YEAR)
            val bln = cal.get(Calendar.MONTH) + 1
            val prefix = FormatUtils.formatPrefixBulan(thn, bln)

            var masuk = 0.0
            var keluar = 0.0

            val sql = """
                SELECT $COL_TIPE_TRANSAKSI, SUM($COL_JUMLAH)
                FROM $TABLE_TRANSAKSI
                WHERE $COL_FK_PENGGUNA = ? AND $COL_TANGGAL LIKE ? ESCAPE '\\'
                GROUP BY $COL_TIPE_TRANSAKSI
            """.trimIndent()

            val cursor = db.rawQuery(sql, arrayOf(idPengguna.toString(), "${escapeLikePattern(prefix)}%"))
            cursor.use {
                while (it.moveToNext()) {
                    val tipe = TipeTransaksi.fromString(it.getString(0))
                    val sum = it.getDouble(1)
                    if (tipe == TipeTransaksi.PEMASUKAN) masuk = sum else keluar = sum
                }
            }

            hasil.add(
                DataBulananChart(
                    tahun = thn,
                    bulan = bln,
                    labelBulanSingkat = FormatUtils.formatBulanSingkat(bln),
                    pemasukan = masuk,
                    pengeluaran = keluar
                )
            )
            cal.add(Calendar.MONTH, 1)
        }

        return hasil
    }
}
