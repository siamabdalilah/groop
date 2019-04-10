package com.example.groop.Util

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.model.value.TimestampValue

class Groop(capacity: Int, creator: User, description: String,
            location: GeoPoint, members: ArrayList<User>, name: String,
            numMembers: Int, startTime: TimestampValue, type: Activity_groop) {
    val capacity: Int = capacity
    val creator: User = creator
    val description: String = description
    val location: GeoPoint = location
    val members: ArrayList<User> = members
    val name: String = name
    val numMembers: Int = numMembers
    val startTime: TimestampValue = startTime
    val type: Activity_groop = type

}