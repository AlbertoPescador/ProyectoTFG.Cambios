package com.example.proyectotfg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectotfg.databinding.FragmentJugadoresBinding

class JugadoresFragment : Fragment(R.layout.fragment_jugadores) {

    private lateinit var binding: FragmentJugadoresBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJugadoresBinding.inflate(inflater,container,false)
        val vista = binding.root

        binding.btnconsultarjugadores.setOnClickListener{
            val intent = Intent(activity, GestionarJugadores::class.java)
            startActivity(intent)
        }

        binding.btninsertarjugadores.setOnClickListener{
            val intent = Intent(activity, AnadirJugador::class.java)
            startActivity(intent)
        }

        return vista
    }

}