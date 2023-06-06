package com.example.proyectotfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadoparques.adapter.PlayerAdapter
import com.example.proyectotfg.adapterEquipos.TeamAdapter
import com.example.proyectotfg.databinding.ActivityGestionarJugadoresBinding
import com.google.firebase.firestore.FirebaseFirestore

class GestionarJugadores : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityGestionarJugadoresBinding
    private lateinit var adapterJugadores: PlayerAdapter
    private lateinit var listaJugadores: ArrayList<Jugadores>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionarJugadoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaJugadores = ArrayList()

        binding.buscarjugadores.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredList = ArrayList<Jugadores>()
                for (jugador in listaJugadores) {
                    if (jugador.Nickname.contains(newText, ignoreCase = true)) {
                        filteredList.add(jugador)
                    }
                }

                // Actualizar el RecyclerView con la lista filtrada
                val adapter = PlayerAdapter(filteredList)
                binding.recyclerjugadores.adapter = adapter

                return true
            }
        })
        adapterJugadores = PlayerAdapter(listaJugadores)

        db.collection("Jugadores").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val player = document.toObject(Jugadores::class.java)
                    player.Nickname = document["Nombre"].toString()
                    player.Nacionalidad = document["Nacionalidad"].toString()
                    player.Equipo = document["Equipo"].toString()
                    player.Foto = document["Foto"].toString()
                    listaJugadores.add(player)
                }
                adapterJugadores.notifyDataSetChanged()
            }

        binding.recyclerjugadores.layoutManager = LinearLayoutManager(this)
        binding.recyclerjugadores.adapter = adapterJugadores
    }
}