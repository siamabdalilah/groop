package com.example.groop.Util

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.model.value.TimestampValue

class Groop(capacity: Int, creator: User, description: String,
            location: GeoPoint, members: ArrayList<User>, name: String,
            numMembers: Int, startTime: TimestampValue, type: Activity) {
    private val capacity: Int = capacity
    private val creator: User = creator
    private val description: String = description
    private val location: GeoPoint = location
    private val members: ArrayList<User> = members
    private val name: String = name
    private val numMembers: Int = numMembers
    private val startTime: TimestampValue = startTime
    private val type: Activity = type

}