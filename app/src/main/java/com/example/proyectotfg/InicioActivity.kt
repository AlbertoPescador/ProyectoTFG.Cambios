package com.example.proyectotfg

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.proyectotfg.databinding.ActivityInicioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import org.checkerframework.checker.interning.qual.EqualsMethod

class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // cargo el header del menu lateral
        val header = binding.navView.getHeaderView(0)
        val email = header.findViewById<TextView>(R.id.textView_userGmail)
        val nombre = header.findViewById<TextView>(R.id.textView_userName)
        obtenerUsername { username ->
            nombre.text = username
        }
        email.text = FirebaseAuth.getInstance().currentUser?.email


        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.inicio -> {
                    supportFragmentManager.beginTransaction().apply {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, HomeFragment()).commit()
                        binding.drawerLayout.closeDrawers()
                    }
                }

                R.id.inicio_equipos -> {
                    supportFragmentManager.beginTransaction().apply {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, EquiposFragment()).commit()
                        binding.drawerLayout.closeDrawers()
                    }
                }

                R.id.inicio_jugadores -> {
                    supportFragmentManager.beginTransaction().apply {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, JugadoresFragment()).commit()
                        binding.drawerLayout.closeDrawers()
                    }
                }

                R.id.inicio_contacto -> {
                    supportFragmentManager.beginTransaction().apply {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, ContactoFragment()).commit()
                        binding.drawerLayout.closeDrawers()
                    }
                }

                R.id.inicio_modonocturno -> {
                    ModoNocturno()
                }

                R.id.inicio_cerrarsesion -> {
                    // Aquí puedes agregar la lógica para cerrar sesión
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "Se ha cerrado sesión correctamente.", Toast.LENGTH_SHORT)
                        .show()
                    // Volver a la actividad del MainActivity (al inicio de seccion)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun obtenerUsername(callback: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val correo = auth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()

        db.collection("Usuarios").document(correo).get()
            .addOnSuccessListener { documentSnapshot ->
                var username = ""
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("Nombre")
                    val apellido = documentSnapshot.getString("Apellidos")
                    username = "$nombre $apellido"
                    Log.d("Usuario", "Datos Usuario: $username")
                }
                Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
                callback(username) // Llamar a la devolución de llamada con el valor de username
            }
    }

    private fun ModoNocturno() {
        val activoModoNocturno = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val nuevoModoNocturno = when (activoModoNocturno) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nuevoModoNocturno)
        recreate() //
    }
}