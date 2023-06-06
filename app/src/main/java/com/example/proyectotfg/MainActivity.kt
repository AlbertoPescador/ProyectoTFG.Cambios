package com.example.proyectotfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectotfg.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btniniciarsesion.setOnClickListener{
            login()
        }

        binding.btnregistrarseprinci.setOnClickListener{
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        binding.btnolvidopassword.setOnClickListener{
            val intent = Intent(this,OlvidoPassword::class.java)
            startActivity(intent)
        }
    }

    fun login() {
        //  Si los campos de correo y password no están vacíos:
        if (binding.escribecorreo.text.isNotEmpty() && binding.escribepassword.text.isNotEmpty()){
             FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.escribecorreo.text.toString(),
                binding.escribepassword.text.toString()
            )
                .addOnCompleteListener {
                    //  Si la autenticación tuvo éxito:
                    if (it.isSuccessful) {

                        Toast.makeText(this, "Login Correcto", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, InicioActivity::class.java)
                        startActivity(intent)

                    } else {
                        //  Sino avisamos al usuario que ocurrió un problema
                        Toast.makeText(this, "Correo o password incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            Toast.makeText(this, "Los campos no pueden ser nulos", Toast.LENGTH_SHORT).show()
        }
    }
}