package com.example.groop.Util

import com.example.groop.DataModels.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.model.value.TimestampValue

class Groop(capacity: Int, creator: DocumentReference, description: String,
            location: GeoPoint, members: ArrayList<DocumentReference>, name: String,
            numMembers: Int, startTime: TimestampValue, type: String) {
    val capacity: Int = capacity
    val creator: DocumentReference = creator
    val description: String = description
    val location: GeoPoint = location
    val members: ArrayList<DocumentReference> = members
    val name: String = name
    val numMembers: Int = numMembers
    val startTime: TimestampValue = startTime
    val type: String = type

}