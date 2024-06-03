package com.rcordoba.m6p2rcordoba.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.rcordoba.m6p2rcordoba.data.remote.model.materiaOrbs
import com.rcordoba.m6p2rcordoba.databinding.OrbElementBinding

class OrbAdapter (
    private val orbs: List<materiaOrbs>,
    context : Context
): RecyclerView.Adapter<OrbViewHolder>(){

    val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrbViewHolder {
        val binding = OrbElementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrbViewHolder(binding, context)
    }

    override fun getItemCount(): Int = orbs.size

    override fun onBindViewHolder(holder: OrbViewHolder, position: Int) {
        val orb = orbs[position]

        holder.bind(orb)
    }

}