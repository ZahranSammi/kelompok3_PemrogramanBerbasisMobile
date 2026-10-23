package com.example.aplikasipencatatankeuanganpribadi.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipencatatankeuanganpribadi.R
import com.example.aplikasipencatatankeuanganpribadi.model.RingkasanKategori
import com.example.aplikasipencatatankeuanganpribadi.model.TipeTransaksi
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils
import java.util.Locale

class KelompokKategoriAdapter(
    private var daftarKelompok: List<RingkasanKategori>,
    private val onKlikKelompok: ((RingkasanKategori) -> Unit)? = null
) : RecyclerView.Adapter<KelompokKategoriAdapter.KelompokViewHolder>() {

    class KelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIkonKelompok: TextView = itemView.findViewById(R.id.tvIkonKelompok)
        val tvNamaKelompok: TextView = itemView.findViewById(R.id.tvNamaKelompok)
        val tvInfoKelompok: TextView = itemView.findViewById(R.id.tvInfoKelompok)
        val tvTotalNominalKelompok: TextView = itemView.findViewById(R.id.tvTotalNominalKelompok)
        val tvPersentaseKelompok: TextView = itemView.findViewById(R.id.tvPersentaseKelompok)
        val progressKategori: ProgressBar = itemView.findViewById(R.id.progressKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KelompokViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kelompok_kategori, parent, false)
        return KelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: KelompokViewHolder, position: Int) {
        val item = daftarKelompok[position]
        val context = holder.itemView.context

        holder.tvIkonKelompok.text = item.ikonKategori
        holder.tvNamaKelompok.text = item.namaKategori
        holder.tvInfoKelompok.text = "${item.tipeKategori.label} • ${item.jumlahTransaksi} transaksi"
        holder.tvTotalNominalKelompok.text = FormatUtils.formatRupiah(item.totalNominal)
        holder.tvPersentaseKelompok.text = String.format(Locale.US, "%.1f%%", item.persentase)

        val warnaInt = try {
            Color.parseColor(item.warnaKategori)
        } catch (_: Exception) {
            if (item.tipeKategori == TipeTransaksi.PEMASUKAN) {
                ContextCompat.getColor(context, R.color.income_green)
            } else {
                ContextCompat.getColor(context, R.color.expense_red)
            }
        }

        holder.tvTotalNominalKelompok.setTextColor(
            if (item.tipeKategori == TipeTransaksi.PEMASUKAN) {
                ContextCompat.getColor(context, R.color.income_green)
            } else {
                ContextCompat.getColor(context, R.color.expense_red)
            }
        )

        holder.progressKategori.progress = item.persentase.toInt().coerceIn(0, 100)
        holder.progressKategori.progressTintList = ColorStateList.valueOf(warnaInt)

        holder.itemView.setOnClickListener {
            onKlikKelompok?.invoke(item)
        }
    }

    override fun getItemCount(): Int = daftarKelompok.size

    fun updateData(newList: List<RingkasanKategori>) {
        daftarKelompok = newList
        notifyDataSetChanged()
    }
}
