package com.example.proyectotfg

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectotfg.databinding.FragmentContactoBinding
import android.content.Intent
import android.net.Uri

class ContactoFragment : Fragment(R.layout.fragment_contacto) {

    private lateinit var binding: FragmentContactoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactoBinding.inflate(inflater,container,false)
        val vista = binding.root

        binding.textViewEmail.setOnClickListener {
            sendEmail()
        }

        binding.textViewInstagram.setOnClickListener{
            openInstagramPage("einfo_app")
        }

        binding.textViewFacebook.setOnClickListener{
            openFacebookPage("https://www.facebook.com/profile.php?id=100092946468370") //
        }

        binding.textViewTwitter.setOnClickListener {
            openTwitterPage("einfomobile")
        }

        return vista
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:einfo@gmail.com")
        }
        startActivity(emailIntent)
    }

    private fun openInstagramPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$url"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$url"))
            startActivity(intent)
        }
    }

    private fun openFacebookPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$url"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun openTwitterPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=$url"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$url"))
            startActivity(intent)
        }
    }
}
