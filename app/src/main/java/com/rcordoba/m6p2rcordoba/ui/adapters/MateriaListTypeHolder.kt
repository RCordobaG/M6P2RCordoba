package com.rcordoba.m6p2rcordoba.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.databinding.MateriaListElementBinding
import com.rcordoba.m6p2rcordoba.databinding.MateriaTypeElementBinding

class MateriaListTypeHolder(private var binding: MateriaListElementBinding) :
        RecyclerView.ViewHolder(binding.root){

            val thumbnail = binding.materiaThumbnail

            fun bind (type: MateriaDTO){
                binding.typeText.text = type.type
                binding.descriptionText.text = type.description
            }
        }