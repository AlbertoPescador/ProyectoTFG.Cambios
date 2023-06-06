package com.example.listadoparques.adapter;

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotfg.Jugadores
import com.example.proyectotfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlayerAdapter(private val listaJugadores:ArrayList<Jugadores>) : RecyclerView.Adapter<PlayerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return PlayerViewHolder(layoutInflater.inflate(R.layout.lista_jugadores,parent,false))
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val item = listaJugadores[position]
        holder.render(item)

        holder.itemView.setOnLongClickListener{
            showConfirmationDialog(holder,item)
            true
        }

        holder.itemView.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return listaJugadores.size
    }

    // Función para mostrar un cuadro de diálogo con dos opciones y pasar los datos del equipo si se acepta
    fun showConfirmationDialog(holder: PlayerViewHolder, jugadores: Jugadores) {
        val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
        dialogBuilder.setTitle("Gestión: Jugador")
        dialogBuilder.setMessage("¿Qué opción desea emplear sobre el equipo?")
        dialogBuilder.setPositiveButton("Eliminar") { dialog, which ->

            borrarJugador(jugadores.Nickname.hashCode().toString(), holder)

        }
        dialogBuilder.setNegativeButton("Modificar", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun borrarJugador(id: String, holder: PlayerViewHolder) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        auth.currentUser.let {

            db.collection("Jugadores").document(id).delete()
                .addOnSuccessListener {
                    // Equipo borrado exitosamente
                    // Realiza cualquier acción adicional o muestra un mensaje de éxito

                    val index = listaJugadores.indexOfFirst { it.Nickname.hashCode().toString() == id }
                    if (index != -1) {
                        listaJugadores.removeAt(index)
                        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
                        Toast.makeText(holder.itemView.context, "El jugador se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Error al borrar el equipo
                    // Muestra un mensaje de error o maneja la excepción
                    Toast.makeText(holder.itemView.context, "Error al intentar elimina el jugador", Toast.LENGTH_SHORT).show()
                }
        }
    }
}