package com.example.aplikasipencatatankeuanganpribadi

import com.example.aplikasipencatatankeuanganpribadi.model.LaporanBulanan
import com.example.aplikasipencatatankeuanganpribadi.model.RingkasanSaldo
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun hitungSaldoOtomatis_isCorrect() {
        val saldo = RingkasanSaldo(
            saldoAwal = 1000000.0,
            totalPemasukan = 7750000.0,
            totalPengeluaran = 1720000.0
        )
        assertEquals(7030000.0, saldo.saldoAkhir, 0.01)
        assertEquals(6030000.0, saldo.selisihTransaksi, 0.01)
    }

    @Test
    fun laporanBulanan_isCorrect() {
        val laporan = LaporanBulanan(
            tahun = 2026,
            bulan = 10,
            totalPemasukan = 5000000.0,
            totalPengeluaran = 2000000.0,
            jumlahTransaksi = 4,
            pengelompokanKategori = emptyList(),
            daftarTransaksi = emptyList()
        )
        assertEquals(3000000.0, laporan.selisihBulanan, 0.01)
        assertEquals(40, laporan.rasioPengeluaranPersen)
        assertEquals("Oktober 2026", FormatUtils.formatBulanTahun(2026, 10))
        assertEquals("2026-10", FormatUtils.formatPrefixBulan(2026, 10))
    }
}
