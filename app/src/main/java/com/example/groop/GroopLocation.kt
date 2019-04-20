package com.example.groop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.schibstedspain.leku.*
import com.schibstedspain.leku.geocoder.GeocoderPresenter
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

class GroopLocation(private val activity: Activity) {

    val fusedLocationClient = FusedLocationProviderClient(activity)

    fun getLocation(): Location {
        android.util.Log.d("location", "got here3")
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            android.util.Log.d("location", "got here")
//            var loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var loc  = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//            if (loc == null){
//                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//            }
            if(loc == null){
                loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            }
            return loc;


        }else{
            android.util.Log.d("location", "got here2")
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return getLocation()
        }
    }

    fun pickLocation(defaulLocation: GeoPoint? = null){
        val defLoc = if (defaulLocation == null) {
            @SuppressWarnings("MissingPermission")
            val loc = fusedLocationClient.lastLocation

            while(!loc.isComplete){}
            GeoPoint(loc.result?.latitude ?: 0.0, loc.result?.longitude ?: 0.0)
        } else defaulLocation
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withLocation(defLoc.latitude, defLoc.longitude).withDefaultLocaleSearchZone()
            .withGeolocApiKey("AIzaSyD7np_VilrYuOrBvwSlrCJvKXTPtVVXAjY").build(activity)

        activity.startActivityForResult(locationPickerIntent, 2)
    }

    fun getGeoPoint(requestCode: Int, resultCode: Int, data: Intent?): GeoPoint?{
        if (resultCode == Activity.RESULT_OK && data != null){
            if (requestCode == 1 || requestCode == 2){
                val lat = data.getDoubleExtra(LATITUDE, 0.0)
                val lon = data.getDoubleExtra(LONGITUDE, 0.0)
                return GeoPoint(lat, lon)
            }else{
                return null
            }
        }else{
            return null
        }
    }

    fun getAddress(requestCode: Int, resultCode: Int, data: Intent?) : String{
        if (requestCode == 1 || requestCode == 2) return data?.getStringExtra(ADDRESS)?:""
        return ""
    }

    companion object {
        fun getLocation(activity: Activity): Location {

            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
                return loc;


            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
                )
                return getLocation(activity)
            }
        }
    }
}