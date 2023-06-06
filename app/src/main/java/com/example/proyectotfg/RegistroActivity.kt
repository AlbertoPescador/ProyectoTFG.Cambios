package com.example.proyectotfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectotfg.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnregistrarse.setOnClickListener {
            registrar()
        }
    }

    fun registrar() {
        val nombre = binding.nombre.text.toString()
        val apellidos = binding.apellidos.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        // Verificar que los campos no estén vacíos
        if (nombre.isNotEmpty() && apellidos.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val db = FirebaseFirestore.getInstance()
                    val usuariosCollection = db.collection("Usuarios")
                    val usuarioDocument = usuariosCollection.document(email)

                    val usuariosData = hashMapOf(
                        "Nombre" to nombre,
                        "Apellidos" to apellidos,
                        "Email" to email,
                        "Password" to password
                    )

                    usuarioDocument.set(usuariosData).addOnSuccessListener {
                        Toast.makeText(this, "USUARIO REGISTRADO CON ÉXITO", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Error en el registro del nuevo usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error en el registro del nuevo usuario: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_SHORT).show()
        }
    }

}