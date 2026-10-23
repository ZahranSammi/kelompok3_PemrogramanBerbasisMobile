package com.example.aplikasipencatatankeuanganpribadi

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipencatatankeuanganpribadi.data.DatabaseHelper
import com.example.aplikasipencatatankeuanganpribadi.model.Kategori
import com.example.aplikasipencatatankeuanganpribadi.model.Pengguna
import com.example.aplikasipencatatankeuanganpribadi.model.TipeTransaksi
import com.example.aplikasipencatatankeuanganpribadi.model.Transaksi
import com.example.aplikasipencatatankeuanganpribadi.ui.adapter.GridKategoriAdapter
import com.example.aplikasipencatatankeuanganpribadi.ui.adapter.KelompokKategoriAdapter
import com.example.aplikasipencatatankeuanganpribadi.ui.adapter.TransaksiAdapter
import com.example.aplikasipencatatankeuanganpribadi.ui.view.MonthlyBarChartView
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    // State Pengguna Aktif
    private var idPenggunaAktif: Long = 1L

    // State Filter Screen 1 (Home / Transaksi)
    private var filterTipeTransaksi: TipeTransaksi? = null
    private var filterIdKategori: Long? = null
    private var daftarKategoriFilter: List<Kategori> = emptyList()

    // State Filter Screen 2 (Select Category & Kelompok)
    private var filterTipeKelompok: TipeTransaksi? = null
    private var kataKunciKategori: String = ""

    // State Screen 3 (Analytics / Laporan Bulanan)
    private var tahunLaporan: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var bulanLaporan: Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    // Views Header & Dashboard Saldo Otomatis (Screen 1)
    private lateinit var btnKelolaPengguna: TextView
    private lateinit var btnNotifikasiHeader: TextView
    private lateinit var tvTanggalHeader: TextView
    private lateinit var tvNamaPenggunaHeader: TextView
    private lateinit var tvPengeluaranBulanIniHero: TextView
    private lateinit var tvSubHeroPemasukan: TextView
    private lateinit var cardSpendingWallet: MaterialCardView
    private lateinit var tvInfoSaldoAwal: TextView
    private lateinit var tvTotalSaldoOtomatis: TextView
    private lateinit var tvSeeAllTransaksi: TextView

    // Views Navigasi Layar & Bottom Bar
    private lateinit var layoutTabTransaksi: LinearLayout
    private lateinit var layoutTabKelompok: NestedScrollView
    private lateinit var layoutTabLaporan: NestedScrollView
    private lateinit var navHome: LinearLayout
    private lateinit var navKategori: LinearLayout
    private lateinit var navAnalytics: LinearLayout
    private lateinit var navAccount: LinearLayout
    private lateinit var tvNavLabelHome: TextView
    private lateinit var tvNavLabelKategori: TextView
    private lateinit var tvNavLabelAnalytics: TextView
    private lateinit var tvNavLabelAccount: TextView
    private lateinit var fabCatatTransaksi: MaterialCardView

    // Views Screen 1: Transaksi
    private lateinit var btnFilterSemua: MaterialButton
    private lateinit var btnFilterPemasukan: MaterialButton
    private lateinit var btnFilterPengeluaran: MaterialButton
    private lateinit var spinnerFilterKategori: Spinner
    private lateinit var tvEmptyTransaksi: TextView
    private lateinit var rvTransaksi: RecyclerView
    private lateinit var transaksiAdapter: TransaksiAdapter

    // Views Screen 2: Select Category & Pengelompokan
    private lateinit var btnBackDariKategori: TextView
    private lateinit var etCariKategori: EditText
    private lateinit var btnKelompokSemua: MaterialButton
    private lateinit var btnKelompokPengeluaran: MaterialButton
    private lateinit var btnKelompokPemasukan: MaterialButton
    private lateinit var rvGridKategori: RecyclerView
    private lateinit var tvEmptyKelompok: TextView
    private lateinit var rvKelompokKategori: RecyclerView
    private lateinit var gridKategoriAdapter: GridKategoriAdapter
    private lateinit var kelompokAdapter: KelompokKategoriAdapter

    // Views Screen 3: Analytics / Laporan Bulanan
    private lateinit var btnBulanSebelumnya: TextView
    private lateinit var tvPeriodeBulan: TextView
    private lateinit var btnBulanBerikutnya: TextView
    private lateinit var chartBulanan: MonthlyBarChartView
    private lateinit var tvLaporanPemasukan: TextView
    private lateinit var tvLaporanPengeluaran: TextView
    private lateinit var tvLaporanSelisih: TextView
    private lateinit var tvStatusArusKas: TextView
    private lateinit var tvRasioBulanan: TextView
    private lateinit var progressRasioBulanan: ProgressBar
    private lateinit var rvLaporanKategori: RecyclerView
    private lateinit var rvLaporanTransaksi: RecyclerView
    private lateinit var tvEmptyLaporan: TextView
    private lateinit var laporanKategoriAdapter: KelompokKategoriAdapter
    private lateinit var laporanTransaksiAdapter: TransaksiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        inisialisasiPenggunaAktif()
        inisialisasiView()
        terapkanWindowInsetsAmanKamera()
        setupAdapter()
        setupListeners()
        muatFilterSpinnerKategori()
        muatUlangSemuaData()
    }

    private fun inisialisasiPenggunaAktif() {
        val prefs = getSharedPreferences("keuangan_prefs", Context.MODE_PRIVATE)
        val savedId = prefs.getLong("id_pengguna_aktif", -1L)
        val semuaPengguna = dbHelper.getSemuaPengguna()
        if (semuaPengguna.isNotEmpty()) {
            val ditemukan = semuaPengguna.firstOrNull { it.idPengguna == savedId }
            idPenggunaAktif = ditemukan?.idPengguna ?: semuaPengguna.first().idPengguna
        } else {
            idPenggunaAktif = dbHelper.tambahPengguna(
                nama = "Pengguna Utama",
                email = "pengguna@keuanganpribadi.id",
                saldoAwal = 1500000.0
            )
        }
    }

    private fun simpanPenggunaAktif(idPengguna: Long) {
        idPenggunaAktif = idPengguna
        getSharedPreferences("keuangan_prefs", Context.MODE_PRIVATE)
            .edit()
            .putLong("id_pengguna_aktif", idPengguna)
            .apply()
    }

    private fun inisialisasiView() {
        btnKelolaPengguna = findViewById(R.id.btnKelolaPengguna)
        btnNotifikasiHeader = findViewById(R.id.btnNotifikasiHeader)
        tvTanggalHeader = findViewById(R.id.tvTanggalHeader)
        tvNamaPenggunaHeader = findViewById(R.id.tvNamaPenggunaHeader)
        tvPengeluaranBulanIniHero = findViewById(R.id.tvPengeluaranBulanIniHero)
        tvSubHeroPemasukan = findViewById(R.id.tvSubHeroPemasukan)
        cardSpendingWallet = findViewById(R.id.cardSpendingWallet)
        tvInfoSaldoAwal = findViewById(R.id.tvInfoSaldoAwal)
        tvTotalSaldoOtomatis = findViewById(R.id.tvTotalSaldoOtomatis)
        tvSeeAllTransaksi = findViewById(R.id.tvSeeAllTransaksi)

        layoutTabTransaksi = findViewById(R.id.layoutTabTransaksi)
        layoutTabKelompok = findViewById(R.id.layoutTabKelompok)
        layoutTabLaporan = findViewById(R.id.layoutTabLaporan)
        navHome = findViewById(R.id.navHome)
        navKategori = findViewById(R.id.navKategori)
        navAnalytics = findViewById(R.id.navAnalytics)
        navAccount = findViewById(R.id.navAccount)
        tvNavLabelHome = findViewById(R.id.tvNavLabelHome)
        tvNavLabelKategori = findViewById(R.id.tvNavLabelKategori)
        tvNavLabelAnalytics = findViewById(R.id.tvNavLabelAnalytics)
        tvNavLabelAccount = findViewById(R.id.tvNavLabelAccount)
        fabCatatTransaksi = findViewById(R.id.fabCatatTransaksi)

        btnFilterSemua = findViewById(R.id.btnFilterSemua)
        btnFilterPemasukan = findViewById(R.id.btnFilterPemasukan)
        btnFilterPengeluaran = findViewById(R.id.btnFilterPengeluaran)
        spinnerFilterKategori = findViewById(R.id.spinnerFilterKategori)
        tvEmptyTransaksi = findViewById(R.id.tvEmptyTransaksi)
        rvTransaksi = findViewById(R.id.rvTransaksi)

        btnBackDariKategori = findViewById(R.id.btnBackDariKategori)
        etCariKategori = findViewById(R.id.etCariKategori)
        btnKelompokSemua = findViewById(R.id.btnKelompokSemua)
        btnKelompokPengeluaran = findViewById(R.id.btnKelompokPengeluaran)
        btnKelompokPemasukan = findViewById(R.id.btnKelompokPemasukan)
        rvGridKategori = findViewById(R.id.rvGridKategori)
        tvEmptyKelompok = findViewById(R.id.tvEmptyKelompok)
        rvKelompokKategori = findViewById(R.id.rvKelompokKategori)

        btnBulanSebelumnya = findViewById(R.id.btnBulanSebelumnya)
        tvPeriodeBulan = findViewById(R.id.tvPeriodeBulan)
        btnBulanBerikutnya = findViewById(R.id.btnBulanBerikutnya)
        chartBulanan = findViewById(R.id.chartBulanan)
        tvLaporanPemasukan = findViewById(R.id.tvLaporanPemasukan)
        tvLaporanPengeluaran = findViewById(R.id.tvLaporanPengeluaran)
        tvLaporanSelisih = findViewById(R.id.tvLaporanSelisih)
        tvStatusArusKas = findViewById(R.id.tvStatusArusKas)
        tvRasioBulanan = findViewById(R.id.tvRasioBulanan)
        progressRasioBulanan = findViewById(R.id.progressRasioBulanan)
        rvLaporanKategori = findViewById(R.id.rvLaporanKategori)
        rvLaporanTransaksi = findViewById(R.id.rvLaporanTransaksi)
        tvEmptyLaporan = findViewById(R.id.tvEmptyLaporan)
    }

    private fun terapkanWindowInsetsAmanKamera() {
        val rootContainer = findViewById<View>(R.id.rootContainer)
        val cardBottomNav = findViewById<View>(R.id.cardBottomNav)
        val density = resources.displayMetrics.density
        val minTopPx = (36 * density).toInt()
        val extraBottomListPx = (96 * density).toInt()

        ViewCompat.setOnApplyWindowInsetsListener(rootContainer) { _, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val safeTop = kotlin.math.max(insets.top, minTopPx) + (6 * density).toInt()
            val safeBottom = insets.bottom

            layoutTabTransaksi.setPadding(
                insets.left,
                safeTop,
                insets.right,
                0
            )
            layoutTabKelompok.setPadding(
                insets.left,
                safeTop,
                insets.right,
                extraBottomListPx + safeBottom
            )
            layoutTabLaporan.setPadding(
                insets.left,
                safeTop,
                insets.right,
                extraBottomListPx + safeBottom
            )
            rvTransaksi.setPadding(
                0,
                (4 * density).toInt(),
                0,
                extraBottomListPx + safeBottom
            )
            cardBottomNav.setPadding(0, 0, 0, safeBottom)

            windowInsets
        }
    }

    private fun setupAdapter() {
        transaksiAdapter = TransaksiAdapter(
            daftarTransaksi = emptyList(),
            onKlikItem = { tx -> tampilkanDialogFormTransaksi(tx) },
            onKlikHapus = { tx -> konfirmasiHapusTransaksi(tx) }
        )
        rvTransaksi.layoutManager = LinearLayoutManager(this)
        rvTransaksi.adapter = transaksiAdapter

        gridKategoriAdapter = GridKategoriAdapter(
            daftarKategori = emptyList(),
            nominalPerKategori = emptyMap(),
            onKlikTambah = {
                tampilkanDialogTambahKategori(
                    defaultTipe = filterTipeKelompok ?: TipeTransaksi.PENGELUARAN
                )
            },
            onKlikKategori = { kat ->
                filterTipeTransaksi = kat.tipeKategori
                muatFilterSpinnerKategori(selectedCategoryId = kat.idKategori)
                pindahKeTab(0)
                Toast.makeText(
                    this,
                    "Filter transaksi: ${kat.namaKategori}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        rvGridKategori.layoutManager = GridLayoutManager(this, 4)
        rvGridKategori.adapter = gridKategoriAdapter

        kelompokAdapter = KelompokKategoriAdapter(
            daftarKelompok = emptyList(),
            onKlikKelompok = { ringkasan ->
                filterTipeTransaksi = ringkasan.tipeKategori
                muatFilterSpinnerKategori(selectedCategoryId = ringkasan.idKategori)
                pindahKeTab(0)
                Toast.makeText(
                    this,
                    "Menampilkan transaksi kategori ${ringkasan.namaKategori}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        rvKelompokKategori.layoutManager = LinearLayoutManager(this)
        rvKelompokKategori.adapter = kelompokAdapter

        laporanKategoriAdapter = KelompokKategoriAdapter(daftarKelompok = emptyList())
        rvLaporanKategori.layoutManager = LinearLayoutManager(this)
        rvLaporanKategori.adapter = laporanKategoriAdapter

        laporanTransaksiAdapter = TransaksiAdapter(
            daftarTransaksi = emptyList(),
            onKlikItem = { tx -> tampilkanDialogFormTransaksi(tx) },
            onKlikHapus = { tx -> konfirmasiHapusTransaksi(tx) }
        )
        rvLaporanTransaksi.layoutManager = LinearLayoutManager(this)
        rvLaporanTransaksi.adapter = laporanTransaksiAdapter
    }

    private fun pindahKeTab(index: Int) {
        val warnaAktif = ContextCompat.getColor(this, R.color.primary_finance)
        val warnaNonAktif = ContextCompat.getColor(this, R.color.text_muted)

        layoutTabTransaksi.visibility = if (index == 0) View.VISIBLE else View.GONE
        layoutTabKelompok.visibility = if (index == 1) View.VISIBLE else View.GONE
        layoutTabLaporan.visibility = if (index == 2) View.VISIBLE else View.GONE

        tvNavLabelHome.setTextColor(if (index == 0) warnaAktif else warnaNonAktif)
        tvNavLabelKategori.setTextColor(if (index == 1) warnaAktif else warnaNonAktif)
        tvNavLabelAnalytics.setTextColor(if (index == 2) warnaAktif else warnaNonAktif)
        tvNavLabelAccount.setTextColor(warnaNonAktif)

        tvNavLabelHome.paint.isFakeBoldText = (index == 0)
        tvNavLabelKategori.paint.isFakeBoldText = (index == 1)
        tvNavLabelAnalytics.paint.isFakeBoldText = (index == 2)
    }

    private fun setupListeners() {
        // Bottom Navigation
        navHome.setOnClickListener { pindahKeTab(0) }
        navKategori.setOnClickListener { pindahKeTab(1) }
        navAnalytics.setOnClickListener { pindahKeTab(2) }
        navAccount.setOnClickListener { tampilkanDialogKelolaPengguna() }

        btnBackDariKategori.setOnClickListener { pindahKeTab(0) }

        // Pencarian Kategori di Screen 2
        etCariKategori.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                kataKunciKategori = s?.toString() ?: ""
                muatPengelompokanKategori()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Filter Tipe di Screen 1 (Home / Transaksi)
        btnFilterSemua.setOnClickListener {
            filterTipeTransaksi = null
            muatFilterSpinnerKategori()
            muatDaftarTransaksi()
        }
        btnFilterPemasukan.setOnClickListener {
            filterTipeTransaksi = TipeTransaksi.PEMASUKAN
            muatFilterSpinnerKategori()
            muatDaftarTransaksi()
        }
        btnFilterPengeluaran.setOnClickListener {
            filterTipeTransaksi = TipeTransaksi.PENGELUARAN
            muatFilterSpinnerKategori()
            muatDaftarTransaksi()
        }

        tvSeeAllTransaksi.setOnClickListener {
            filterTipeTransaksi = null
            filterIdKategori = null
            muatFilterSpinnerKategori()
            muatDaftarTransaksi()
        }

        // Filter Tipe di Screen 2 (Select Category & Kelompok)
        btnKelompokSemua.setOnClickListener {
            filterTipeKelompok = null
            muatPengelompokanKategori()
        }
        btnKelompokPengeluaran.setOnClickListener {
            filterTipeKelompok = TipeTransaksi.PENGELUARAN
            muatPengelompokanKategori()
        }
        btnKelompokPemasukan.setOnClickListener {
            filterTipeKelompok = TipeTransaksi.PEMASUKAN
            muatPengelompokanKategori()
        }

        // Navigasi Bulan di Screen 3 (Analytics / Laporan Bulanan)
        btnBulanSebelumnya.setOnClickListener {
            bulanLaporan -= 1
            if (bulanLaporan < 1) {
                bulanLaporan = 12
                tahunLaporan -= 1
            }
            muatLaporanBulanan()
        }

        btnBulanBerikutnya.setOnClickListener {
            bulanLaporan += 1
            if (bulanLaporan > 12) {
                bulanLaporan = 1
                tahunLaporan += 1
            }
            muatLaporanBulanan()
        }

        // Kelola Pengguna & Dompet
        btnKelolaPengguna.setOnClickListener { tampilkanDialogKelolaPengguna() }
        cardSpendingWallet.setOnClickListener { tampilkanDialogKelolaPengguna() }
        btnNotifikasiHeader.setOnClickListener {
            val saldo = dbHelper.hitungRingkasanSaldo(idPenggunaAktif)
            AlertDialog.Builder(this)
                .setTitle("Ringkasan Saldo Otomatis")
                .setMessage(
                    "Saldo Awal: ${FormatUtils.formatRupiah(saldo.saldoAwal)}\n" +
                        "Total Pemasukan: +${FormatUtils.formatRupiah(saldo.totalPemasukan)}\n" +
                        "Total Pengeluaran: -${FormatUtils.formatRupiah(saldo.totalPengeluaran)}\n\n" +
                        "Saldo Akhir Saat Ini:\n${FormatUtils.formatRupiah(saldo.saldoAkhir)}"
                )
                .setPositiveButton("OK", null)
                .show()
        }

        // Tombol Bulat '+' di Tengah Bawah
        fabCatatTransaksi.setOnClickListener {
            tampilkanDialogFormTransaksi(null)
        }
    }

    private fun muatFilterSpinnerKategori(selectedCategoryId: Long? = null) {
        daftarKategoriFilter = dbHelper.getSemuaKategori(filterTipeTransaksi)
        val labelItems = mutableListOf("Semua Kategori")
        labelItems.addAll(daftarKategoriFilter.map { "${it.ikon} ${it.namaKategori}" })

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            labelItems
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerFilterKategori.adapter = spinnerAdapter

        if (selectedCategoryId != null) {
            val idx = daftarKategoriFilter.indexOfFirst { it.idKategori == selectedCategoryId }
            if (idx >= 0) {
                filterIdKategori = selectedCategoryId
                spinnerFilterKategori.setSelection(idx + 1)
            } else {
                filterIdKategori = null
            }
        } else {
            filterIdKategori = null
        }

        spinnerFilterKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterIdKategori = if (position == 0) {
                    null
                } else {
                    daftarKategoriFilter.getOrNull(position - 1)?.idKategori
                }
                muatDaftarTransaksi()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun muatUlangSemuaData() {
        muatHeaderDanSaldoOtomatis()
        muatDaftarTransaksi()
        muatPengelompokanKategori()
        muatLaporanBulanan()
    }

    private fun muatHeaderDanSaldoOtomatis() {
        tvTanggalHeader.text = "📅 ${FormatUtils.formatHeaderHariIni()}"
        val pengguna = dbHelper.getPenggunaById(idPenggunaAktif)
        if (pengguna != null) {
            tvNamaPenggunaHeader.text = "${pengguna.namaPengguna} • ${pengguna.email}"
        }

        // Menghitung Saldo Otomatis
        val ringkasanSaldo = dbHelper.hitungRingkasanSaldo(idPenggunaAktif)
        tvInfoSaldoAwal.text = "Saldo Awal: ${FormatUtils.formatRupiah(ringkasanSaldo.saldoAwal)}"
        tvTotalSaldoOtomatis.text = FormatUtils.formatRupiah(ringkasanSaldo.saldoAkhir)

        // Hero Section: Pengeluaran Bulan Berjalan (This Month Spend)
        val calNow = Calendar.getInstance()
        val laporanBulanIni = dbHelper.getLaporanBulanan(
            idPenggunaAktif,
            calNow.get(Calendar.YEAR),
            calNow.get(Calendar.MONTH) + 1
        )
        tvPengeluaranBulanIniHero.text = FormatUtils.formatRupiah(laporanBulanIni.totalPengeluaran)
        tvSubHeroPemasukan.text =
            "↘ Pemasukan bulan ini: +${FormatUtils.formatRupiah(laporanBulanIni.totalPemasukan)}"
    }

    private fun muatDaftarTransaksi() {
        perbaruiTampilanTombolFilterTransaksi()
        val list = dbHelper.getDaftarTransaksi(
            idPengguna = idPenggunaAktif,
            filterTipe = filterTipeTransaksi,
            filterIdKategori = filterIdKategori
        )
        transaksiAdapter.updateData(list)
        tvEmptyTransaksi.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun perbaruiTampilanTombolFilterTransaksi() {
        val aktifColor = ContextCompat.getColor(this, R.color.primary_finance)
        val nonAktifColor = ContextCompat.getColor(this, R.color.text_secondary)

        btnFilterSemua.setTextColor(if (filterTipeTransaksi == null) aktifColor else nonAktifColor)
        btnFilterPemasukan.setTextColor(
            if (filterTipeTransaksi == TipeTransaksi.PEMASUKAN) aktifColor else nonAktifColor
        )
        btnFilterPengeluaran.setTextColor(
            if (filterTipeTransaksi == TipeTransaksi.PENGELUARAN) aktifColor else nonAktifColor
        )
    }

    private fun muatPengelompokanKategori() {
        val aktifColor = ContextCompat.getColor(this, R.color.primary_finance)
        val nonAktifColor = ContextCompat.getColor(this, R.color.text_secondary)

        btnKelompokSemua.setTextColor(if (filterTipeKelompok == null) aktifColor else nonAktifColor)
        btnKelompokPengeluaran.setTextColor(
            if (filterTipeKelompok == TipeTransaksi.PENGELUARAN) aktifColor else nonAktifColor
        )
        btnKelompokPemasukan.setTextColor(
            if (filterTipeKelompok == TipeTransaksi.PEMASUKAN) aktifColor else nonAktifColor
        )

        val listKelompok = dbHelper.getPengelompokanKategori(
            idPengguna = idPenggunaAktif,
            filterTipe = filterTipeKelompok
        )

        // Update Grid 4 Kolom Kategori (Select Category)
        val semuaKategoriCocok = dbHelper.getSemuaKategori(
            filterTipe = filterTipeKelompok,
            kataKunci = kataKunciKategori
        )
        val mapNominal = listKelompok.associate { it.idKategori to it.totalNominal }
        gridKategoriAdapter.updateData(semuaKategoriCocok, mapNominal)

        // Update daftar pengelompokan transaksi
        val listKelompokTersaring = if (kataKunciKategori.isBlank()) {
            listKelompok
        } else {
            listKelompok.filter {
                it.namaKategori.contains(kataKunciKategori.trim(), ignoreCase = true)
            }
        }
        kelompokAdapter.updateData(listKelompokTersaring)
        tvEmptyKelompok.visibility = if (listKelompokTersaring.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun muatLaporanBulanan() {
        tvPeriodeBulan.text = FormatUtils.formatBulanTahun(tahunLaporan, bulanLaporan)
        val laporan = dbHelper.getLaporanBulanan(idPenggunaAktif, tahunLaporan, bulanLaporan)

        // Update Grafik 6 Bulan (Analytics Bar Chart)
        val dataEnamBulan = dbHelper.getTrenEnamBulan(idPenggunaAktif, tahunLaporan, bulanLaporan)
        chartBulanan.setData(dataEnamBulan)

        tvLaporanPemasukan.text = FormatUtils.formatRupiah(laporan.totalPemasukan)
        tvLaporanPengeluaran.text = FormatUtils.formatRupiah(laporan.totalPengeluaran)

        val tanda = if (laporan.selisihBulanan >= 0) "+" else ""
        tvLaporanSelisih.text = "$tanda${FormatUtils.formatRupiah(laporan.selisihBulanan)}"

        val statusKet = when {
            laporan.selisihBulanan > 0 -> "Selisih Bulanan • Surplus (${laporan.jumlahTransaksi} transaksi)"
            laporan.selisihBulanan < 0 -> "Selisih Bulanan • Defisit (${laporan.jumlahTransaksi} transaksi)"
            else -> "Selisih Bulanan • Seimbang (${laporan.jumlahTransaksi} transaksi)"
        }
        tvStatusArusKas.text = statusKet

        val warnaSelisih = if (laporan.selisihBulanan >= 0) {
            ContextCompat.getColor(this, R.color.primary_finance)
        } else {
            ContextCompat.getColor(this, R.color.expense_red)
        }
        tvLaporanSelisih.setTextColor(warnaSelisih)

        val rasio = laporan.rasioPengeluaranPersen
        tvRasioBulanan.text = "Rasio Keluar: $rasio%"
        progressRasioBulanan.progress = rasio
        val warnaProgress = if (rasio <= 75) {
            ContextCompat.getColor(this, R.color.primary_finance)
        } else {
            ContextCompat.getColor(this, R.color.expense_red)
        }
        progressRasioBulanan.progressTintList = ColorStateList.valueOf(warnaProgress)

        laporanKategoriAdapter.updateData(laporan.pengelompokanKategori)
        laporanTransaksiAdapter.updateData(laporan.daftarTransaksi)
        tvEmptyLaporan.visibility =
            if (laporan.daftarTransaksi.isEmpty()) View.VISIBLE else View.GONE
    }

    // ==================== DIALOG CATAT / EDIT TRANSAKSI ====================

    private fun tampilkanDialogFormTransaksi(transaksiEdit: Transaksi?) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_form_transaksi, null)

        val rgTipe = dialogView.findViewById<RadioGroup>(R.id.rgTipeTransaksi)
        val rbPengeluaran = dialogView.findViewById<RadioButton>(R.id.rbPengeluaran)
        val rbPemasukan = dialogView.findViewById<RadioButton>(R.id.rbPemasukan)
        val btnTambahKategoriDialog = dialogView.findViewById<TextView>(R.id.btnTambahKategoriDialog)
        val spinnerKategori = dialogView.findViewById<Spinner>(R.id.spinnerKategoriTransaksi)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlahTransaksi)
        val etTanggal = dialogView.findViewById<EditText>(R.id.etTanggalTransaksi)
        val etKeterangan = dialogView.findViewById<EditText>(R.id.etKeteranganTransaksi)

        var daftarKategoriSaatIni: List<Kategori> = emptyList()

        fun muatKategoriDialog(tipe: TipeTransaksi, pilihIdKategori: Long? = null) {
            daftarKategoriSaatIni = dbHelper.getSemuaKategori(tipe)
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                daftarKategoriSaatIni
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerKategori.adapter = adapter
            if (pilihIdKategori != null) {
                val index = daftarKategoriSaatIni.indexOfFirst { it.idKategori == pilihIdKategori }
                if (index >= 0) spinnerKategori.setSelection(index)
            }
        }

        if (transaksiEdit != null) {
            if (transaksiEdit.tipeTransaksi == TipeTransaksi.PEMASUKAN) {
                rbPemasukan.isChecked = true
            } else {
                rbPengeluaran.isChecked = true
            }
            muatKategoriDialog(transaksiEdit.tipeTransaksi, transaksiEdit.idKategori)
            etJumlah.setText(transaksiEdit.jumlah.toLong().toString())
            etTanggal.setText(transaksiEdit.tanggal)
            etKeterangan.setText(transaksiEdit.keterangan)
        } else {
            rbPengeluaran.isChecked = true
            muatKategoriDialog(TipeTransaksi.PENGELUARAN)
            etTanggal.setText(FormatUtils.tanggalHariIni())
        }

        rgTipe.setOnCheckedChangeListener { _, checkedId ->
            val tipeBaru = if (checkedId == R.id.rbPemasukan) {
                TipeTransaksi.PEMASUKAN
            } else {
                TipeTransaksi.PENGELUARAN
            }
            muatKategoriDialog(tipeBaru)
        }

        btnTambahKategoriDialog.setOnClickListener {
            val tipeTerpilih = if (rbPemasukan.isChecked) {
                TipeTransaksi.PEMASUKAN
            } else {
                TipeTransaksi.PENGELUARAN
            }
            tampilkanDialogTambahKategori(defaultTipe = tipeTerpilih) { idKategoriBaru ->
                muatKategoriDialog(tipeTerpilih, idKategoriBaru)
                muatFilterSpinnerKategori()
            }
        }

        etTanggal.setOnClickListener {
            val cal = Calendar.getInstance()
            val parts = etTanggal.text.toString().split("-")
            val y = parts.getOrNull(0)?.toIntOrNull() ?: cal.get(Calendar.YEAR)
            val m = (parts.getOrNull(1)?.toIntOrNull() ?: (cal.get(Calendar.MONTH) + 1)) - 1
            val d = parts.getOrNull(2)?.toIntOrNull() ?: cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val formatted = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                etTanggal.setText(formatted)
            }, y, m, d).show()
        }

        val judulDialog = if (transaksiEdit == null) "Catat Transaksi Baru" else "Ubah Transaksi"
        val dialog = AlertDialog.Builder(this)
            .setTitle(judulDialog)
            .setView(dialogView)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val tipe = if (rbPemasukan.isChecked) {
                    TipeTransaksi.PEMASUKAN
                } else {
                    TipeTransaksi.PENGELUARAN
                }
                val kategoriTerpilih =
                    daftarKategoriSaatIni.getOrNull(spinnerKategori.selectedItemPosition)
                if (kategoriTerpilih == null) {
                    Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val jumlahText = etJumlah.text.toString().trim()
                val jumlah = jumlahText.toDoubleOrNull()
                if (jumlah == null || jumlah <= 0) {
                    etJumlah.error = "Masukkan nominal lebih dari 0"
                    return@setOnClickListener
                }

                val tanggal = etTanggal.text.toString().trim().ifBlank { FormatUtils.tanggalHariIni() }
                val keterangan = etKeterangan.text.toString().trim()
                    .ifBlank { kategoriTerpilih.namaKategori }

                if (transaksiEdit == null) {
                    dbHelper.tambahTransaksi(
                        idPengguna = idPenggunaAktif,
                        idKategori = kategoriTerpilih.idKategori,
                        tipeTransaksi = tipe,
                        jumlah = jumlah,
                        tanggal = tanggal,
                        keterangan = keterangan
                    )
                    Toast.makeText(this, "Transaksi berhasil dicatat", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.updateTransaksi(
                        idTransaksi = transaksiEdit.idTransaksi,
                        idKategori = kategoriTerpilih.idKategori,
                        tipeTransaksi = tipe,
                        jumlah = jumlah,
                        tanggal = tanggal,
                        keterangan = keterangan
                    )
                    Toast.makeText(this, "Transaksi berhasil diperbarui", Toast.LENGTH_SHORT).show()
                }

                muatUlangSemuaData()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun konfirmasiHapusTransaksi(transaksi: Transaksi) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Transaksi")
            .setMessage(
                "Apakah Anda yakin ingin menghapus transaksi '${transaksi.keterangan}' (${
                    FormatUtils.formatRupiah(transaksi.jumlah)
                })?"
            )
            .setPositiveButton("Hapus") { _, _ ->
                dbHelper.hapusTransaksi(transaksi.idTransaksi)
                muatUlangSemuaData()
                Toast.makeText(this, "Transaksi dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // ==================== DIALOG TAMBAH KATEGORI ====================

    private fun tampilkanDialogTambahKategori(
        defaultTipe: TipeTransaksi = TipeTransaksi.PENGELUARAN,
        onSelesai: ((Long) -> Unit)? = null
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_form_kategori, null)
        val rbPengeluaran = view.findViewById<RadioButton>(R.id.rbKategoriPengeluaran)
        val rbPemasukan = view.findViewById<RadioButton>(R.id.rbKategoriPemasukan)
        val etNama = view.findViewById<EditText>(R.id.etNamaKategoriBaru)
        val etIkon = view.findViewById<EditText>(R.id.etIkonKategoriBaru)

        if (defaultTipe == TipeTransaksi.PEMASUKAN) {
            rbPemasukan.isChecked = true
        } else {
            rbPengeluaran.isChecked = true
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Kategori Baru")
            .setView(view)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val nama = etNama.text.toString().trim()
                if (nama.isBlank()) {
                    etNama.error = "Nama kategori wajib diisi"
                    return@setOnClickListener
                }
                val tipe = if (rbPemasukan.isChecked) {
                    TipeTransaksi.PEMASUKAN
                } else {
                    TipeTransaksi.PENGELUARAN
                }
                val ikon = etIkon.text.toString().trim()
                    .ifBlank { if (tipe == TipeTransaksi.PEMASUKAN) "💰" else "🛍️" }
                val warnaHex = if (tipe == TipeTransaksi.PEMASUKAN) "#8B5CF6" else "#10B981"

                val idBaru = dbHelper.tambahKategori(nama, tipe, ikon, warnaHex)
                muatFilterSpinnerKategori()
                muatUlangSemuaData()
                onSelesai?.invoke(idBaru)
                Toast.makeText(this, "Kategori '$nama' ditambahkan", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    // ==================== DIALOG KELOLA PENGGUNA ====================

    private fun tampilkanDialogKelolaPengguna() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_kelola_pengguna, null)
        val spinnerPilihPengguna = view.findViewById<Spinner>(R.id.spinnerPilihPengguna)
        val etNamaPengguna = view.findViewById<EditText>(R.id.etNamaPengguna)
        val etEmailPengguna = view.findViewById<EditText>(R.id.etEmailPengguna)
        val etSaldoAwalPengguna = view.findViewById<EditText>(R.id.etSaldoAwalPengguna)

        val daftarPengguna = dbHelper.getSemuaPengguna().toMutableList()
        val labelPengguna = daftarPengguna.map { "${it.namaPengguna} (${it.email})" }

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            labelPengguna
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerPilihPengguna.adapter = spinnerAdapter

        var penggunaTerpilih: Pengguna =
            daftarPengguna.firstOrNull { it.idPengguna == idPenggunaAktif }
                ?: daftarPengguna.first()

        val indexAktif = daftarPengguna.indexOfFirst { it.idPengguna == penggunaTerpilih.idPengguna }
        if (indexAktif >= 0) {
            spinnerPilihPengguna.setSelection(indexAktif)
        }

        fun isiFormPengguna(p: Pengguna) {
            etNamaPengguna.setText(p.namaPengguna)
            etEmailPengguna.setText(p.email)
            etSaldoAwalPengguna.setText(p.saldoAwal.toLong().toString())
        }

        isiFormPengguna(penggunaTerpilih)

        spinnerPilihPengguna.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                daftarPengguna.getOrNull(position)?.let {
                    penggunaTerpilih = it
                    isiFormPengguna(it)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Account & Spending Wallet")
            .setView(view)
            .setPositiveButton("Simpan & Aktifkan", null)
            .setNeutralButton("+ Pengguna Baru", null)
            .setNegativeButton("Tutup", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val nama = etNamaPengguna.text.toString().trim()
                if (nama.isBlank()) {
                    etNamaPengguna.error = "Nama pengguna wajib diisi"
                    return@setOnClickListener
                }
                val email = etEmailPengguna.text.toString().trim()
                    .ifBlank { "user@keuanganpribadi.id" }
                val saldoAwal = etSaldoAwalPengguna.text.toString().trim().toDoubleOrNull() ?: 0.0

                dbHelper.updatePengguna(
                    Pengguna(
                        idPengguna = penggunaTerpilih.idPengguna,
                        namaPengguna = nama,
                        email = email,
                        saldoAwal = saldoAwal
                    )
                )
                simpanPenggunaAktif(penggunaTerpilih.idPengguna)
                muatUlangSemuaData()
                Toast.makeText(this, "Pengguna '$nama' diaktifkan", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                val nama = etNamaPengguna.text.toString().trim()
                if (nama.isBlank()) {
                    etNamaPengguna.error = "Isi nama pengguna baru"
                    return@setOnClickListener
                }
                val email = etEmailPengguna.text.toString().trim()
                    .ifBlank { "pengguna@keuanganpribadi.id" }
                val saldoAwal = etSaldoAwalPengguna.text.toString().trim().toDoubleOrNull() ?: 0.0

                val idBaru = dbHelper.tambahPengguna(nama, email, saldoAwal)
                simpanPenggunaAktif(idBaru)
                muatUlangSemuaData()
                Toast.makeText(
                    this,
                    "Pengguna baru '$nama' berhasil dibuat & diaktifkan",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}
