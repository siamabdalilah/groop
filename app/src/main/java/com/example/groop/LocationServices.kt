package com.example.groop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.groop.Util.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
class LocationServices {
    private lateinit var location : Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var context: Context
    fun getLocation(context: Context):Location
    {
        this.context=context
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locate()
        return location
    }
    private fun locate(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(context as Activity,
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