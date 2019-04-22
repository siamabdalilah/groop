package com.example.groop.DataModels

import com.google.common.util.concurrent.AtomicDouble
import com.google.firebase.firestore.GeoPoint

object GeoPoint {
    var lat = AtomicDouble(0.0)
    var long= AtomicDouble(0.0)


}