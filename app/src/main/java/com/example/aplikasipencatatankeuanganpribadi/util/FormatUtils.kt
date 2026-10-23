package com.example.aplikasipencatatankeuanganpribadi.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object FormatUtils {
    private val localeId: Locale = Locale.forLanguageTag("id-ID")

    private val namaBulan = arrayOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    private val namaBulanSingkat = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
    )

    private val namaHariSingkat = arrayOf(
        "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"
    )

    fun formatRupiah(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(localeId).apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
        val sign = if (amount < 0) "-" else ""
        return "${sign}Rp ${formatter.format(kotlin.math.abs(amount))}"
    }

    fun formatRupiahSingkat(amount: Double): String {
        val absVal = kotlin.math.abs(amount)
        return when {
            absVal >= 1_000_000 -> String.format(Locale.US, "Rp%.1fjt", absVal / 1_000_000.0)
                .replace(".0jt", "jt")
            absVal >= 1_000 -> String.format(Locale.US, "Rp%.0frb", absVal / 1_000.0)
            else -> "Rp0"
        }
    }

    fun formatTanggalLengkap(tanggalIso: String): String {
        val parts = tanggalIso.split("-")
        if (parts.size != 3) return tanggalIso
        val tahun = parts[0].toIntOrNull() ?: return tanggalIso
        val bulan = parts[1].toIntOrNull() ?: return tanggalIso
        val hari = parts[2].toIntOrNull() ?: return tanggalIso
        if (bulan !in 1..12) return tanggalIso
        return "$hari ${namaBulan[bulan - 1]} $tahun"
    }

    fun formatBulanTahun(tahun: Int, bulan: Int): String {
        if (bulan !in 1..12) return "$bulan/$tahun"
        return "${namaBulan[bulan - 1]} $tahun"
    }

    fun formatBulanSingkat(bulan: Int): String {
        if (bulan !in 1..12) return "$bulan"
        return namaBulanSingkat[bulan - 1]
    }

    fun formatPrefixBulan(tahun: Int, bulan: Int): String {
        return String.format(Locale.US, "%04d-%02d", tahun, bulan)
    }

    fun tanggalHariIni(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Calendar.getInstance().time)
    }

    fun formatHeaderHariIni(): String {
        val cal = Calendar.getInstance()
        val hariMinggu = cal.get(Calendar.DAY_OF_WEEK) // 1..7
        val tanggal = cal.get(Calendar.DAY_OF_MONTH)
        val bulan = cal.get(Calendar.MONTH) // 0..11
        return "${namaHariSingkat[hariMinggu - 1]}, $tanggal ${namaBulanSingkat[bulan]}"
    }
}
