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

object GroopLocation {

    private lateinit var location: Location

    fun getLocation(activity: Activity, fusedLocationClient: FusedLocationProviderClient): Location {
        locate(activity, fusedLocationClient)
        return location
    }

    private fun locate(activity: Activity, fusedLocationClient: FusedLocationProviderClient) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
            )
            return

        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it == null) {
//                toast(this, "Failed. Please try again")
                fusedLocationClient.requestLocationUpdates(LocationRequest.create(), object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        super.onLocationResult(p0)
                        toast(activity, "got here")
                        locate(activity, fusedLocationClient)
                    }
                }, null)

            } else {
                location = it

            }
        }

    }
}