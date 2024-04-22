package com.rcordoba.m6p2rcordoba.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcordoba.m6p2rcordoba.data.remote.model.materiaOrbs
import com.rcordoba.m6p2rcordoba.databinding.OrbElementBinding

class OrbAdapter (
    private val orbs: List<materiaOrbs>
): RecyclerView.Adapter<OrbViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrbViewHolder {
        val binding = OrbElementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrbViewHolder(binding)
    }

    override fun getItemCount(): Int = orbs.size

    override fun onBindViewHolder(holder: OrbViewHolder, position: Int) {
        val orb = orbs[position]

        holder.bind(orb)
    }

}