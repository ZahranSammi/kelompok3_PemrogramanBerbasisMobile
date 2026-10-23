package com.example.aplikasipencatatankeuanganpribadi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasipencatatankeuanganpribadi.R
import com.example.aplikasipencatatankeuanganpribadi.model.Kategori
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils

class GridKategoriAdapter(
    private var daftarKategori: List<Kategori>,
    private var nominalPerKategori: Map<Long, Double>,
    private val onKlikTambah: () -> Unit,
    private val onKlikKategori: (Kategori) -> Unit
) : RecyclerView.Adapter<GridKategoriAdapter.GridViewHolder>() {

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIkon: TextView = itemView.findViewById(R.id.tvIkonGridKategori)
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaGridKategori)
        val tvSub: TextView = itemView.findViewById(R.id.tvSubGridKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grid_kategori, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val context = holder.itemView.context
        if (position == 0) {
            // Item pertama adalah tombol "+ Add" seperti pada gambar referensi
            holder.tvIkon.text = "＋"
            holder.tvIkon.setTextColor(ContextCompat.getColor(context, R.color.primary_finance))
            holder.tvNama.text = "Tambah"
            holder.tvSub.text = "Kategori"
            holder.itemView.setOnClickListener { onKlikTambah() }
        } else {
            val kat = daftarKategori[position - 1]
            holder.tvIkon.text = kat.ikon
            holder.tvNama.text = kat.namaKategori
            val total = nominalPerKategori[kat.idKategori] ?: 0.0
            holder.tvSub.text = if (total > 0) {
                FormatUtils.formatRupiahSingkat(total)
            } else {
                kat.tipeKategori.label
            }
            holder.itemView.setOnClickListener { onKlikKategori(kat) }
        }
    }

    override fun getItemCount(): Int = daftarKategori.size + 1

    fun updateData(newKategori: List<Kategori>, newMapNominal: Map<Long, Double>) {
        daftarKategori = newKategori
        nominalPerKategori = newMapNominal
        notifyDataSetChanged()
    }
}
