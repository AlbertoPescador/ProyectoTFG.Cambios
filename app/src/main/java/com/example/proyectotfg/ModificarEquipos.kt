package com.example.proyectotfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectotfg.databinding.ActivityModificarEquiposBinding
import com.google.firebase.firestore.FirebaseFirestore

class ModificarEquipos : AppCompatActivity() {

    private lateinit var binding: ActivityModificarEquiposBinding
    private lateinit var equipo: Equipos

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarEquiposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        equipo = intent.getSerializableExtra("Nombre") as? Equipos ?: Equipos()

        rellenarCampos()

        binding.modificarequipotv.setOnClickListener {
            // Alerta de diálogo para guardar o cancelar
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Deseas guardar los cambios?")

            // Si se ha pulsado guardar
            builder.setPositiveButton("Guardar") { dialog, which ->
                guardarCambios()
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                // Acción al pulsar el botón Cancelar
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    // Rellenar campos
    private fun rellenarCampos() {
        val equipoId = equipo.Nombre // Suponiendo que el nombre del equipo es único
        db.collection("Equipos").document(equipoId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("Nombre")
                    val sede = documentSnapshot.getString("Sede")
                    val paginaWeb = documentSnapshot.getString("PaginaWeb")

                    binding.nombreequipo.setText(nombre)
                    binding.sedeequipo.setText(sede)
                    binding.paginawebequipo.setText(paginaWeb)
                } else {
                    Toast.makeText(this, "No se encontró el equipo en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener los datos del equipo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para actualizar los campos en Firebase
    private fun guardarCambios() {
        val nombreEquipo = binding.nombreequipo.text.toString()
        val sedeEquipo = binding.sedeequipo.text.toString()
        val paginaWebEquipo = binding.paginawebequipo.text.toString()

        if (nombreEquipo.isNotEmpty() && sedeEquipo.isNotEmpty() && paginaWebEquipo.isNotEmpty()) {
            db.collection("Equipos").document(equipo.Nombre)
                .update(
                    "Nombre", nombreEquipo,
                    "Sede", sedeEquipo,
                    "PaginaWeb", paginaWebEquipo
                )
                .addOnSuccessListener {
                    // Notificar al usuario que los cambios se han guardado exitosamente
                    Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
                    // Regresar a la actividad principal
                    val intent = Intent(this, GestionarEquipos::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    // Notificar al usuario que ha ocurrido un error
                    Toast.makeText(this, "Error al guardar cambios: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Se han dejado campos en blanco.", Toast.LENGTH_SHORT).show()
        }
    }
}
