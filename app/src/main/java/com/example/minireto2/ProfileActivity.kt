package com.example.minireto2

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.minireto2.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.util.jar.Manifest

class ProfileActivity : AppCompatActivity() {
    private val CAMARA_REQUEST_CODE: Int = 101
    private val PERMISSION_CAMARA : Int = 100
    lateinit var binding: ActivityProfileBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPhoto.setOnClickListener {
            askPermissions()
        }

        lateinit var auth: FirebaseAuth

// Initialize Firebase Auth
        auth = Firebase.auth

        //set up
        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("Email")
        setup(email ?: "")
    }



    private fun setup(email: String){
        title = "Profile"
        val emailProfile: TextView = findViewById(R.id.tvEmailProfile)
        emailProfile.text = "Hello $email"
        val logOut:Button = findViewById(R.id.btnLogout)
        logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermissions() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                takePhoto()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                showMessage("The permission was previously denied, allow it in settings ")
            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CAMARA)
            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMARA_REQUEST_CODE)
    }

    fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMARA_REQUEST_CODE ->{
                if(resultCode != Activity.RESULT_OK){
                    showMessage("Photo was not taken")
                }
                else{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.ivProfile.setImageBitmap(bitmap)
                }
            }
        }
    }

}