package com.example.aplikasipencatatankeuanganpribadi.model

data class Pengguna(
    val idPengguna: Long = 0,
    val namaPengguna: String,
    val email: String,
    val saldoAwal: Double = 0.0
)
