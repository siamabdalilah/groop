package com.example.groop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.groop.Util.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.firestore.GeoPoint
import com.schibstedspain.leku.LocationPickerActivity

class GroopLocation(private val activity: Activity) {
    private val fusedLocationClient = FusedLocationProviderClient(activity)

    private lateinit var location: Location

    fun getLocation(): Location {

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
            return loc;


        }else{
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
            )
            return getLocation()
        }
    }

    fun pickLocation(defaulLocation: GeoPoint? = null){
        val defLoc = if (defaulLocation == null) {
            val loc = getLocation()
            GeoPoint(loc.latitude, loc.longitude)
        } else defaulLocation
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withLocation(defLoc.latitude, defLoc.longitude).withDefaultLocaleSearchZone()
            .withGeolocApiKey("")
    }

    companion object {
        fun getLocation(activity: Activity): Location {

            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                val loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
                return loc;


            }else{
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
                )
                return getLocation(activity)
            }
        }
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