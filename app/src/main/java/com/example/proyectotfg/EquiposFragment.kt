package com.example.proyectotfg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectotfg.databinding.FragmentEquiposBinding

class EquiposFragment : Fragment(R.layout.fragment_equipos) {

    private lateinit var binding: FragmentEquiposBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEquiposBinding.inflate(inflater,container,false)
        val vista = binding.root

        binding.btnconsultarequipo.setOnClickListener{
            val intent = Intent(activity, GestionarEquipos::class.java)
            startActivity(intent)
        }

        binding.btninsertarequipo.setOnClickListener{
            val intent = Intent(activity, AnadirEquipo::class.java)
            startActivity(intent)
        }

        return vista
    }

}
