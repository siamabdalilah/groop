package com.example.groop

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.transition.TransitionManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.groop.DataModels.User
import com.example.groop.Util.isEmail
import com.example.groop.Util.toast
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var state = true // Login
    private val auth = FirebaseAuth.getInstance()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isLocated = false
    private lateinit var location : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        switch_button.setOnClickListener{switch()}
        finish_button.setOnClickListener{login()}
        locate_button.setOnClickListener({locate()})
    }

    fun switch(){
        TransitionManager.beginDelayedTransition(login_screen)
        when(state){
            true -> {
                state = false
                log_status.text = "Sign Up"
                switch_button.text = "Login"
                finish_button.text = "Sign Up"
                finish_button.setOnClickListener{locatePrompt()}
                signup_info.visibility = View.VISIBLE
                locate_button.visibility = View.VISIBLE
            }
            else -> {
                state = true
                log_status.text = "Login"
                switch_button.text = "Sign Up"
                finish_button.text = "Login"
                finish_button.setOnClickListener{login()}
                signup_info.visibility = View.GONE
                locate_button.visibility = View.GONE

                user_confirm_password.setText("")
                user_name.setText("")
                bio_info.setText("")
                isLocated = false
            }
        }
    }

    private fun login(){
        val email = user_email.text.toString()
        if (!isEmail(email)) {
            toast(this, "Please enter valid email")
            return
        }
        val password = user_password.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful){
                //val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else {
                toast(this, "sign in failed")
            }
        }
    }

    private fun signup(){
        if (user_confirm_password.text.toString() != user_password.text.toString()) {
            toast(this, "Password does not match")
            return
        }
        if (user_name.text.toString() == "") {
            toast(this, "Name cannot be empty")
            return
        }

        val email = user_email.text.toString()
        val password = user_password.text.toString()
        val name = user_name.text.toString()
        val bio = bio_info.text.toString()
        val loc = GeoPoint(location.latitude, location.longitude)
        val user = User(email, name,loc, bio)

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful){
                db.collection("users").document(user.email).set(user)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }else{
                toast(this, "Failed. Try again")
            }
        }
    }


    private fun locate(){
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return

        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it == null){
//                toast(this, "Failed. Please try again")
                fusedLocationClient.requestLocationUpdates(LocationRequest.create(), object : LocationCallback(){
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        toast(this@MainActivity, "got here")
                        locate()
                    }
                }, null)

            }else{
                location = it
                isLocated = true
                TransitionManager.beginDelayedTransition(login_screen_button_container)
                locate_button.visibility = View.GONE
                finish_button.setOnClickListener{signup()}
            }
        }

    }

    private fun locatePrompt(){
        toast(this, "Please locate first")
    }

}
