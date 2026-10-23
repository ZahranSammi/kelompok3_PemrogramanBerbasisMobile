package com.example.aplikasipencatatankeuanganpribadi.model

data class Transaksi(
    val idTransaksi: Long = 0,
    val idPengguna: Long,
    val idKategori: Long,
    val namaKategori: String = "",
    val ikonKategori: String = "💰",
    val warnaKategori: String = "#4CAF50",
    val tipeTransaksi: TipeTransaksi,
    val jumlah: Double,
    val tanggal: String, // YYYY-MM-DD
    val keterangan: String
)
