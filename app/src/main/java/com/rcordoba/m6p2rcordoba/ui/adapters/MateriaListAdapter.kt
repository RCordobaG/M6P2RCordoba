package com.rcordoba.m6p2rcordoba.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.databinding.MateriaListElementBinding

class MateriaListAdapter (
    private val types: List< MateriaDTO>,
    private val onTypeClicked: (MateriaDTO) -> Unit
) : RecyclerView.Adapter<MateriaListTypeHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriaListTypeHolder {
        val binding = MateriaListElementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MateriaListTypeHolder(binding)
    }

    override fun getItemCount(): Int = types.size

    override fun onBindViewHolder(holder: MateriaListTypeHolder, position: Int) {
        val type = types[position]

        holder.bind(type)

        Glide.with(holder.itemView.context)
            .load(type.thumbnail)
            .into(holder.thumbnail)


        holder.itemView.setOnClickListener{
            onTypeClicked(type)
        }
    }
}