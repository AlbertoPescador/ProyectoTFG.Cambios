package com.example.proyectotfg

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.proyectotfg.databinding.ActivityAnadirEquipoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException

class AnadirEquipo : AppCompatActivity() {

    lateinit var binding: ActivityAnadirEquipoBinding

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val storageReference = storage.getReference("FotosEquipos/")
    var urlFoto: String = ""
    lateinit var imageBitmap: Bitmap
    private var galeria = 0
    private var camara = 1


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAnadirEquipoBinding.inflate(layoutInflater)
            setContentView(binding.root)

            limpiarcampos()

            imageBitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888)

            binding.btnAbrirGaleria3.setOnClickListener {
               fotoGaleria()
            }

            binding.btnAccederCamara3.setOnClickListener {
                fotoCamara()
            }

            binding.btnanadirequipo.setOnClickListener {
                var nombre = binding.nombreequipo.text.toString()
                var sede = binding.sedeequipo.text.toString()
                var web = binding.paginawebequipo.text.toString()

                if (binding.nombreequipo.text.isNotEmpty() && binding.sedeequipo.text.isNotEmpty() && binding.paginawebequipo.text.toString().isNotEmpty() )
                {
                    val auth = FirebaseAuth.getInstance()
                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val data = baos.toByteArray()
                    val imageRef = storageReference.child("${nombre}.jpg")
                    val uploadTask = imageRef.putBytes(data)

                    auth.currentUser?.let {
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            imageRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                urlFoto = downloadUri.toString()
                                Log.d("Firebase", "URL de imagen: $urlFoto")
                                // Aquí puedes guardar la URL de la imagen en Firebase Firestore o realizar otras operaciones
                            } else {
                                Log.e(
                                    "Firebase",
                                    "Error al obtener la URL de la imagen",
                                    task.exception
                                )
                            }
                        }
                    } ?: run {
                        // El usuario actual es nulo, no se realiza ninguna acción
                    }


                    Handler(Looper.getMainLooper()).postDelayed({
                        val equipoData = hashMapOf(
                            "Nombre" to nombre,
                            "Sede" to sede,
                            "PaginaWeb" to web,
                            "Foto" to urlFoto
                        )

                        db.collection("Equipos").document(nombre.hashCode().toString()).set(equipoData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Se ha guardado con éxito", Toast.LENGTH_SHORT).show()
                                limpiarcampos()

                                onBackPressed()
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firebase", "Error al guardar el equipo: $e")
                            }

                    }, 1000) // 1000 milisegundos = 1 segundo
                }
                else
                {
                    Toast.makeText(this,"Algún campo está vacío", Toast.LENGTH_SHORT).show()
                }
            }
        }



    private fun fotoGaleria() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent,galeria)
    }

    private fun fotoCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,camara)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == galeria) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val fotoGaleria = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)

                        Log.i("ImagenGuardada","Imagen guardada en el dispositivo")
                        imageBitmap = fotoGaleria
                        binding.imgImagenEquipo.setImageBitmap(fotoGaleria)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.e("Error", "Error al guardar la imagen")
                    }
                }
            } else if (requestCode == camara) {
                val fotoCamara = data?.extras?.get("data") as? Bitmap
                if (fotoCamara != null) {
                    imageBitmap = fotoCamara
                    binding.imgImagenEquipo.setImageBitmap(fotoCamara)
                }
            }
        }
    }

    private fun limpiarcampos(){
        binding.nombreequipo.setText("")
        binding.sedeequipo.setText("")
        binding.paginawebequipo.setText("")
    }
}


