package com.trilinggo.kids.ui.sukukata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trilinggo.kids.data.model.ConsonantGroup
import com.trilinggo.kids.databinding.ItemKonsonanBinding

/**
 * Adapter untuk grid pilihan konsonan/vokal di halaman menu Suku Kata.
 * Setiap item menampilkan nama grup (contoh "B", "C", atau "Vokal") dan
 * memanggil callback ketika diklik.
 */
class ConsonantAdapter(
    private val groups: List<ConsonantGroup>,
    private val onItemClick: (ConsonantGroup) -> Unit
) : RecyclerView.Adapter<ConsonantAdapter.ConsonantViewHolder>() {

    inner class ConsonantViewHolder(val binding: ItemKonsonanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ConsonantViewHolder {
        val binding = ItemKonsonanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsonantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsonantViewHolder, position: Int) {
        val group = groups[position]
        holder.binding.tvKonsonan.text = group.consonant
        holder.binding.root.setOnClickListener { onItemClick(group) }
    }

    override fun getItemCount(): Int = groups.size
}
