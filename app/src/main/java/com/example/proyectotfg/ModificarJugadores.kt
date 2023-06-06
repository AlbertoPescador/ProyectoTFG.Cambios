package com.example.proyectotfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectotfg.databinding.ActivityModificarJugadoresBinding
import com.google.firebase.firestore.FirebaseFirestore

class ModificarJugadores : AppCompatActivity() {

    lateinit var binding: ActivityModificarJugadoresBinding

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarJugadoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}