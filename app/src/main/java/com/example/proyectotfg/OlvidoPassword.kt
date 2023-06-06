package com.example.proyectotfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OlvidoPassword : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_olvido_password)

        val textemail : TextView = findViewById(R.id.olvido_email)
        val btncambiar : Button = findViewById(R.id.btnenviarcorreo)
        btncambiar.setOnClickListener(){
            sendPasswordReset(textemail.text.toString())
        }
        firebaseAuth = Firebase.auth
    }

    private fun sendPasswordReset (email: String){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "¡ENVIADO CON ÉXITO! Consulte su correo para cambiar la contraseña", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "ERROR: No se pudo completar el proceso", Toast.LENGTH_SHORT).show()
                }
            }
    }
}