package com.example.proyectotfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectotfg.adapterEquipos.TeamAdapter
import com.example.proyectotfg.databinding.ActivityGestionarEquiposBinding
import com.google.firebase.firestore.FirebaseFirestore

class GestionarEquipos : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityGestionarEquiposBinding
    private lateinit var adapterEquipos: TeamAdapter
    private lateinit var listaEquipos: ArrayList<Equipos>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionarEquiposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaEquipos = ArrayList()

        binding.buscarequipo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredList = ArrayList<Equipos>()
                for (equipo in listaEquipos) {
                    if (equipo.Nombre.contains(newText, ignoreCase = true)) {
                        filteredList.add(equipo)
                    }
                }

                // Actualizar el RecyclerView con la lista filtrada
                val adapter = TeamAdapter(filteredList)
                binding.recyclerequipos.adapter = adapter

                return true
            }
        })

        adapterEquipos = TeamAdapter(listaEquipos)

        db.collection("Equipos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val team = document.toObject(Equipos::class.java)
                    team.Nombre = document["Nombre"].toString()
                    team.Sede = document["Sede"].toString()
                    team.PaginaWeb = document["PaginaWeb"].toString()
                    team.Foto = document["Foto"].toString()
                    listaEquipos.add(team)
                }
                adapterEquipos.notifyDataSetChanged()
            }

        binding.recyclerequipos.layoutManager = LinearLayoutManager(this)
        binding.recyclerequipos.adapter = adapterEquipos
    }
}
