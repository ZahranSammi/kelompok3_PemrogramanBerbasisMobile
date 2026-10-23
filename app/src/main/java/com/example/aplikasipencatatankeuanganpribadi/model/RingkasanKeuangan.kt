package com.example.aplikasipencatatankeuanganpribadi.model

data class RingkasanKategori(
    val idKategori: Long,
    val namaKategori: String,
    val ikonKategori: String,
    val warnaKategori: String,
    val tipeKategori: TipeTransaksi,
    val jumlahTransaksi: Int,
    val totalNominal: Double,
    val persentase: Float
)

data class RingkasanSaldo(
    val saldoAwal: Double,
    val totalPemasukan: Double,
    val totalPengeluaran: Double
) {
    /**
     * Menghitung saldo otomatis berdasarkan saldo awal pengguna + total pemasukan - total pengeluaran.
     */
    val saldoAkhir: Double
        get() = saldoAwal + totalPemasukan - totalPengeluaran

    val selisihTransaksi: Double
        get() = totalPemasukan - totalPengeluaran
}

data class DataBulananChart(
    val tahun: Int,
    val bulan: Int,
    val labelBulanSingkat: String,
    val pemasukan: Double,
    val pengeluaran: Double
)

data class LaporanBulanan(
    val tahun: Int,
    val bulan: Int,
    val totalPemasukan: Double,
    val totalPengeluaran: Double,
    val jumlahTransaksi: Int,
    val pengelompokanKategori: List<RingkasanKategori>,
    val daftarTransaksi: List<Transaksi>
) {
    val selisihBulanan: Double
        get() = totalPemasukan - totalPengeluaran

    val rasioPengeluaranPersen: Int
        get() = if (totalPemasukan > 0) {
            ((totalPengeluaran / totalPemasukan) * 100.0).toInt().coerceIn(0, 100)
        } else if (totalPengeluaran > 0) {
            100
        } else {
            0
        }
}
