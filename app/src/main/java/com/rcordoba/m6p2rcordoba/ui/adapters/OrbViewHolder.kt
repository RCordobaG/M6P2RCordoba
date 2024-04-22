package com.rcordoba.m6p2rcordoba.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.rcordoba.m6p2rcordoba.data.remote.model.materiaOrbs
import com.rcordoba.m6p2rcordoba.databinding.FragmentMateriaTypeBinding
import com.rcordoba.m6p2rcordoba.databinding.OrbElementBinding

class OrbViewHolder (private var binding: OrbElementBinding) :
    RecyclerView.ViewHolder(binding.root){



        fun bind(orb: materiaOrbs){
            binding.orbNameText.text = orb.name
            binding.orbIDText.text = orb.id.toString()
            binding.orbDescriptionText.text = orb.description
            binding.orbLevelsTextView.text = orb.levels.toString()
        }
}