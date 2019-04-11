package com.example.groop.Util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.groop.*
import com.example.groop.HomePackage.home
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.GeoPoint

fun toast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun isEmail(text: String) : Boolean {
    val regex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
    return regex.containsMatchIn(text)
}

/**
 * Insert this function below bottom nav activities and pass the bottom_bar_{activityname}.bottom_bar_layout
 * to set up the bottom navigation bar
 */
fun setupBottomNav(context: Context, view: BottomNavigationView){
    // not quite sure if this is necessary yet
    val act = when(context){
        is HomeActivity     -> {
            view.selectedItemId = R.id.nav_home
            context as HomeActivity
        }
        is display_users    -> {
            view.selectedItemId = R.id.nav_people
            context as HomeActivity
        }
        is display_groops   -> {
            view.selectedItemId = R.id.nav_groops
            context as HomeActivity
        }
        else                -> {
            Log.wtf("wtf", "How the f did this happen")
            context as home
        }
    }

    view.setOnNavigationItemSelectedListener {
        val intent = when(it.title){
            "Home"      -> {
                Log.d("title", "Home")
                if (!(act is HomeActivity)) Intent(act, HomeActivity::class.java) else null
            }
            "People"    -> {
                Log.d("title", "People")
                if (!(act is display_users)) Intent(act, HomeActivity::class.java) else null
            }
            else        -> {
                Log.d("title", "Title")
                if (!(act is display_groops)) Intent(act, HomeActivity::class.java) else null
            }
        }
        if (intent != null) {
            act.startActivity(intent)
            return@setOnNavigationItemSelectedListener true
        }
        false
    }
}

/**
 * Helpful for the findDistance method, converts a degree measurement
 * to a radian measurement
 */
fun degreesToRadians(degree: Double) : Double {
    return (degree * Math.PI) / 180
}

/**
 * Very simply finds the distance between two GeoPoints through
 * this bizarrely complicated algorithm we found online
 */
fun findDistance(one: GeoPoint, two: GeoPoint) : Double {
    //converted into radians
    val oneLat: Double = degreesToRadians(one.latitude)
    val oneLong: Double = degreesToRadians(one.longitude)
    val twoLat: Double = degreesToRadians(two.latitude)
    val twoLong: Double = degreesToRadians(two.longitude)

    //compute the distance between the two points with the haversine formula
    //shoutout to: https://www.movable-type.co.uk/scripts/latlong.html
    //shoutout to: https://www.tfes.org/
    val earthRadius = 3958.8 //in miles
    val deltaLat = degreesToRadians(two.latitude - one.latitude)
    val deltaLong = degreesToRadians(two.longitude - one.longitude)

    //the actual formula
    val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(oneLat) * Math.sin(twoLat) *
            Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val distance = earthRadius * c

    return distance
}