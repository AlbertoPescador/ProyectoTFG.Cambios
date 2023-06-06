package com.example.listadoparques.adapter;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectotfg.Jugadores
import com.example.proyectotfg.databinding.ListaJugadoresBinding


class PlayerViewHolder(view:View): RecyclerView.ViewHolder(view) {
    val binding = ListaJugadoresBinding.bind(view)

    fun render(jugadoresModel: Jugadores){
        binding.jugadorview.text = "Nombre:" + "\t" + jugadoresModel.Nickname
        binding.nacionalidadview.text= "Nacionalidad:" + "\t" + jugadoresModel.Nacionalidad
        binding.equipoview.text = "Equipo:" + "\t" + jugadoresModel.Equipo
        Glide.with(binding.fotoview.context).load(jugadoresModel.Foto).into(binding.fotoview)
    }
}
