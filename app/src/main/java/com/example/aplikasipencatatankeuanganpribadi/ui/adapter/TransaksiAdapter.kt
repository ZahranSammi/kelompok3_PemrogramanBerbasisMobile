package com.example.aplikasipencatatankeuanganpribadi.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipencatatankeuanganpribadi.R
import com.example.aplikasipencatatankeuanganpribadi.model.TipeTransaksi
import com.example.aplikasipencatatankeuanganpribadi.model.Transaksi
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils
import com.google.android.material.card.MaterialCardView

class TransaksiAdapter(
    private var daftarTransaksi: List<Transaksi>,
    private val onKlikItem: (Transaksi) -> Unit,
    private val onKlikHapus: (Transaksi) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    class TransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardBadgeIkon: MaterialCardView = itemView.findViewById(R.id.cardBadgeIkon)
        val tvIkonKategori: TextView = itemView.findViewById(R.id.tvIkonKategori)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        val tvKategoriTanggal: TextView = itemView.findViewById(R.id.tvKategoriTanggal)
        val tvNominal: TextView = itemView.findViewById(R.id.tvNominal)
        val tvAksiHapus: TextView = itemView.findViewById(R.id.tvAksiHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val item = daftarTransaksi[position]
        val context = holder.itemView.context

        holder.tvIkonKategori.text = item.ikonKategori
        holder.tvKeterangan.text = item.keterangan.ifBlank { item.namaKategori }
        holder.tvKategoriTanggal.text =
            "${FormatUtils.formatTanggalLengkap(item.tanggal)} • ${item.namaKategori}"

        val formattedNominal = FormatUtils.formatRupiah(item.jumlah)
        if (item.tipeTransaksi == TipeTransaksi.PEMASUKAN) {
            holder.tvNominal.text = "+$formattedNominal"
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.income_green))
            holder.cardBadgeIkon.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.income_bg)
            )
        } else {
            holder.tvNominal.text = "-$formattedNominal"
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.expense_red))
            val softColor = try {
                val base = Color.parseColor(item.warnaKategori)
                Color.argb(36, Color.red(base), Color.green(base), Color.blue(base))
            } catch (_: Exception) {
                ContextCompat.getColor(context, R.color.primary_finance_light)
            }
            holder.cardBadgeIkon.setCardBackgroundColor(softColor)
        }

        holder.itemView.setOnClickListener {
            onKlikItem(item)
        }

        holder.tvAksiHapus.setOnClickListener {
            onKlikHapus(item)
        }
    }

    override fun getItemCount(): Int = daftarTransaksi.size

    fun updateData(newList: List<Transaksi>) {
        daftarTransaksi = newList
        notifyDataSetChanged()
    }
}
