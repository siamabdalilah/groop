package com.example.groop

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.groop.Util.Activity
import com.example.groop.Util.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*

class LocationServices {
    private lateinit var location : Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var context: Context
    private lateinit var activity:android.app.activity
    fun getLocation(context: Context, activity:android.app.activity)
    {
        this.context=context
        this.activity=activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    private fun locate(){
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return

        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it == null){
//                toast(this, "Failed. Please try again")
                fusedLocationClient.requestLocationUpdates(LocationRequest.create(), object : LocationCallback(){
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        toast(context, "got here")
                        locate()
                    }
                }, null)

            }else{
                location = it
            }
        }

    }

    private fun locatePrompt(){
        toast(this.context, "Please locate first")
    }
}