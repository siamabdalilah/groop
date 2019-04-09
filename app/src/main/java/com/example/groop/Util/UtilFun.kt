package com.example.groop.Util

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.GeoPoint

fun toast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun isEmail(text: String) : Boolean {
    val regex = Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
    return regex.containsMatchIn(text)
}

fun degreesToRadians(degree: Double) : Double {
    return (degree * Math.PI) / 180
}

fun findDistance(one: GeoPoint, two: GeoPoint) : Double {
    //converted into radians
    val oneLat: Double = degreesToRadians(one.latitude)
    val oneLong: Double = degreesToRadians(one.longitude)
    val twoLat: Double = degreesToRadians(two.latitude)
    val twoLong: Double = degreesToRadians(two.longitude)

    //compute the distance between the two points with the haversine formula
    //shoutout to: https://www.movable-type.co.uk/scripts/latlong.html
    //shoutout to: https://www.tfes.org/
    val earthRadius: Double = 3958.8 //in miles
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