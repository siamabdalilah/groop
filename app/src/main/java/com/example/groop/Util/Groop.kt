package com.example.groop.Util

import com.example.groop.DataModels.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.model.value.TimestampValue
import java.io.Serializable
import java.sql.Timestamp

class Groop: Serializable {

    var capacity: Int = 0
    var creator: DocumentReference? = null
    var description: String = ""
    var location: GeoPoint = GeoPoint(0.0,0.0)
    var members: ArrayList<DocumentReference>? = null
    var name: String = ""
    var numMembers: Int = 0
    var startTime: TimestampValue? = null
    var type: String = ""

    constructor(
        capacity: Int, creator: DocumentReference, description: String,
        location: GeoPoint, members: ArrayList<DocumentReference>, name: String,
        numMembers: Int, startTime: TimestampValue, type: String
    ) {
         this.capacity = capacity
        this.creator = creator
        this.description = description
        this.location = location
        this.members = members
        this.name = name
        this.numMembers = numMembers
        this.startTime = startTime
        this.type = type

    }
}