package com.example.aplikasipencatatankeuanganpribadi.model

enum class TipeTransaksi(val label: String) {
    PEMASUKAN("Pemasukan"),
    PENGELUARAN("Pengeluaran");

    companion object {
        fun fromString(value: String): TipeTransaksi {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PENGELUARAN
        }
    }
}

data class Kategori(
    val idKategori: Long = 0,
    val namaKategori: String,
    val tipeKategori: TipeTransaksi,
    val ikon: String = "💰",
    val warnaHex: String = "#4CAF50"
) {
    override fun toString(): String = "$ikon $namaKategori"
}
