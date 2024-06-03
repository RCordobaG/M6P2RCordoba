package com.rcordoba.m6p2rcordoba.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.MediaController
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rcordoba.m6p2rcordoba.data.remote.model.materiaOrbs
import com.rcordoba.m6p2rcordoba.databinding.FragmentMateriaTypeBinding
import com.rcordoba.m6p2rcordoba.databinding.OrbElementBinding
import com.rcordoba.m6p2rcordoba.ui.fragments.MateriaListTypeFragment

class OrbViewHolder (private var binding: OrbElementBinding, context: Context) :
    RecyclerView.ViewHolder(binding.root){

    val context = context
    fun bind(orb: materiaOrbs){
            binding.orbNameText.text = orb.name
            binding.orbIDText.text = orb.id.toString()
            binding.orbDescriptionText.text = orb.description
            binding.orbLevelsTextView.text = orb.levels.toString()
            if(orb.video.isNullOrEmpty()) {
                binding.materiaVideoView.visibility = View.INVISIBLE

            }
            else{
                val controller = MediaController(context)
                binding.materiaVideoView.setMediaController(controller)
                binding.materiaVideoView.setVideoURI(Uri.parse(orb.video))
                controller.setAnchorView(binding.materiaVideoView)
            }
        }
}