package com.example.proyectotfg.adapterEquipos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotfg.Equipos
import com.example.proyectotfg.ModificarEquipos
import com.example.proyectotfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.util.ArrayList

class TeamAdapter(private val listaEquipos:ArrayList<Equipos>) : RecyclerView.Adapter<TeamViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return TeamViewHolder(layoutInflater.inflate(R.layout.lista_equipos,parent,false))
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val item = listaEquipos[position]
        holder.binding.paginawebview.setOnClickListener {
            openWebPage(item.PaginaWeb, holder.itemView.context)
        }
        holder.render(item)


        holder.itemView.setOnLongClickListener{
            showConfirmationDialog(holder,item)
            true
        }

        holder.itemView.setOnClickListener{

        }

    }

    private fun openWebPage(url: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return listaEquipos.size
    }

    // Función para mostrar un cuadro de diálogo con dos opciones y pasar los datos del equipo si se acepta
    fun showConfirmationDialog(holder: TeamViewHolder, equipo: Equipos) {
        val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
        dialogBuilder.setTitle("Gestión: Equipo")
        dialogBuilder.setMessage("¿Qué opción desea emplear sobre el equipo?")
        dialogBuilder.setPositiveButton("Eliminar") { dialog, which ->

            borrarEquipo(equipo.Nombre.hashCode().toString(), holder)
            Toast.makeText(holder.itemView.context, "El equipo se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.setNegativeButton("Modificar") { dialog, which ->
            val intent = Intent(holder.itemView.context, ModificarEquipos::class.java)
            intent.putExtra("Nombre", equipo as Serializable) // donde "equipo" es el objeto Equipos que deseas pasar
            holder.itemView.context.startActivity(intent)
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun borrarEquipo(id: String, holder: TeamViewHolder) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        auth.currentUser.let {

            db.collection("Equipos").document(id).delete()
                .addOnSuccessListener {
                    // Equipo borrado exitosamente
                    // Realiza cualquier acción adicional o muestra un mensaje de éxito

                    val index = listaEquipos.indexOfFirst { it.Nombre.hashCode().toString() == id }
                    if (index != -1) {
                        listaEquipos.removeAt(index)
                        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
                        Toast.makeText(holder.itemView.context, "El equipo se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Error al borrar el equipo
                    // Muestra un mensaje de error o maneja la excepción
                    Toast.makeText(holder.itemView.context, "Error al intentar elimina el equipo", Toast.LENGTH_SHORT).show()
                }
        }
    }

}