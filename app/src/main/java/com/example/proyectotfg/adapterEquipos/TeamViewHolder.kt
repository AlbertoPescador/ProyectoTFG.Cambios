package com.example.proyectotfg.adapterEquipos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectotfg.Equipos
import com.example.proyectotfg.databinding.ListaEquiposBinding

class TeamViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ListaEquiposBinding.bind(view)

    fun render(equiposModel: Equipos) {
        binding.teamview.text = "Nombre:" + "\t" +equiposModel.Nombre
        binding.sedeview.text = "Sede: " + "\t" +  equiposModel.Sede
        binding.paginawebview.text = "Pagina Web"
        Glide.with(binding.fotoviewequipo.context).load(equiposModel.Foto).into(binding.fotoviewequipo)
    }
}